package com.emiliosg23.view;

import com.emiliosg23.models.infos.Info;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public abstract class PresentationNode {
	private final Info info;
	//No es lo ideal dejar el showFilename aquí
	private final boolean showFilename;

	private Pane treeNode;
	private VBox title;

	public PresentationNode(Info info, boolean showFilename){
		this.info = info;
		this.showFilename = showFilename;
	}

	public Info getInfo(){
		return this.info;
	}
	public boolean showFilename(){
		return this.showFilename;
	}


	public VBox getTitle() {
		return title;
	}

	public Pane getTreeNode() {
		return treeNode;
	}

	public void createTitle(){
		title=new VBox();
		Label label = new Label(info.toString());
		
		title.getStyleClass().add("title-box");
    label.getStyleClass().add("title-label");

    title.getChildren().add(label);
	}

	public void createTreeNode(double width, double height, String color){
		final int MAX_SIZE = 25;
		treeNode.setPrefSize(width, height);
		treeNode.setMinSize(width, height);
		treeNode.setMaxSize(width, height);
		treeNode.setStyle("-fx-background-color:"+color);
		
		if(width >= MAX_SIZE && height >= MAX_SIZE)
			treeNode.getStyleClass().add("tree-node");
	}
	
	public abstract VBox createNode(boolean vertical, boolean showSize); 
}
