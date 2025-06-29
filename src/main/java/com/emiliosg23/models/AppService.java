package com.emiliosg23.models;

import com.emiliosg23.models.enums.Modes;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.models.tdas.trees.MultiTree;

public class AppService {
	private final AppLogic app;

	public AppService(){
		this.app = new AppLogic();
	}

	public MultiTree<Info> initDirectory(String directory) {
		this.app.changeDirectory(directory);
		this.app.createTreeDirectory();
		return this.app.transformTreeDirectory();
	}

	public MultiTree<Info> initDirectory(){
		this.app.createTreeDirectory();
		return this.app.transformTreeDirectory();
	}

	public void reset() {
		app.reset();
	}
	
	public MultiTree<Info> update() {
		return this.app.transformTreeDirectory();
	}

	public boolean toogleMode(Modes mode){
		return app.toogleMode(mode);
	}

	public boolean changeMode(Modes mode, boolean enable){
		return app.changeMode(mode, enable);
	}

	public boolean showFilenames() {
		return app.toggleShowFilenames();
	}

	public int incrementLevel() {
		return app.increaseLimitLevel();
	}

	public int decrementLevel() {
		return app.decreaseLimitLevel();
	}

	public int incrementLevelTitle() {
		return app.increaseLimitTitleLevel();
	}

	public int decrementLevelTitle() {
		return app.decreaseLimitTitleLevel();
	}

	public RenderConfiguration getRenderConfiguration(){
		return app.getRenderConfiguration();
	}

	public PanelConfiguration getPanelConfiguration(){
		return app.getPanelConfiguration();
	}
}
