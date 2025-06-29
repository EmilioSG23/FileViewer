package com.emiliosg23.models;

import com.emiliosg23.logic.TreeInfoGenerator;
import com.emiliosg23.models.enums.Modes;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.models.tdas.trees.MultiTree;

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
		this.directoryTree = treeGenerator.createTree(directory);
		return this.directoryTree;
	}

	public MultiTree<Info> transformTreeDirectory() {
		if (directoryTree == null) return null;
		MultiTree<Info> tree = treeGenerator.copyTree(directoryTree);

		if (panelConfiguration.isFileExtensionMode())
			tree = treeGenerator.transformTree(tree, Modes.FILE_EXTENSION);

		if (panelConfiguration.isFileExtensionMode() && panelConfiguration.isAcumulativeMode())
			tree = treeGenerator.transformTree(tree, Modes.ACUMULATIVE);

		return tree;
	}

	public boolean toogleMode(Modes mode) {
		return this.panelConfiguration.toogleMode(mode);
	}

	public boolean changeMode(Modes mode, boolean enable){
		return this.panelConfiguration.changeMode(mode, enable);
	}

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
		this.directoryTree = null;
	}

	// Acceso a la configuración si hace falta en otras partes
	public RenderConfiguration getRenderConfiguration() {
		return this.panelConfiguration.getRenderConfiguration();
	}

	public PanelConfiguration getPanelConfiguration() {
		return this.panelConfiguration;
	}
}
