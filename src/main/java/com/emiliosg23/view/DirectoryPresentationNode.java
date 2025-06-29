package com.emiliosg23.view;

import com.emiliosg23.models.infos.DirectoryInfo;
import com.emiliosg23.utils.AppUtils;
import com.emiliosg23.utils.FileExtensionUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DirectoryPresentationNode extends PresentationNode{
	private VBox title;
	private Pane childTreePane;
	private final boolean showTitle;
	private final boolean lastLevel;

	public DirectoryPresentationNode(DirectoryInfo info, boolean showTitle, boolean lastLevel){
		super(info);
		this.showTitle = showTitle;
		this.lastLevel = lastLevel;
	}

	public VBox getTitle() {return title;}
	public Pane getChildTreePane() {return childTreePane;}

	public void createTitle(){
		title=new VBox();
		Label label = new Label(getInfo().toString());
		
		title.getStyleClass().add("title-box");
    label.getStyleClass().add("title-label");

    title.getChildren().add(label);
	}

	@Override
	public VBox createNode(boolean showSize) {
		final int MIN_SIZE = 30;
    VBox rootPane = getTreePane();

		rootPane.setAlignment(Pos.CENTER);

		if (lastLevel){
			if (rootPane.getPrefWidth() > 10 && rootPane.getPrefHeight() > 10){
				Label filenameLabel = new Label(getInfo().getName());
				filenameLabel.getStyleClass().add("title-file");
				rootPane.getChildren().add(filenameLabel);
			}
			rootPane.setStyle("-fx-background-color:"+FileExtensionUtils.getColor("")+";");
			
			setTreePane(rootPane);
			return rootPane;
		}

		//Insert directory title
		if(showTitle && rootPane.getPrefWidth() > MIN_SIZE){
			createTitle();
			rootPane.getChildren().add(title);
		}
		//Create child pane depends orientation
		if (isVertical())
			childTreePane = new VBox();
		else
			childTreePane = new HBox();
		
		if (title != null)
			AppUtils.setHeight(childTreePane, rootPane.getPrefHeight() - 10);
		else
			AppUtils.setHeight(childTreePane, rootPane.getPrefHeight());
		
		AppUtils.setWidth(childTreePane, rootPane.getPrefWidth());
		
		rootPane.getChildren().add(childTreePane);
		setTreePane(rootPane);
		return rootPane;
	}
}
