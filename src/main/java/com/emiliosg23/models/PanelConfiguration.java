package com.emiliosg23.models;

import com.emiliosg23.models.enums.Modes;

public class PanelConfiguration {
	private String directory;

	private boolean fileExtensionMode;
	private boolean acumulativeMode;

	private final RenderConfiguration renderConfiguration;

	public PanelConfiguration() {
		this.directory = "C://";
		this.fileExtensionMode = false;
		this.acumulativeMode = false;
		this.renderConfiguration = new RenderConfiguration();
	}

	public void reset() {
		this.directory = "C://";
		this.fileExtensionMode = false;
		this.acumulativeMode = false;
		this.renderConfiguration.reset();
	}

	public boolean changeMode(Modes mode) {
		if (mode == Modes.ACUMULATIVE)
			return this.acumulativeMode = !this.acumulativeMode;
		if (mode == Modes.EXECUTABLE)
			return renderConfiguration.toogleExecutableMode();
		if (mode == Modes.FILE_EXTENSION)
			return this.fileExtensionMode = !this.fileExtensionMode;
		throw new RuntimeException("Selected mode is not available");
	}

	public String getDirectory() {return directory;}
	public void setDirectory(String directory) {this.directory = directory;}
	public boolean isFileExtensionMode() {return fileExtensionMode;}
	public boolean isAcumulativeMode() {return acumulativeMode;}
	public boolean isExecutableMode() {return renderConfiguration.isExecutableMode();}
	public RenderConfiguration getRenderConfiguration() {return renderConfiguration;}
}
