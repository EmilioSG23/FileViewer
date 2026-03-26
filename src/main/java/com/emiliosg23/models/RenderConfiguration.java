package com.emiliosg23.models;

import com.emiliosg23.utils.Consts;

/**
 * Configuración de renderizado del treemap.
 *
 * <p>
 * Controla los niveles de profundidad, la visibilidad de nombres de archivo, la
 * orientación vertical/horizontal y el modo ejecutable. Cada nivel del árbol
 * utiliza una configuración hija creada con
 * {@link #createChildConfiguration()}, que decrementa los niveles y alterna la
 * orientación.</p>
 *
 * @see PanelConfiguration
 * @see Consts
 */
public class RenderConfiguration {

    private final int MAX_NUM_LEVEL_LIMIT = Consts.MAX_NUM_LEVEL_LIMIT;
    private final int MAX_NUM_TITLE_LEVEL_LIMIT = Consts.MAX_NUM_TITLE_LEVEL_LIMIT;

    private int limitLevel = Consts.DEFAULT_LIMIT_LEVEL;
    private int limitLevelTitle = Consts.DEFAULT_LIMIT_LEVEL_TITLE;
    private boolean showFilenames = Consts.DEFAULT_SHOW_FILENAMES;
    private boolean verticalStart = Consts.DEFAULT_VERTICAL_START;
    private boolean executableMode = Consts.DEFAULT_EXECUTABLE_MODE;

    /**
     * Crea una configuración con valores por defecto.
     */
    public RenderConfiguration() {
    }

    /**
     * Crea una configuración con valores explícitos (usado internamente para
     * configuraciones hijas).
     *
     * @param limitLevel nivel de profundidad del treemap
     * @param limitLevelTitle nivel de profundidad para títulos de directorio
     * @param showFilenames si se muestran nombres de archivo
     * @param verticalStart si la orientación inicial es vertical
     * @param executableMode si el modo ejecutable está activo
     */
    public RenderConfiguration(int limitLevel, int limitLevelTitle, boolean showFilenames, boolean verticalStart, boolean executableMode) {
        this.limitLevel = limitLevel;
        this.limitLevelTitle = limitLevelTitle;
        this.showFilenames = showFilenames;
        this.verticalStart = verticalStart;
        this.executableMode = executableMode;
    }

    // --- Getters ---
    /**
     * @return nivel actual de profundidad del treemap
     */
    public int getLimitLevel() {
        return limitLevel;
    }

    /**
     * @return nivel actual de profundidad para títulos
     */
    public int getLimitLevelTitle() {
        return limitLevelTitle;
    }

    /**
     * @return {@code true} si se muestran nombres de archivo
     */
    public boolean isShowFilenames() {
        return showFilenames;
    }

    /**
     * @return {@code true} si la orientación inicial es vertical
     */
    public boolean isVerticalStart() {
        return verticalStart;
    }

    /**
     * @return {@code true} si el modo ejecutable está activo
     */
    public boolean isExecutableMode() {
        return executableMode;
    }

    // --- Toggles ---
    /**
     * Alterna la visibilidad de nombres de archivo.
     *
     * @return nuevo estado ({@code true} = visible)
     */
    public boolean toggleShowFilenames() {
        showFilenames = !showFilenames;
        return showFilenames;
    }

    /**
     * Alterna la orientación inicial.
     *
     * @return nuevo estado ({@code true} = vertical)
     */
    public boolean toggleOrientation() {
        verticalStart = !verticalStart;
        return verticalStart;
    }

    /**
     * Alterna el modo ejecutable.
     *
     * @return nuevo estado ({@code true} = activo)
     */
    public boolean toggleExecutableMode() {
        executableMode = !executableMode;
        return executableMode;
    }

    /**
     * Establece explícitamente el modo ejecutable.
     *
     * @param enable {@code true} para activar
     * @return nuevo estado del modo
     */
    public boolean changeExecutableMode(boolean enable) {
        executableMode = enable;
        return executableMode;
    }

    // --- Ajustes de nivel ---
    /**
     * Incrementa el nivel de profundidad del treemap (máximo:
     * {@link Consts#MAX_NUM_LEVEL_LIMIT}).
     *
     * @return nuevo valor del nivel
     */
    public int increaseLimitLevel() {
        if (limitLevel < MAX_NUM_LEVEL_LIMIT) {
            limitLevel++;
        }
        return limitLevel;
    }

    /**
     * Decrementa el nivel de profundidad del treemap (mínimo: 1).
     *
     * @return nuevo valor del nivel
     */
    public int decreaseLimitLevel() {
        if (limitLevel > 1) {
            limitLevel--;
        }
        return limitLevel;
    }

    /**
     * Incrementa el nivel de profundidad de títulos (máximo:
     * {@link Consts#MAX_NUM_TITLE_LEVEL_LIMIT}).
     *
     * @return nuevo valor del nivel de títulos
     */
    public int increaseTitleLimitLevel() {
        if (limitLevelTitle < MAX_NUM_TITLE_LEVEL_LIMIT) {
            limitLevelTitle++;
        }
        return limitLevelTitle;
    }

    /**
     * Decrementa el nivel de profundidad de títulos (mínimo: 1).
     *
     * @return nuevo valor del nivel de títulos
     */
    public int decreaseTitleLimitLevel() {
        if (limitLevelTitle > 1) {
            limitLevelTitle--;
        }
        return limitLevelTitle;
    }

    /**
     * Restaura todos los parámetros de renderizado a sus valores por defecto.
     */
    public void reset() {
        limitLevel = Consts.DEFAULT_LIMIT_LEVEL;
        limitLevelTitle = Consts.DEFAULT_LIMIT_LEVEL_TITLE;
        showFilenames = Consts.DEFAULT_SHOW_FILENAMES;
        verticalStart = Consts.DEFAULT_VERTICAL_START;
        executableMode = Consts.DEFAULT_EXECUTABLE_MODE;
    }

    /**
     * Crea una configuración hija para el siguiente nivel de profundidad.
     * Decrementa ambos niveles en 1 y alterna la orientación.
     *
     * @return nueva configuración para nodos hijos
     */
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
