package com.emiliosg23.models;

public class RenderConfiguration {
	private final int MAX_NUM_LEVEL_LIMIT = 10;
	private final int MAX_NUM_TITLE_LEVEL_LIMIT = 3;

	private int limitLevel = 8;
	private int limitLevelTitle = 2;
	private boolean showFilenames = true;
	private boolean verticalStart = false;
	private boolean executableMode = false;

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
		limitLevel = 8;
		limitLevelTitle = 2;
		showFilenames = true;
		verticalStart = false;
		executableMode = false;
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

