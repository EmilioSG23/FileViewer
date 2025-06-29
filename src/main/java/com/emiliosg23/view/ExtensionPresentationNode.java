package com.emiliosg23.view;

import com.emiliosg23.models.infos.ExtensionInfo;
import com.emiliosg23.utils.FileExtensionUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ExtensionPresentationNode extends PresentationNode{
	public ExtensionPresentationNode(ExtensionInfo info){
		super(info);
	}

	@Override
	public VBox createNode(boolean showSize) {
		final int MIN_SIZE = 10;
		VBox rootPane = getTreePane();
		rootPane.setAlignment(Pos.CENTER);

		if (rootPane.getPrefWidth() > MIN_SIZE && rootPane.getPrefHeight() > MIN_SIZE){
			Label extensionLabel = new Label(showSize ? getInfo().toString() : getInfo().getName());
			extensionLabel.getStyleClass().add("title-file");
			rootPane.getChildren().add(extensionLabel);
		}

		rootPane.setStyle("-fx-background-color:"+FileExtensionUtils.getColor(getInfo().getName())+";");
		
		setTreePane(rootPane);
		return rootPane;
	}
}
