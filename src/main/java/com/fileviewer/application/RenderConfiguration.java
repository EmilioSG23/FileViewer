package com.fileviewer.application;

/**
 * Parámetros puramente de renderizado del treemap: niveles de profundidad,
 * visibilidad de nombres y orientación inicial.
 *
 * <p>
 * El método {@link #createChildConfiguration()} produce una configuración
 * derivada con niveles decrementados y orientación alternada, usada por
 * {@link com.fileviewer.view.TreeRender} en cada nivel del árbol.</p>
 */
public class RenderConfiguration {

    private int limitLevel = Consts.DEFAULT_LIMIT_LEVEL;
    private int limitLevelTitle = Consts.DEFAULT_LIMIT_LEVEL_TITLE;
    private boolean showFilenames = Consts.DEFAULT_SHOW_FILENAMES;
    private boolean verticalStart = Consts.DEFAULT_VERTICAL_START;

    public RenderConfiguration() {
    }

    private RenderConfiguration(int limitLevel, int limitLevelTitle,
            boolean showFilenames, boolean verticalStart) {
        this.limitLevel = limitLevel;
        this.limitLevelTitle = limitLevelTitle;
        this.showFilenames = showFilenames;
        this.verticalStart = verticalStart;
    }

    public int getLimitLevel() {
        return limitLevel;
    }

    public int getLimitLevelTitle() {
        return limitLevelTitle;
    }

    public boolean isShowFilenames() {
        return showFilenames;
    }

    public boolean isVerticalStart() {
        return verticalStart;
    }

    public boolean toggleShowFilenames() {
        showFilenames = !showFilenames;
        return showFilenames;
    }

    public boolean toggleOrientation() {
        verticalStart = !verticalStart;
        return verticalStart;
    }

    public int increaseLimitLevel() {
        if (limitLevel < Consts.MAX_NUM_LEVEL_LIMIT) {
            limitLevel++;
        }
        return limitLevel;
    }

    public int decreaseLimitLevel() {
        if (limitLevel > 1) {
            limitLevel--;
        }
        return limitLevel;
    }

    public int increaseTitleLimitLevel() {
        if (limitLevelTitle < Consts.MAX_NUM_TITLE_LEVEL_LIMIT) {
            limitLevelTitle++;
        }
        return limitLevelTitle;
    }

    public int decreaseTitleLimitLevel() {
        if (limitLevelTitle > 1) {
            limitLevelTitle--;
        }
        return limitLevelTitle;
    }

    public void reset() {
        limitLevel = Consts.DEFAULT_LIMIT_LEVEL;
        limitLevelTitle = Consts.DEFAULT_LIMIT_LEVEL_TITLE;
        showFilenames = Consts.DEFAULT_SHOW_FILENAMES;
        verticalStart = Consts.DEFAULT_VERTICAL_START;
    }

    /**
     * Crea una configuración derivada para el siguiente nivel del árbol.
     */
    public RenderConfiguration createChildConfiguration() {
        return new RenderConfiguration(limitLevel - 1, limitLevelTitle - 1,
                showFilenames, !verticalStart);
    }
}
