package com.emiliosg23.view;

import com.emiliosg23.models.RenderConfiguration;
import com.emiliosg23.models.infos.DirectoryInfo;
import com.emiliosg23.models.infos.ExtensionInfo;
import com.emiliosg23.models.infos.FileInfo;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.models.tdas.trees.MultiTree;

import javafx.scene.layout.Pane;

public class TreeRender {
	private MultiTree<Info> tree;
	private RenderConfiguration config;

	public TreeRender(MultiTree<Info> tree, RenderConfiguration config){
		this.tree = tree;
		this.config = config;
	}

	public TreeRender(){}

	public void setTree(MultiTree<Info> tree) {this.tree = tree;}
	public void setConfig(RenderConfiguration config) {this.config = config;}

	public MultiTree<PresentationNode> initialize(Pane paneRoot){
		paneRoot.getChildren().clear();
		long totalSize = tree.getRoot().getContent().getSize();
		return initializeRecursive(tree, paneRoot, totalSize, config);
	}

	public void render(MultiTree<PresentationNode> presentationTree, Pane paneRoot){
		PresentationNode presentationNode = presentationTree.getRoot().getContent();
		paneRoot.getChildren().add(presentationNode.getTreePane());
		
		if (presentationNode instanceof DirectoryPresentationNode){
			DirectoryPresentationNode directoryNode = (DirectoryPresentationNode) presentationNode;
			Pane childTreePane = directoryNode.getChildTreePane();

			
			for(MultiTree<PresentationNode> subtree: presentationTree.getRoot().getChildren())
				render(subtree, childTreePane);
		}
	}
	
	private MultiTree<PresentationNode> initializeRecursive(MultiTree<Info> tree, Pane paneRoot, long sizeParent, RenderConfiguration config) {
		if (config.getLimitLevel() < 0)
			return null;

		Info info = tree.getRoot().getContent();
		PresentationNode presentationInfoNode = createPresentationNode(info, config);
		presentationInfoNode.initializeNode(sizeParent, paneRoot, config.isVerticalStart());
		presentationInfoNode.createNode(true);

		MultiTree<PresentationNode> presentationTree = new MultiTree<>(presentationInfoNode);

		if (info instanceof DirectoryInfo && config.getLimitLevel() > 0) {
			DirectoryInfo directoryInfo = (DirectoryInfo) info;
			DirectoryPresentationNode directoryNode = (DirectoryPresentationNode) presentationInfoNode;
			Pane childTreePane = directoryNode.getChildTreePane();
			RenderConfiguration childConfig = config.createChildConfiguration();

			for (MultiTree<Info> subtree : tree.getRoot().getChildren()) {
				MultiTree<PresentationNode> childTree = initializeRecursive(subtree, childTreePane, directoryInfo.getSize(), childConfig);
				if (childTree != null) {
					presentationTree.addChild(childTree);
				}
			}
		}
		return presentationTree;
	}

	//Instance Info with it Presentation Node
	private PresentationNode createPresentationNode(Info info, RenderConfiguration config) {
		if (info instanceof DirectoryInfo)
			return new DirectoryPresentationNode((DirectoryInfo) info, config.getLimitLevelTitle() > 0, config.getLimitLevel() == 0);
		else if (info instanceof FileInfo)
			return new FilePresentationNode((FileInfo) info, config.isShowFilenames(), config.isExecutableMode());
		else if (info instanceof ExtensionInfo)
			return new ExtensionPresentationNode((ExtensionInfo) info);
		else
			throw new IllegalArgumentException("Info must be either DirectoryInfo or FileInfo");
	}
}
