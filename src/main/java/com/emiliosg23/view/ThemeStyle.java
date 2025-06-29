package com.emiliosg23.view;

import com.emiliosg23.App;

public enum ThemeStyle {
	DARK("dark.css"),
	DRACULA("dracula.css"),
	GRUVBOX_DARK("gruvbox-dark.css"),
	LIGHT("light.css"),
	MONOKAI("monokai.css"),
	NORD("nord.css"),
	ONE_DARK("one-dark.css"),
	SOLARIZED_DARK("solarized-dark.css"),
	SOLARIZED_LIGHT("solarized-light.css");

	private final String path;
	private final String absolutePath = "themes/theme-";
	ThemeStyle(String path) { this.path = path; }

	public String getPath() {
		return App.class.getResource(absolutePath + path).toExternalForm();
	}

	@Override
	public String toString() {
		switch (this) {
			case DARK: return "Dark";
			case DRACULA: return "Dracula";
			case GRUVBOX_DARK: return "Gruvbox Dark";
			case LIGHT: return "Light";
			case MONOKAI: return "Monokai";
			case NORD: return "Nord";
			case ONE_DARK: return "One Dark";
			case SOLARIZED_DARK: return "Solarized Dark";
			case SOLARIZED_LIGHT: return "Solarized Light";
			default: return name();
		}
	}
}