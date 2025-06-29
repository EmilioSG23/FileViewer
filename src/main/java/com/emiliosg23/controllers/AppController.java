package com.emiliosg23.controllers;

import java.io.Console;
import java.io.File;
import java.io.IOException;

import com.emiliosg23.models.AppService;
import com.emiliosg23.models.RenderConfiguration;
import com.emiliosg23.models.enums.Modes;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.models.tdas.trees.MultiTree;
import com.emiliosg23.utils.AppUtils;
import com.emiliosg23.view.PresentationNode;
import com.emiliosg23.view.TreeRender;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

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
	private Button updateButton;
	@FXML
	private Button helpButton;

	private AppService service;


	public void initialize(){
		this.service = new AppService();
		//treeMapPanel.prefHeightProperty().bind(background.heightProperty().subtract(192));
		AppUtils.changeButtonState(showFileOrExtensionButton, false);
		AppUtils.changeButtonState(acumulativeButton, false);
		AppUtils.changeButtonState(executableButton, false);
		AppUtils.changeButtonState(showFilenamesButton, true);

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
		//Reset panel
		treeMapPanel.getChildren().clear();
		//Reconfigurate panel size
		treeMapPanel.setPrefSize(treeMapPanel.getWidth(), treeMapPanel.getHeight());
		//Create directory tree
		MultiTree<Info> directoryTree = service.initDirectory(path);
		TreeRender render = new TreeRender(directoryTree, service.getRenderConfiguration());
		//Init directory tree presentation
		MultiTree<PresentationNode> presentationTree = render.initialize(treeMapPanel);
		render.render(presentationTree, treeMapPanel);
	}

	@FXML
	private void reset(ActionEvent event) {
			service.reset();
	}
	@FXML
	private void help(ActionEvent event) throws IOException {
		/* TODO: Incluir ayuda.fxml */
		/*Scene secondScene = new Scene(App.loadFXML("ayuda"), 450, 600);
		Stage newWindow = new Stage();
		newWindow.setTitle("Ayuda");
		newWindow.setScene(secondScene);
		newWindow.initModality(Modality.WINDOW_MODAL);
		newWindow.initOwner(App.getStage());
		newWindow.centerOnScreen();

		newWindow.show();*/
	}
	/* TODO Rerenderizar arbol */
	private boolean toggleMode(Button button, Modes mode) {
		boolean isActive = service.changeMode(mode);
		AppUtils.changeButtonState(button, isActive);
		return isActive;
	}

	@FXML
	private void changeFileExtensionMode(ActionEvent event) {
		boolean isActive = toggleMode(showFileOrExtensionButton, Modes.FILE_EXTENSION);
		acumulativeButton.setDisable(!isActive);
		if(acumulativeButton.isDisable())
			AppUtils.changeButtonState(acumulativeButton, false);
	}

	@FXML
	private void changeExecutableMode(ActionEvent event) {
		toggleMode(executableButton, Modes.EXECUTABLE);
	}

	@FXML
	private void changeAcumulativeMode(ActionEvent event) {
		toggleMode(acumulativeButton, Modes.ACUMULATIVE);
	}

	@FXML
	private void showFilenames(ActionEvent event) {
		boolean showFilenames = service.showFilenames();
		AppUtils.changeButtonState(showFilenamesButton, showFilenames);
	}

	private void updateLevel(Label field, int level){
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

}
