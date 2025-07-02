package com.emiliosg23.models;

import com.emiliosg23.utils.Consts;

public class RenderConfiguration {
	private final int MAX_NUM_LEVEL_LIMIT = Consts.MAX_NUM_LEVEL_LIMIT;
	private final int MAX_NUM_TITLE_LEVEL_LIMIT = Consts.MAX_NUM_TITLE_LEVEL_LIMIT;

	private int limitLevel = Consts.DEFAULT_LIMIT_LEVEL;
	private int limitLevelTitle = Consts.DEFAULT_LIMIT_LEVEL_TITLE;
	private boolean showFilenames = Consts.DEFAULT_SHOW_FILENAMES;
	private boolean verticalStart = Consts.DEFAULT_VERTICAL_START;
	private boolean executableMode = Consts.DEFAULT_EXECUTABLE_MODE;

	public RenderConfiguration() {}
	public RenderConfiguration(int limitLevel, int limitLevelTitle, boolean showFilenames, boolean verticalStart, boolean executableMode){
		this.limitLevel = limitLevel;
		this.limitLevelTitle = limitLevelTitle;
		this.showFilenames = showFilenames;
		this.verticalStart = verticalStart;
		this.executableMode = executableMode;
	}

	// GETTERS
	public int getLimitLevel() {return limitLevel;}
	public int getLimitLevelTitle() {return limitLevelTitle;}
	public boolean isShowFilenames() {return showFilenames;}
	public boolean isVerticalStart() {return verticalStart;}
	public boolean isExecutableMode() {return executableMode;}

	// TOGGLE
	public boolean toggleShowFilenames() {
		showFilenames = !showFilenames;
		return showFilenames;
	}

	public boolean toggleOrientation() {
		verticalStart = !verticalStart;
		return verticalStart;
	}

	public boolean toogleExecutableMode(){
		executableMode = !executableMode;
		return executableMode;
	}

	public boolean changeExecutableMode(boolean enable){
		executableMode = enable;
		return executableMode;
	}

	// LIMIT LEVEL ADJUSTMENT
	public int increaseLimitLevel() {
		if (limitLevel < MAX_NUM_LEVEL_LIMIT)
			limitLevel++;
		return limitLevel;
	}

	public int decreaseLimitLevel() {
		if (limitLevel > 1)
			limitLevel--;
		return limitLevel;
	}

	public int increaseTitleLimitLevel() {
		if (limitLevelTitle < MAX_NUM_TITLE_LEVEL_LIMIT)
			limitLevelTitle++;
		return limitLevelTitle;
	}

	public int decreaseTitleLimitLevel() {
		if (limitLevelTitle > 1)
			limitLevelTitle--;
		return limitLevelTitle;
	}

	public void reset() {
		limitLevel = Consts.DEFAULT_LIMIT_LEVEL;
		limitLevelTitle = Consts.DEFAULT_LIMIT_LEVEL_TITLE;
		showFilenames = Consts.DEFAULT_SHOW_FILENAMES;
		verticalStart = Consts.DEFAULT_VERTICAL_START;
		executableMode = Consts.DEFAULT_EXECUTABLE_MODE;
	}

	public RenderConfiguration createChildConfiguration() {
		RenderConfiguration child = new RenderConfiguration(
			this.limitLevel - 1,
			this.limitLevelTitle - 1,
			this.showFilenames,
			!this.verticalStart,
			this.executableMode
		);
		return child;
	}
}

