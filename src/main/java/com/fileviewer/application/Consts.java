package com.fileviewer.application;

/**
 * Constantes globales de configuración de la aplicación FileViewer.
 *
 * <p>
 * Define valores por defecto para límites de profundidad, modos de
 * visualización y otras propiedades de la interfaz de usuario.</p>
 */
public final class Consts {

    private Consts() {
    }

    /**
     * Nombre de la aplicación mostrado en la barra de título.
     */
    public static final String PROGRAM_NAME = "File Viewer";

    // --- Límites de profundidad ---
    /**
     * Nivel máximo de profundidad permitido para la visualización del treemap.
     */
    public static final int MAX_NUM_LEVEL_LIMIT = 10;

    /**
     * Nivel máximo de profundidad para mostrar títulos de directorio.
     */
    public static final int MAX_NUM_TITLE_LEVEL_LIMIT = 3;

    /**
     * Nivel de profundidad inicial por defecto.
     */
    public static final int DEFAULT_LIMIT_LEVEL = 8;

    /**
     * Nivel de profundidad de títulos por defecto.
     */
    public static final int DEFAULT_LIMIT_LEVEL_TITLE = 2;

    // --- Configuraciones de renderizado ---
    /**
     * Si se muestran nombres de archivo por defecto.
     */
    public static final boolean DEFAULT_SHOW_FILENAMES = true;

    /**
     * Orientación vertical inicial por defecto.
     */
    public static final boolean DEFAULT_VERTICAL_START = false;

    /**
     * Directorio seleccionado por defecto (vacío = ninguno).
     */
    public static final String DEFAULT_DIRECTORY = "";
}
