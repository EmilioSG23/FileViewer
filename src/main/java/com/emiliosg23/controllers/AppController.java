package com.emiliosg23.controllers;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;
import com.emiliosg23.App;
import com.emiliosg23.models.AppService;
import com.emiliosg23.models.enums.Modes;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.models.tdas.trees.MultiTree;
import com.emiliosg23.utils.AppUtils;
import com.emiliosg23.view.PresentationNode;
import com.emiliosg23.view.ThemeStyle;
import com.emiliosg23.view.TreeRender;

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

public class AppController{
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
	private ComboBox<ThemeStyle> themeComboBox;

	private AppService service;
	private TreeRender render;
	private ThemeStyle currentTheme = ThemeStyle.LIGHT;


	public void initialize(){
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
		if(selectedDirectory!=null){
			directoryTextField.setText(selectedDirectory.getAbsolutePath());
			initDirectory(selectedDirectory.getAbsolutePath());
		}
		else
				AppUtils.showErrorAlert("There is not selected directory.");
	}

	private void initDirectory(String path){
		if(path.equals("")){
				AppUtils.showErrorAlert("There is not selected directory.");
				return;
		}
		//Reconfigurate panel size
		treeMapPanel.setPrefSize(treeMapPanel.getWidth(), treeMapPanel.getHeight());
		//Create directory tree
		Alert generating = AppUtils.showLoadingAlert("Generating tree...");
		MultiTree<Info> directoryTree = service.initDirectory(path);
		generating.close();
		processTreeGenerator(directoryTree, treeMapPanel);
	}
	
	private void processTreeGenerator(MultiTree<Info> tree, HBox panel){
		Alert initializing = AppUtils.showLoadingAlert("Initializing tree...");
		//Config Render Engine
		render.setTree(tree);
		render.setConfig(service.getRenderConfiguration());
		//Init directory tree presentation
		MultiTree<PresentationNode> presentationTree = render.initialize(panel);
		initializing.close();
		//Render directory tree
		Alert rendering = AppUtils.showLoadingAlert("Rendering tree...");
		render.render(presentationTree, panel);
		rendering.close();
	}

	private void update(){
		MultiTree<Info> updatedDirectoryTree = service.update();
		if (updatedDirectoryTree != null)
			processTreeGenerator(updatedDirectoryTree, treeMapPanel);
	}

	@FXML
	private void reset(ActionEvent event) {
		service.reset();
		treeMapPanel.getChildren().clear();

		AppUtils.changeButtonState(showFileOrExtensionButton, service.getPanelConfiguration().isFileExtensionMode());
		AppUtils.changeButtonState(acumulativeButton, service.getPanelConfiguration().isAcumulativeMode());
		AppUtils.changeButtonState(executableButton, service.getPanelConfiguration().isExecutableMode());
		AppUtils.changeButtonState(showFilenamesButton, service.getRenderConfiguration().isShowFilenames());

		acumulativeButton.setDisable(true);
		executableButton.setDisable(true);

		directoryTextField.setText(service.getPanelConfiguration().getDirectory());
		levelLabel.setText(Integer.toString(service.getRenderConfiguration().getLimitLevel()));
		titleLevelLabel.setText(Integer.toString(service.getRenderConfiguration().getLimitLevelTitle()));
	}
	@FXML
	private void help(ActionEvent event) throws IOException {
		Scene secondScene = new Scene(App.loadFXML("ayuda"), 450, 600);
		Stage newWindow = new Stage();
		newWindow.setTitle("Ayuda");
		newWindow.setScene(secondScene);
		newWindow.initModality(Modality.WINDOW_MODAL);
		newWindow.initOwner(App.getStage());
		newWindow.centerOnScreen();

		newWindow.show();
	}

	private boolean toggleMode(Button button, Modes mode) {
		boolean isActive = service.toogleMode(mode);
		AppUtils.changeButtonState(button, isActive);
		return isActive;
	}

	private boolean changeMode(Button button, Modes mode, boolean enable){
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

	private void updateLevel(Label field, int level){
		update();
		field.setText(Integer.toString(level));
	}
	@FXML
	private void incrementLevel(ActionEvent event) {
		int level = service.incrementLevel();
		updateLevel(levelLabel, level);
	}
	@FXML
	private void decrementLevel(ActionEvent event) {
		int level = service.decrementLevel();
		updateLevel(levelLabel, level);
	}
	@FXML
	private void incrementLevelTitle(ActionEvent event) {
		int level = service.incrementLevelTitle();
		updateLevel(titleLevelLabel, level);
	}
	@FXML
	private void decrementLevelTitle(ActionEvent event) {
		int level = service.decrementLevelTitle();
		updateLevel(titleLevelLabel, level);
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
