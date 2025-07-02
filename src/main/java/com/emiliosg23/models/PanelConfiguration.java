package com.emiliosg23.models;

import com.emiliosg23.models.enums.Modes;
import com.emiliosg23.utils.Consts;

public class PanelConfiguration {
	private String directory;

	private boolean fileExtensionMode;
	private boolean acumulativeMode;

	private final RenderConfiguration renderConfiguration;

	public PanelConfiguration() {
		this.directory = Consts.DEFAULT_DIRECTORY;
		this.fileExtensionMode = Consts.DEFAULT_FILE_EXTENSION_MODE;
		this.acumulativeMode = Consts.DEFAULT_ACUMULATIVE_MODE;
		this.renderConfiguration = new RenderConfiguration();
	}

	public void reset() {
		this.directory = Consts.DEFAULT_DIRECTORY;
		this.fileExtensionMode = Consts.DEFAULT_FILE_EXTENSION_MODE;
		this.acumulativeMode = Consts.DEFAULT_ACUMULATIVE_MODE;
		this.renderConfiguration.reset();
	}

	public boolean toogleMode(Modes mode) {
		if (mode == Modes.ACUMULATIVE)
			return this.acumulativeMode = !this.acumulativeMode;
		if (mode == Modes.EXECUTABLE)
			return renderConfiguration.toogleExecutableMode();
		if (mode == Modes.FILE_EXTENSION)
			return this.fileExtensionMode = !this.fileExtensionMode;
		throw new RuntimeException("Selected mode is not available");
	}

	public boolean changeMode(Modes mode, boolean enable){
		if (mode == Modes.ACUMULATIVE)
			return this.acumulativeMode = enable;
		if (mode == Modes.EXECUTABLE)
			return renderConfiguration.changeExecutableMode(enable);
		if (mode == Modes.FILE_EXTENSION)
			return this.fileExtensionMode = enable;
		throw new RuntimeException("Selected mode is not available");
	}

	public String getDirectory() {return directory;}
	public void setDirectory(String directory) {this.directory = directory;}
	public boolean isFileExtensionMode() {return fileExtensionMode;}
	public boolean isAcumulativeMode() {return acumulativeMode;}
	public boolean isExecutableMode() {return renderConfiguration.isExecutableMode();}
	public RenderConfiguration getRenderConfiguration() {return renderConfiguration;}
}
