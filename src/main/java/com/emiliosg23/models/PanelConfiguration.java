package com.emiliosg23.models;

import com.emiliosg23.models.enums.Modes;
import com.emiliosg23.utils.Consts;

/**
 * Configuración del panel principal de la aplicación.
 *
 * <p>
 * Almacena el directorio seleccionado y el estado de los modos de visualización
 * (extensión de archivo, acumulativo). Contiene también una referencia a
 * {@link RenderConfiguration} para los parámetros de renderizado.</p>
 *
 * @see RenderConfiguration
 * @see Modes
 */
public class PanelConfiguration {

    private String directory;

    private boolean fileExtensionMode;
    private boolean acumulativeMode;

    private final RenderConfiguration renderConfiguration;

    /**
     * Crea una configuración de panel con valores por defecto definidos en
     * {@link Consts}.
     */
    public PanelConfiguration() {
        this.directory = Consts.DEFAULT_DIRECTORY;
        this.fileExtensionMode = Consts.DEFAULT_FILE_EXTENSION_MODE;
        this.acumulativeMode = Consts.DEFAULT_ACUMULATIVE_MODE;
        this.renderConfiguration = new RenderConfiguration();
    }

    /**
     * Restaura todos los valores a su estado por defecto.
     */
    public void reset() {
        this.directory = Consts.DEFAULT_DIRECTORY;
        this.fileExtensionMode = Consts.DEFAULT_FILE_EXTENSION_MODE;
        this.acumulativeMode = Consts.DEFAULT_ACUMULATIVE_MODE;
        this.renderConfiguration.reset();
    }

    /**
     * Alterna el estado de un modo de visualización.
     *
     * @param mode modo a alternar
     * @return nuevo estado del modo ({@code true} = activo)
     * @throws RuntimeException si el modo no es soportado
     */
    public boolean toggleMode(Modes mode) {
        if (mode == Modes.ACUMULATIVE) {
            return this.acumulativeMode = !this.acumulativeMode;
        }
        if (mode == Modes.EXECUTABLE) {
            return renderConfiguration.toggleExecutableMode();
        }
        if (mode == Modes.FILE_EXTENSION) {
            return this.fileExtensionMode = !this.fileExtensionMode;
        }
        throw new RuntimeException("Selected mode is not available");
    }

    /**
     * Establece explícitamente el estado de un modo.
     *
     * @param mode modo a modificar
     * @param enable {@code true} para activar, {@code false} para desactivar
     * @return nuevo estado del modo
     * @throws RuntimeException si el modo no es soportado
     */
    public boolean changeMode(Modes mode, boolean enable) {
        if (mode == Modes.ACUMULATIVE) {
            return this.acumulativeMode = enable;
        }
        if (mode == Modes.EXECUTABLE) {
            return renderConfiguration.changeExecutableMode(enable);
        }
        if (mode == Modes.FILE_EXTENSION) {
            return this.fileExtensionMode = enable;
        }
        throw new RuntimeException("Selected mode is not available");
    }

    /**
     * @return directorio actualmente seleccionado
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Establece el directorio a explorar.
     *
     * @param directory ruta absoluta del directorio
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * @return {@code true} si el modo de extensión de archivo está activo
     */
    public boolean isFileExtensionMode() {
        return fileExtensionMode;
    }

    /**
     * @return {@code true} si el modo acumulativo está activo
     */
    public boolean isAcumulativeMode() {
        return acumulativeMode;
    }

    /**
     * @return {@code true} si el modo ejecutable está activo
     */
    public boolean isExecutableMode() {
        return renderConfiguration.isExecutableMode();
    }

    /**
     * @return configuración de renderizado asociada
     */
    public RenderConfiguration getRenderConfiguration() {
        return renderConfiguration;
    }
}
