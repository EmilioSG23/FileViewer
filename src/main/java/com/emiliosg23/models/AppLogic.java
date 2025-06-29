package com.emiliosg23.models;

import com.emiliosg23.logic.TreeInfoGenerator;
import com.emiliosg23.models.enums.Modes;
import com.emiliosg23.models.infos.FileInfo;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.models.tdas.trees.MultiTree;

import javafx.scene.layout.Pane;

public class AppLogic {
	private final PanelConfiguration panelConfiguration;
	private final TreeInfoGenerator treeGenerator;

	private MultiTree<Info> directoryTree;

	public AppLogic() {
		this.panelConfiguration = new PanelConfiguration();
		this.treeGenerator = new TreeInfoGenerator();
	}

	public void changeDirectory(String directory) {
		this.panelConfiguration.setDirectory(directory);
	}

	public MultiTree<Info> createTreeDirectory() {
		String directory = panelConfiguration.getDirectory();
		int depthLimit = panelConfiguration.getRenderConfiguration().getLimitLevel();
		this.directoryTree = treeGenerator.createTree(directory, depthLimit);
		return this.directoryTree;
	}

	public MultiTree<Info> transformTreeDirectory() {
		MultiTree<Info> tree = this.directoryTree;

		if (panelConfiguration.isFileExtensionMode())
			tree = treeGenerator.transformTree(tree, Modes.FILE_EXTENSION);

		if (panelConfiguration.isFileExtensionMode() && panelConfiguration.isAcumulativeMode())
			tree = treeGenerator.transformTree(tree, Modes.ACUMULATIVE);

		return tree;
	}

	public boolean changeMode(Modes mode) {
		return this.panelConfiguration.changeMode(mode);
	}

	// Delega a RenderConfiguration
	public boolean toggleShowFilenames() {
		return this.panelConfiguration.getRenderConfiguration().toggleShowFilenames();
	}

	public int increaseLimitLevel() {
		return this.panelConfiguration.getRenderConfiguration().increaseLimitLevel();
	}

	public int decreaseLimitLevel() {
		return this.panelConfiguration.getRenderConfiguration().decreaseLimitLevel();
	}

	public int increaseLimitTitleLevel() {
		return this.panelConfiguration.getRenderConfiguration().increaseTitleLimitLevel();
	}

	public int decreaseLimitTitleLevel() {
		return this.panelConfiguration.getRenderConfiguration().decreaseTitleLimitLevel();
	}

	public void reset() {
		this.panelConfiguration.reset();
	}

	// Acceso a la configuración si hace falta en otras partes
	public RenderConfiguration getRenderConfiguration() {
		return this.panelConfiguration.getRenderConfiguration();
	}

	public PanelConfiguration getPanelConfiguration() {
		return this.panelConfiguration;
	}

	public void update(){

	}
    
    private void initializeTreeMaps(MultiTree<FileInfo> directoryTree,Pane paneRoot,long sizeParent,int limitLevelTitle,int limitLevel,boolean vertical){
        /*MultiTreeNode<FileInfo> root=directoryTree.getRoot();
        if(limitLevelTitle>0)
            root.getContent().initializePresentationNode(sizeParent, paneRoot, true,vertical);
        else if(limitLevel>0)
            root.getContent().initializePresentationNode(sizeParent, paneRoot, false,vertical);
        for(MultiTree<FileInfo> child:directoryTree.getRoot().getChildren())
                initializeTreeMaps(child, root.getContent().getPresentationNode().getTreeMap(), root.getContent().getSize(), limitLevelTitle-1, limitLevel-1,!vertical);
    */}
    private void fillPaneTreeMap(Pane pane,MultiTree<FileInfo> directoryTree, boolean resetPane,boolean showFilenames){
        /*if(resetPane)
            resetPane(pane);
        MultiTreeNode<FileInfo> root=directoryTree.getRoot();
        if(!root.getChildren().isEmpty())
            for(MultiTree<FileInfo> child:root.getChildren())
                fillPaneTreeMap(root.getContent().getPresentationNode().getTreeMap(),child,false,showFilenames);
        pane.getChildren().add(directoryTree.getRoot().getContent().createPresentationNode(showFilenames));*/
    }
}
