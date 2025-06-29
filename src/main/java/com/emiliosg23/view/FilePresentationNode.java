package com.emiliosg23.view;

import com.emiliosg23.models.infos.FileInfo;
import com.emiliosg23.utils.FileExtensionUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FilePresentationNode extends PresentationNode{
	private final boolean showFilename;
	private final boolean executableMode;

	public FilePresentationNode(FileInfo info, boolean showFilename, boolean executableMode){
		super(info);
		this.showFilename = showFilename;
		this.executableMode = executableMode;
	}

	public boolean showFilename(){return this.showFilename;}
	public boolean isExecutableMode(){return this.executableMode;}

	@Override
	public VBox createNode(boolean showSize) {
		final int MIN_SIZE = 10;
		VBox rootPane = getTreePane();
		rootPane.setAlignment(Pos.CENTER);

		if (rootPane.getPrefWidth() > MIN_SIZE && rootPane.getPrefHeight() > MIN_SIZE){
			Label filenameLabel = new Label(showFilename ? getInfo().getName() : "");
			filenameLabel.getStyleClass().add("title-file");
			rootPane.getChildren().add(filenameLabel);
		}

		String extension = ((FileInfo) getInfo()).getExtension();
		rootPane.setStyle("-fx-background-color:"+FileExtensionUtils.getColor(extension)+";");
		
		setTreePane(rootPane);
		return rootPane;
	}
}
