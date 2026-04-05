package com.fileviewer.style;

import com.fileviewer.App;

/**
 * Temas visuales disponibles para la aplicación.
 *
 * <p>
 * Cada tema apunta a un archivo CSS dentro de {@code themes/theme-*.css}. La
 * preferencia del usuario se persiste con
 * {@link java.util.prefs.Preferences}.</p>
 */
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

    ThemeStyle(String path) {
        this.path = path;
    }

    public String getPath() {
        return App.class.getResource(absolutePath + path).toExternalForm();
    }

    @Override
    public String toString() {
        return switch (this) {
            case DARK ->
                "Dark";
            case DRACULA ->
                "Dracula";
            case GRUVBOX_DARK ->
                "Gruvbox Dark";
            case LIGHT ->
                "Light";
            case MONOKAI ->
                "Monokai";
            case NORD ->
                "Nord";
            case ONE_DARK ->
                "One Dark";
            case SOLARIZED_DARK ->
                "Solarized Dark";
            case SOLARIZED_LIGHT ->
                "Solarized Light";
            default ->
                name();
        };
    }
}
