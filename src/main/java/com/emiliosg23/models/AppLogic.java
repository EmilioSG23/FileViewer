package com.emiliosg23.models;

import com.emiliosg23.logic.TreeInfoGenerator;
import com.emiliosg23.models.enums.Modes;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.tdas.trees.MultiTree;

/**
 * Lógica principal de la aplicación.
 *
 * <p>
 * Coordina la creación y transformación del árbol de directorio según los modos
 * activos en {@link PanelConfiguration}. Mantiene el árbol original en memoria
 * para poder aplicar transformaciones sin re-escanear el sistema de
 * archivos.</p>
 *
 * @see AppService
 * @see TreeInfoGenerator
 * @see PanelConfiguration
 */
public class AppLogic {

    private final PanelConfiguration panelConfiguration;
    private final TreeInfoGenerator treeGenerator;

    private MultiTree<Info> directoryTree;

    /**
     * Crea la lógica con configuración y generador por defecto.
     */
    public AppLogic() {
        this.panelConfiguration = new PanelConfiguration();
        this.treeGenerator = new TreeInfoGenerator();
    }

    /**
     * Cambia el directorio activo.
     *
     * @param directory ruta absoluta del nuevo directorio
     */
    public void changeDirectory(String directory) {
        this.panelConfiguration.setDirectory(directory);
    }

    /**
     * Escanea el directorio activo y construye el árbol de información. El
     * escaneo usa múltiples hilos internamente a través de
     * {@link TreeInfoGenerator}.
     *
     * @return árbol construido con tamaños acumulados
     */
    public MultiTree<Info> createTreeDirectory() {
        String directory = panelConfiguration.getDirectory();
        this.directoryTree = treeGenerator.createTree(directory);
        return this.directoryTree;
    }

    /**
     * Aplica las transformaciones activas (extensión, acumulativo) al árbol
     * actual. Trabaja sobre una copia para preservar el original.
     *
     * @return árbol transformado, o {@code null} si no hay árbol cargado
     */
    public MultiTree<Info> transformTreeDirectory() {
        if (directoryTree == null) {
            return null;
        }
        MultiTree<Info> tree = treeGenerator.copyTree(directoryTree);

        if (panelConfiguration.isFileExtensionMode()) {
            tree = treeGenerator.transformTree(tree, Modes.FILE_EXTENSION);
        }

        if (panelConfiguration.isFileExtensionMode() && panelConfiguration.isAcumulativeMode()) {
            tree = treeGenerator.transformTree(tree, Modes.ACUMULATIVE);
        }

        return tree;
    }

    /**
     * Alterna un modo de visualización.
     *
     * @param mode modo a alternar
     * @return nuevo estado del modo
     */
    public boolean toggleMode(Modes mode) {
        return this.panelConfiguration.toggleMode(mode);
    }

    /**
     * Establece explícitamente el estado de un modo.
     *
     * @param mode modo a modificar
     * @param enable {@code true} para activar
     * @return nuevo estado del modo
     */
    public boolean changeMode(Modes mode, boolean enable) {
        return this.panelConfiguration.changeMode(mode, enable);
    }

    /**
     * Alterna la visibilidad de nombres de archivo.
     *
     * @return nuevo estado de visibilidad
     */
    public boolean toggleShowFilenames() {
        return this.panelConfiguration.getRenderConfiguration().toggleShowFilenames();
    }

    /**
     * Incrementa el nivel de profundidad. @return nuevo valor
     */
    public int increaseLimitLevel() {
        return this.panelConfiguration.getRenderConfiguration().increaseLimitLevel();
    }

    /**
     * Decrementa el nivel de profundidad. @return nuevo valor
     */
    public int decreaseLimitLevel() {
        return this.panelConfiguration.getRenderConfiguration().decreaseLimitLevel();
    }

    /**
     * Incrementa el nivel de títulos. @return nuevo valor
     */
    public int increaseLimitTitleLevel() {
        return this.panelConfiguration.getRenderConfiguration().increaseTitleLimitLevel();
    }

    /**
     * Decrementa el nivel de títulos. @return nuevo valor
     */
    public int decreaseLimitTitleLevel() {
        return this.panelConfiguration.getRenderConfiguration().decreaseTitleLimitLevel();
    }

    /**
     * Restaura toda la configuración y descarta el árbol actual.
     */
    public void reset() {
        this.panelConfiguration.reset();
        this.directoryTree = null;
    }

    /**
     * @return configuración de renderizado actual
     */
    public RenderConfiguration getRenderConfiguration() {
        return this.panelConfiguration.getRenderConfiguration();
    }

    /**
     * @return configuración del panel actual
     */
    public PanelConfiguration getPanelConfiguration() {
        return this.panelConfiguration;
    }
}
