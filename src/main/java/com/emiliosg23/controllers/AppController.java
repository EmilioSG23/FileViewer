package com.emiliosg23.controllers;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

import com.emiliosg23.App;
import com.emiliosg23.application.AppService;
import com.emiliosg23.domain.model.Info;
import com.emiliosg23.domain.pipeline.AccumulatedTransformation;
import com.emiliosg23.domain.pipeline.FileExtensionTransformation;
import com.emiliosg23.infrastructure.preferences.ThemePreferences;
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
 * Adaptador FXML: lee eventos del usuario, delega toda la lógica a
 * {@link AppService} y actualiza los widgets. El escaneo de directorios se
 * ejecuta en un hilo de fondo mediante {@link Task}.</p>
 *
 * <p>
 * Las operaciones de dominio (transformaciones, interacción) se identifican con
 * las constantes estáticas de las clases de transformación, eliminando el uso
 * del antiguo enum {@code Modes}.</p>
 *
 * @see AppService
 * @see TreeRender
 * @see ThemePreferences
 */
public class AppController {

    // -------------------------------------------------------------------------
    // FXML fields
    // -------------------------------------------------------------------------
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

    // -------------------------------------------------------------------------
    // Instance state
    // -------------------------------------------------------------------------
    private final ThemePreferences themePreferences = new ThemePreferences(AppController.class);
    private ThemeStyle currentTheme;

    private AppService service;
    private TreeRender render;

