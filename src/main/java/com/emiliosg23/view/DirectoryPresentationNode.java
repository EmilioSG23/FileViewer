package com.emiliosg23.view;

import com.emiliosg23.models.infos.DirectoryInfo;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class DirectoryPresentationNode extends PresentationNode{
	public DirectoryPresentationNode(DirectoryInfo info, boolean showFilename){
		super(info, showFilename);
	}

	@Override
	public VBox createNode(boolean vertical, boolean showSize) {
		final int MIN_PREF_SIZE = 30;
		double prefWidth = getTreeNode().getPrefWidth();
		double prefHeight = getTreeNode().getHeight();

		VBox node=new VBox();
		node.setAlignment(Pos.TOP_CENTER);
		
		if(getTitle()!=null){
			getTitle().setPrefWidth(prefWidth);
			if(prefWidth >= MIN_PREF_SIZE)
			node.getChildren().add(getTitle());
		}

		node.setPrefWidth(prefWidth);
		node.setPrefHeight(prefHeight);
		node.getChildren().add(getTreeNode());
		return node;
	}
}
