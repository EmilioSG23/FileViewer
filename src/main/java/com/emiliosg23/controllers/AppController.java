package com.emiliosg23.controllers;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.prefs.Preferences;

import com.emiliosg23.App;
import com.emiliosg23.models.AppService;
import com.emiliosg23.models.enums.Modes;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.tdas.trees.MultiTree;
import com.emiliosg23.utils.AppUtils;
import com.emiliosg23.utils.Consts;
import com.emiliosg23.view.PresentationNode;
import com.emiliosg23.view.ThemeStyle;
import com.emiliosg23.view.TreeRender;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controlador principal de la interfaz de usuario.
 *
 * <p>
 * Gestiona los eventos de la vista FXML ({@code app.fxml}): selección de
 * directorio, alternancia de modos, ajuste de niveles de profundidad, cambio de
 * tema y renderizado del treemap. El escaneo de directorios se ejecuta en un
 * hilo de fondo para no bloquear la interfaz gráfica.</p>
 *
 * @see AppService
 * @see TreeRender
 */
public class AppController {

    @FXML
    private TextField directoryTextField;
    @FXML
    private Button resetButton;
    @FXML
    private VBox background;
    @FXML
    private HBox treeMapPanel;
    @FXML
    private Button showFileOrExtensionButton;
    @FXML
    private Button acumulativeButton;
    @FXML
    private Button executableButton;
    @FXML
    private Button showFilenamesButton;
    @FXML
    private Label levelLabel;
    @FXML
    private Label titleLevelLabel;
    @FXML
    private Button incrementLevelButton;
    @FXML
    private Button decrementLevelButton;
    @FXML
    private Button incrementLevelTitleButton;
    @FXML
    private Button decrementLevelTitleButton;
    @FXML
    private ComboBox<ThemeStyle> themeComboBox;

    private AppService service;
    private TreeRender render;
    private ThemeStyle currentTheme = ThemeStyle.LIGHT;

