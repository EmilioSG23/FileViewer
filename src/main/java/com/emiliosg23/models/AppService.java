package com.emiliosg23.models;

import com.emiliosg23.models.enums.Modes;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.tdas.trees.MultiTree;

/**
 * Fachada de servicios de la aplicación.
 *
 * <p>
 * Actúa como punto de entrada único para las operaciones de negocio:
 * inicializar un directorio, actualizar transformaciones, alternar modos y
 * ajustar niveles de profundidad. Delega toda la lógica a {@link AppLogic}.</p>
 *
 * @see AppLogic
 * @see PanelConfiguration
 * @see RenderConfiguration
 */
public class AppService {

    private final AppLogic app;

    /**
     * Crea el servicio con una nueva instancia de {@link AppLogic}.
     */
    public AppService() {
        this.app = new AppLogic();
    }

    /**
     * Inicializa el árbol de directorio para la ruta indicada. Cambia el
     * directorio activo, escanea el sistema de archivos en paralelo y aplica
     * las transformaciones activas.
     *
     * @param directory ruta absoluta del directorio
     * @return árbol transformado listo para renderizar
     */
    public MultiTree<Info> initDirectory(String directory) {
        this.app.changeDirectory(directory);
        this.app.createTreeDirectory();
        return this.app.transformTreeDirectory();
    }

    /**
     * Re-inicializa el directorio actual sin cambiar la ruta.
     *
     * @return árbol transformado
     */
    public MultiTree<Info> initDirectory() {
        this.app.createTreeDirectory();
        return this.app.transformTreeDirectory();
    }

    /**
     * Restaura toda la configuración a su estado inicial.
     */
    public void reset() {
        app.reset();
    }

    /**
     * Aplica las transformaciones activas al árbol actual sin re-escanear.
     *
     * @return árbol transformado, o {@code null} si no hay directorio activo
     */
    public MultiTree<Info> update() {
        return this.app.transformTreeDirectory();
    }

    /**
     * Alterna el estado de un modo de visualización.
     *
     * @param mode modo a alternar
     * @return nuevo estado del modo
     */
    public boolean toggleMode(Modes mode) {
        return app.toggleMode(mode);
    }

    /**
     * Establece explícitamente el estado de un modo.
     *
     * @param mode modo a modificar
     * @param enable {@code true} para activar
     * @return nuevo estado del modo
     */
    public boolean changeMode(Modes mode, boolean enable) {
        return app.changeMode(mode, enable);
    }

    /**
     * Alterna la visibilidad de nombres de archivo.
     *
     * @return nuevo estado de visibilidad
     */
    public boolean showFilenames() {
        return app.toggleShowFilenames();
    }

    /**
     * Incrementa el nivel de profundidad. @return nuevo valor
     */
    public int incrementLevel() {
        return app.increaseLimitLevel();
    }

    /**
     * Decrementa el nivel de profundidad. @return nuevo valor
     */
    public int decrementLevel() {
        return app.decreaseLimitLevel();
    }

    /**
     * Incrementa el nivel de títulos. @return nuevo valor
     */
    public int incrementLevelTitle() {
        return app.increaseLimitTitleLevel();
    }

    /**
     * Decrementa el nivel de títulos. @return nuevo valor
     */
    public int decrementLevelTitle() {
        return app.decreaseLimitTitleLevel();
    }

    /**
     * @return configuración de renderizado actual
     */
    public RenderConfiguration getRenderConfiguration() {
        return app.getRenderConfiguration();
    }

    /**
     * @return configuración del panel actual
     */
    public PanelConfiguration getPanelConfiguration() {
        return app.getPanelConfiguration();
    }
}
