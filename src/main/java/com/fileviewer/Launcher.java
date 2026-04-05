package com.fileviewer;

import javafx.application.Application;

/**
 * Punto de entrada alternativo de la aplicación.
 *
 * <p>
 * Necesario para empaquetado con {@code jpackage}, ya que JavaFX requiere que
 * el main class no extienda {@link Application} cuando se usa con módulos.</p>
 */
public class Launcher {

    /**
     * Lanza la aplicación JavaFX.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
