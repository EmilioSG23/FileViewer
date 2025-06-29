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
	
	public void update() {
		app.update();
	}

	public boolean changeMode(Modes mode){
		return app.changeMode(mode);
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
}
