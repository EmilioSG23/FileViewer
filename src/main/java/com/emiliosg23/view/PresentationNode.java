package com.emiliosg23.view;

import com.emiliosg23.models.infos.Info;
import com.emiliosg23.utils.AppUtils;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public abstract class PresentationNode {
	private final Info info;
	private VBox treePane;
	private boolean vertical;
	
	public PresentationNode(Info info){
		this.info = info;
		this.treePane = new VBox();
		this.vertical = false;
	}

	public VBox getTreePane() {return treePane;}
	public void setTreePane(VBox treePane){this.treePane = treePane;}
	public Info getInfo(){return this.info;}
	public boolean isVertical(){return this.vertical;}
	
	public void initializeNode(long sizeParent, Pane parentPane, boolean vertical) {
		this.vertical = vertical;
		double percent = (double) info.getSize() / sizeParent;
		double width, height;

		if(!vertical){
			width = parentPane.getPrefWidth();
			height = parentPane.getPrefHeight() * percent;
		}
		else{
			width = parentPane.getPrefWidth() * percent;
			height = parentPane.getPrefHeight();
		}

		AppUtils.setWidth(treePane, width);
		AppUtils.setHeight(treePane, height);
	}

	public abstract VBox createNode(boolean showSize); 
}