    public void initialize() {
        Preferences prefs = Preferences.userNodeForPackage(AppController.class);
        String savedTheme = prefs.get("theme", ThemeStyle.DARK.name()); // Valor por defecto
        currentTheme = ThemeStyle.valueOf(savedTheme);

        themeComboBox.getItems().setAll(ThemeStyle.values());
        themeComboBox.setValue(currentTheme);

        Scene scene = themeComboBox.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(currentTheme.getPath());
        } else {
            themeComboBox.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.getStylesheets().clear();
                    //newScene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
                    newScene.getStylesheets().add(currentTheme.getPath());
                }
            });
        }

        // Clipping (recorte) para treeMapPanel
        Rectangle clip = new Rectangle();
        treeMapPanel.setClip(clip);

        // Actualizar el tamaño del clip dinámicamente cuando el tamaño del panel cambie
        treeMapPanel.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            clip.setWidth(newVal.getWidth());
            clip.setHeight(newVal.getHeight());
        });

        this.service = new AppService();
        this.render = new TreeRender();
        //treeMapPanel.prefHeightProperty().bind(background.heightProperty().subtract(192));
        AppUtils.changeButtonState(showFileOrExtensionButton, service.getPanelConfiguration().isFileExtensionMode());
        AppUtils.changeButtonState(acumulativeButton, service.getPanelConfiguration().isAcumulativeMode());
        AppUtils.changeButtonState(executableButton, service.getPanelConfiguration().isExecutableMode());
        AppUtils.changeButtonState(showFilenamesButton, service.getRenderConfiguration().isShowFilenames());

        directoryTextField.setText(service.getPanelConfiguration().getDirectory());
        levelLabel.setText(Integer.toString(service.getRenderConfiguration().getLimitLevel()));
        titleLevelLabel.setText(Integer.toString(service.getRenderConfiguration().getLimitLevelTitle()));
    }

    @FXML
    private void selectDirectory(ActionEvent event) {
        DirectoryChooser selector = new DirectoryChooser();
        selector.setTitle("Select a directory");

        String path = directoryTextField.getText();
        File initialDir = new File(path);
        if (initialDir.exists() && initialDir.isDirectory()) {
            selector.setInitialDirectory(initialDir);
        } else {
            selector.setInitialDirectory(null);
        }

        File selectedDirectory = selector.showDialog(null);
        if (selectedDirectory != null) {
            directoryTextField.setText(selectedDirectory.getAbsolutePath());
            initDirectory(selectedDirectory.getAbsolutePath());
        } else {
            AppUtils.showErrorAlert("There is not selected directory.");
        }
    }

    private void initDirectory(String path) {
        if (path.isEmpty()) {
            AppUtils.showErrorAlert("There is not selected directory.");
            return;
        }
        // Reconfigure panel size
        treeMapPanel.setPrefSize(treeMapPanel.getWidth(), treeMapPanel.getHeight());

        // Show non-blocking loading indicator
        Alert generating = AppUtils.showLoadingAlert("Generating tree...");
        setControlsDisabled(true);

        // Background task: scan directory using parallel ForkJoinPool
        Task<MultiTree<Info>> scanTask = new Task<>() {
            @Override
            protected MultiTree<Info> call() {
                return service.initDirectory(path);
            }
        };

        scanTask.setOnSucceeded(event -> {
            generating.close();
            MultiTree<Info> directoryTree = scanTask.getValue();
            processTreeGenerator(directoryTree, treeMapPanel);
            setControlsDisabled(false);
        });

        scanTask.setOnFailed(event -> {
            generating.close();
            setControlsDisabled(false);
            AppUtils.showErrorAlert("Error scanning directory: " + scanTask.getException().getMessage());
        });

        Thread backgroundThread = new Thread(scanTask);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void processTreeGenerator(MultiTree<Info> tree, HBox panel) {
        // Config Render Engine
        render.setTree(tree);
        render.setConfig(service.getRenderConfiguration());
        // Init directory tree presentation
        MultiTree<PresentationNode> presentationTree = render.initialize(panel);
        // Render directory tree
        render.render(presentationTree, panel);
    }

    private void update() {
        MultiTree<Info> updatedDirectoryTree = service.update();
        if (updatedDirectoryTree != null) {
            processTreeGenerator(updatedDirectoryTree, treeMapPanel);
        }
    }

    /**
     * Habilita o deshabilita los controles de la interfaz durante operaciones
     * de carga.
     *
     * @param disabled {@code true} para deshabilitar, {@code false} para
     * habilitar
     */
    private void setControlsDisabled(boolean disabled) {
        resetButton.setDisable(disabled);
        showFileOrExtensionButton.setDisable(disabled);
        executableButton.setDisable(disabled);
        showFilenamesButton.setDisable(disabled);
        incrementLevelButton.setDisable(disabled);
        decrementLevelButton.setDisable(disabled);
        incrementLevelTitleButton.setDisable(disabled);
        decrementLevelTitleButton.setDisable(disabled);
    }

    @FXML
    private void reset(ActionEvent event) {
        service.reset();
        treeMapPanel.getChildren().clear();

        // Reset buttons
        AppUtils.changeButtonState(showFileOrExtensionButton, service.getPanelConfiguration().isFileExtensionMode());
        AppUtils.changeButtonState(acumulativeButton, service.getPanelConfiguration().isAcumulativeMode());
        AppUtils.changeButtonState(executableButton, service.getPanelConfiguration().isExecutableMode());
        AppUtils.changeButtonState(showFilenamesButton, service.getRenderConfiguration().isShowFilenames());

        showFileOrExtensionButton.setDisable(false);
        acumulativeButton.setDisable(true);
        showFilenamesButton.setDisable(false);
        executableButton.setDisable(false);

        // Reset panel
        directoryTextField.setText(service.getPanelConfiguration().getDirectory());

        int currentLevel = service.getRenderConfiguration().getLimitLevel();
        int currentTitleLevel = service.getRenderConfiguration().getLimitLevelTitle();

        levelLabel.setText(Integer.toString(currentLevel));
        titleLevelLabel.setText(Integer.toString(currentTitleLevel));

        // Reset level buttons
        incrementLevelButton.setDisable(currentLevel >= Consts.MAX_NUM_LEVEL_LIMIT);
        decrementLevelButton.setDisable(currentLevel <= 1);

        incrementLevelTitleButton.setDisable(currentTitleLevel >= Consts.MAX_NUM_TITLE_LEVEL_LIMIT);
        decrementLevelTitleButton.setDisable(currentTitleLevel <= 1);
    }

    @FXML
    private void help(ActionEvent event) throws IOException {
        Scene secondScene = new Scene(App.loadFXML("ayuda"), 450, 600);
        Stage newWindow = new Stage();
        newWindow.setTitle("Help");
        newWindow.setScene(secondScene);
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(App.getStage());
        newWindow.centerOnScreen();

        newWindow.show();
    }

    private boolean toggleMode(Button button, Modes mode) {
        boolean isActive = service.toggleMode(mode);
        AppUtils.changeButtonState(button, isActive);
        return isActive;
    }

    private boolean changeMode(Button button, Modes mode, boolean enable) {
        service.changeMode(mode, enable);
        AppUtils.changeButtonState(button, enable);
        return enable;
    }

    @FXML
    private void changeFileExtensionMode(ActionEvent event) {
        boolean isActive = toggleMode(showFileOrExtensionButton, Modes.FILE_EXTENSION);
        acumulativeButton.setDisable(!isActive);
        changeMode(acumulativeButton, Modes.ACUMULATIVE, false);
        update();
    }

    @FXML
    private void changeExecutableMode(ActionEvent event) {
        boolean isActive = toggleMode(executableButton, Modes.EXECUTABLE);

        showFileOrExtensionButton.setDisable(isActive);
        acumulativeButton.setDisable(true);
        changeMode(showFileOrExtensionButton, Modes.FILE_EXTENSION, false);
        changeMode(acumulativeButton, Modes.ACUMULATIVE, false);
        update();
    }

    @FXML
    private void changeAcumulativeMode(ActionEvent event) {
        toggleMode(acumulativeButton, Modes.ACUMULATIVE);
        update();
    }

    @FXML
    private void showFilenames(ActionEvent event) {
        boolean showFilenames = service.showFilenames();
        update();
        AppUtils.changeButtonState(showFilenamesButton, showFilenames);
    }

    private void updateLevel(Label field, int level) {
        int currentLevel = Integer.parseInt(field.getText());
        if (currentLevel == level) {
            return;
        }
        update();
        field.setText(Integer.toString(level));
    }

    private void handleLevelChange(
            Supplier<Integer> levelSupplier,
            Label label,
            Button incrementButton,
            Button decrementButton,
            int min,
            int max
    ) {
        int level = levelSupplier.get();
        updateLevel(label, level);

        incrementButton.setDisable(level >= max);
        decrementButton.setDisable(level <= min);
    }

    @FXML
    private void incrementLevel(ActionEvent event) {
        handleLevelChange(
                () -> service.incrementLevel(),
                levelLabel,
                incrementLevelButton,
                decrementLevelButton,
                1,
                Consts.MAX_NUM_LEVEL_LIMIT
        );
    }

    @FXML
    private void decrementLevel(ActionEvent event) {
        handleLevelChange(
                () -> service.decrementLevel(),
                levelLabel,
                incrementLevelButton,
                decrementLevelButton,
                1,
                Consts.MAX_NUM_LEVEL_LIMIT
        );
    }

    @FXML
    private void incrementLevelTitle(ActionEvent event) {
        handleLevelChange(
                () -> service.incrementLevelTitle(),
                titleLevelLabel,
                incrementLevelTitleButton,
                decrementLevelTitleButton,
                1,
                Consts.MAX_NUM_TITLE_LEVEL_LIMIT
        );
    }

    @FXML
    private void decrementLevelTitle(ActionEvent event) {
        handleLevelChange(
                () -> service.decrementLevelTitle(),
                titleLevelLabel,
                incrementLevelTitleButton,
                decrementLevelTitleButton,
                1,
                Consts.MAX_NUM_TITLE_LEVEL_LIMIT
        );
    }

    @FXML
    private void changeTheme(ActionEvent event) {
        ThemeStyle selected = themeComboBox.getValue();
        if (selected != null && selected != currentTheme) {
            currentTheme = selected;

            Preferences prefs = Preferences.userNodeForPackage(AppController.class);
            prefs.put("theme", currentTheme.name());

            Scene scene = themeComboBox.getScene();
            if (scene != null) {
                scene.getStylesheets().clear();
                scene.getStylesheets().add(currentTheme.getPath());
            }
        }
    }
}
