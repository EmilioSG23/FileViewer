package com.fileviewer.infrastructure.preferences;

import java.util.prefs.Preferences;

import com.fileviewer.view.ThemeStyle;

/**
 * Persiste y recupera la preferencia de tema del usuario mediante
 * {@link java.util.prefs.Preferences}.
 *
 * <p>
 * Extraído de {@code AppController} para separar la persistencia de la lógica
 * de presentación. Se instancia pasando la clase propietaria para mantener el
 * mismo nodo de preferencias que se guardaba antes.</p>
 */
public class ThemePreferences {

    private static final String THEME_KEY = "theme";

    private final Preferences prefs;

    /**
     * @param owner clase cuyo paquete se usa como nodo de preferencias (p. ej.
     * {@code AppController.class})
     */
    public ThemePreferences(Class<?> owner) {
        this.prefs = Preferences.userNodeForPackage(owner);
    }

    /**
     * Carga el tema guardado. Si no hay ninguno, devuelve
     * {@link ThemeStyle#DARK}.
     */
    public ThemeStyle load() {
        String saved = prefs.get(THEME_KEY, ThemeStyle.DARK.name());
        try {
            return ThemeStyle.valueOf(saved);
        } catch (IllegalArgumentException e) {
            return ThemeStyle.DARK;
        }
    }

    /**
     * Persiste el tema seleccionado.
     *
     * @param theme tema a guardar
     */
    public void save(ThemeStyle theme) {
        prefs.put(THEME_KEY, theme.name());
    }
}