    // -------------------------------------------------------------------------
    // Initialization
    // -------------------------------------------------------------------------
    public void initialize() {
        // --- Tema ---
        currentTheme = themePreferences.load();
        themeComboBox.getItems().setAll(ThemeStyle.values());
        themeComboBox.setValue(currentTheme);
        applyTheme(currentTheme);

        // --- Clip del panel de treemap ---
        Rectangle clip = new Rectangle();
        treeMapPanel.setClip(clip);
        treeMapPanel.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            clip.setWidth(newVal.getWidth());
            clip.setHeight(newVal.getHeight());
        });

        // --- Servicio y render ---
        this.service = new AppService();
        this.render = new TreeRender();

        // --- Estado inicial de controles ---
        AppUtils.changeButtonState(showFileOrExtensionButton,
                service.isTransformationActive(FileExtensionTransformation.ID));
        AppUtils.changeButtonState(acumulativeButton,
                service.isTransformationActive(AccumulatedTransformation.ID));
        AppUtils.changeButtonState(executableButton, service.isExecutableMode());
        AppUtils.changeButtonState(showFilenamesButton,
                service.getRenderConfiguration().isShowFilenames());

        acumulativeButton.setDisable(
                !service.isTransformationActive(FileExtensionTransformation.ID));

        directoryTextField.setText(service.getDirectory());
        levelLabel.setText(Integer.toString(service.getRenderConfiguration().getLimitLevel()));
        titleLevelLabel.setText(Integer.toString(service.getRenderConfiguration().getLimitLevelTitle()));
    }

    // -------------------------------------------------------------------------
    // Selección de directorio
    // -------------------------------------------------------------------------
    @FXML
    private void selectDirectory(ActionEvent event) {
        DirectoryChooser selector = new DirectoryChooser();
        selector.setTitle("Select a directory");

        String path = directoryTextField.getText();
        File initialDir = new File(path);
        if (initialDir.exists() && initialDir.isDirectory()) {
            selector.setInitialDirectory(initialDir);
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
        treeMapPanel.setPrefSize(treeMapPanel.getWidth(), treeMapPanel.getHeight());

        Alert generating = AppUtils.showLoadingAlert("Generating tree...");
        setControlsDisabled(true);

        Task<MultiTree<Info>> scanTask = new Task<>() {
            @Override
            protected MultiTree<Info> call() {
                return service.initDirectory(path);
            }
        };

        scanTask.setOnSucceeded(event -> {
            generating.close();
            processTreeGenerator(scanTask.getValue(), treeMapPanel);
            setControlsDisabled(false);
        });

        scanTask.setOnFailed(event -> {
            generating.close();
            setControlsDisabled(false);
            AppUtils.showErrorAlert("Error scanning directory: "
                    + scanTask.getException().getMessage());
        });

        Thread backgroundThread = new Thread(scanTask);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    // -------------------------------------------------------------------------
    // Reset
    // -------------------------------------------------------------------------
    @FXML
    private void reset(ActionEvent event) {
        service.reset();
        treeMapPanel.getChildren().clear();

        AppUtils.changeButtonState(showFileOrExtensionButton, false);
        AppUtils.changeButtonState(acumulativeButton, false);
        AppUtils.changeButtonState(executableButton, false);
        AppUtils.changeButtonState(showFilenamesButton,
                service.getRenderConfiguration().isShowFilenames());

        showFileOrExtensionButton.setDisable(false);
        acumulativeButton.setDisable(true);
        showFilenamesButton.setDisable(false);
        executableButton.setDisable(false);

        directoryTextField.setText(service.getDirectory());

        int level = service.getRenderConfiguration().getLimitLevel();
        int titleLevel = service.getRenderConfiguration().getLimitLevelTitle();
        levelLabel.setText(Integer.toString(level));
        titleLevelLabel.setText(Integer.toString(titleLevel));

        incrementLevelButton.setDisable(level >= Consts.MAX_NUM_LEVEL_LIMIT);
        decrementLevelButton.setDisable(level <= 1);
        incrementLevelTitleButton.setDisable(titleLevel >= Consts.MAX_NUM_TITLE_LEVEL_LIMIT);
        decrementLevelTitleButton.setDisable(titleLevel <= 1);
    }

    // -------------------------------------------------------------------------
    // Modos de transformación del árbol
    // -------------------------------------------------------------------------
    @FXML
    private void changeFileExtensionMode(ActionEvent event) {
        boolean isActive = service.toggleTransformation(FileExtensionTransformation.ID);
        AppUtils.changeButtonState(showFileOrExtensionButton, isActive);
        if (!isActive) {
            // Desactivar FILE_EXTENSION desactiva ACCUMULATED en cascada (pipeline)
            AppUtils.changeButtonState(acumulativeButton, false);
        }
        acumulativeButton.setDisable(!isActive);
        update();
    }

    @FXML
    private void changeAcumulativeMode(ActionEvent event) {
        boolean isActive = service.toggleTransformation(AccumulatedTransformation.ID);
        AppUtils.changeButtonState(acumulativeButton, isActive);
        update();
    }

    // -------------------------------------------------------------------------
    // Modo de interacción (ejecutable)
    // -------------------------------------------------------------------------
    @FXML
    private void changeExecutableMode(ActionEvent event) {
        boolean isActive = service.toggleExecutableMode();
        AppUtils.changeButtonState(executableButton, isActive);

        if (isActive) {
            // Modo ejecutable: las transformaciones de árbol no aplican
            service.disableTransformation(FileExtensionTransformation.ID);
            AppUtils.changeButtonState(showFileOrExtensionButton, false);
            AppUtils.changeButtonState(acumulativeButton, false);
            showFileOrExtensionButton.setDisable(true);
            acumulativeButton.setDisable(true);
        } else {
            showFileOrExtensionButton.setDisable(false);
            acumulativeButton.setDisable(
                    !service.isTransformationActive(FileExtensionTransformation.ID));
        }
        update();
    }

    // -------------------------------------------------------------------------
    // Opciones de render
    // -------------------------------------------------------------------------
    @FXML
    private void showFilenames(ActionEvent event) {
        boolean showFilenames = service.showFilenames();
        AppUtils.changeButtonState(showFilenamesButton, showFilenames);
        update();
    }

    @FXML
    private void incrementLevel(ActionEvent event) {
        handleLevelChange(() -> service.incrementLevel(),
                levelLabel, incrementLevelButton, decrementLevelButton,
                1, Consts.MAX_NUM_LEVEL_LIMIT);
    }

    @FXML
    private void decrementLevel(ActionEvent event) {
        handleLevelChange(() -> service.decrementLevel(),
                levelLabel, incrementLevelButton, decrementLevelButton,
                1, Consts.MAX_NUM_LEVEL_LIMIT);
    }

    @FXML
    private void incrementLevelTitle(ActionEvent event) {
        handleLevelChange(() -> service.incrementLevelTitle(),
                titleLevelLabel, incrementLevelTitleButton, decrementLevelTitleButton,
                1, Consts.MAX_NUM_TITLE_LEVEL_LIMIT);
    }

    @FXML
    private void decrementLevelTitle(ActionEvent event) {
        handleLevelChange(() -> service.decrementLevelTitle(),
                titleLevelLabel, incrementLevelTitleButton, decrementLevelTitleButton,
                1, Consts.MAX_NUM_TITLE_LEVEL_LIMIT);
    }

    private void handleLevelChange(Supplier<Integer> levelSupplier, Label label,
            Button incrementBtn, Button decrementBtn,
            int min, int max) {
        int level = levelSupplier.get();
        int current = Integer.parseInt(label.getText());
        if (current != level) {
            update();
            label.setText(Integer.toString(level));
        }
        incrementBtn.setDisable(level >= max);
        decrementBtn.setDisable(level <= min);
    }

    // -------------------------------------------------------------------------
    // Tema
    // -------------------------------------------------------------------------
    @FXML
    private void changeTheme(ActionEvent event) {
        ThemeStyle selected = themeComboBox.getValue();
        if (selected != null && selected != currentTheme) {
            currentTheme = selected;
            themePreferences.save(currentTheme);
            applyTheme(currentTheme);
        }
    }

    private void applyTheme(ThemeStyle theme) {
        Scene scene = themeComboBox != null ? themeComboBox.getScene() : null;
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(theme.getPath());
        } else if (themeComboBox != null) {
            themeComboBox.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.getStylesheets().clear();
                    newScene.getStylesheets().add(theme.getPath());
                }
            });
        }
    }

    // -------------------------------------------------------------------------
    // Ayuda
    // -------------------------------------------------------------------------
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

    // -------------------------------------------------------------------------
    // Helpers internos
    // -------------------------------------------------------------------------
    private void update() {
        MultiTree<Info> updatedTree = service.update();
        if (updatedTree != null) {
            processTreeGenerator(updatedTree, treeMapPanel);
        }
    }

    private void processTreeGenerator(MultiTree<Info> tree, HBox panel) {
        render.setTree(tree);
        render.setConfig(service.getRenderConfiguration());
        render.setInteractionOptions(service.getInteractionOptions());
        MultiTree<PresentationNode> presentationTree = render.initialize(panel);
        render.render(presentationTree, panel);
    }

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
}
