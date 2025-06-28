package com.emiliosg23.view;

import com.emiliosg23.models.infos.FileInfo;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class FilePresentationNode extends PresentationNode{
	public FilePresentationNode(FileInfo info, boolean showFilename){
		super(info, showFilename);
	}

	@Override
	public VBox createNode(boolean vertical, boolean showSize) {
		final int MIN_PREF_SIZE = 10;
		double prefWidth = getTreeNode().getPrefWidth();
		double prefHeight = getTreeNode().getHeight();

		VBox node=new VBox();
		node.setAlignment(Pos.TOP_CENTER);
		
		if(getTitle()!=null)
			getTitle().setPrefWidth(prefWidth);

		if (showFilename() && prefWidth >= MIN_PREF_SIZE && prefHeight >= MIN_PREF_SIZE){
			Font font=Font.font("Consolas",9);
			Label filename;
			if(showSize)
					filename =new Label(getInfo().toString());
					else
					filename=new Label(getInfo().getName());

			filename.setFont(font);
			getTreeNode().getChildren().add(filename);
		}

		node.setPrefWidth(prefWidth);
		node.setPrefHeight(prefHeight);
		node.getChildren().add(getTreeNode());
		return node;
	}
}
