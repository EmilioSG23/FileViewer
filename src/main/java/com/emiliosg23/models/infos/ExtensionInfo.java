package com.emiliosg23.models.infos;

/**
 * Representa una extensión de archivo agrupada dentro del modo de visualización
 * por extensión.
 *
 * <p>
 * El nombre es la extensión (por ejemplo {@code ".java"}) y el tamaño es la
 * suma de todos los archivos con esa extensión en el directorio o subárbol
 * correspondiente.</p>
 *
 * @see Info
 * @see FileInfo
 */
public class ExtensionInfo extends Info {

    /**
     * Crea un {@code ExtensionInfo} con extensión y tamaño conocidos.
     *
     * @param name extensión (por ejemplo {@code ".java"})
     * @param size tamaño acumulado en bytes
     */
    public ExtensionInfo(String name, long size) {
        super(name, size);
    }

    /**
     * Crea un {@code ExtensionInfo} con tamaño inicial cero.
     *
     * @param name extensión del archivo
     */
    public ExtensionInfo(String name) {
        super(name);
    }
}
