package com.emiliosg23.models;

import com.emiliosg23.models.enums.Modes;
import com.emiliosg23.models.tdas.trees.MultiTree;

public class AppService {
	private final AppLogic app;

	public AppService(){
		this.app = new AppLogic();
	}

	public MultiTree initDirectory(String directory) {
		/*TODO */
	}

	public MultiTree initDirectory(){
		/* TODO */
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
		return app.showFilenames();
	}

	public int incrementLevel() {
		return app.addLimitLevel();
	}

	public int decrementLevel() {
		return app.substractLimitLevel();
	}

	public int incrementLevelTitle() {
		return app.addLimitTitleLevel();
	}

	public int decrementLevelTitle() {
		return app.substractLimitTitleLevel();
	}
}
