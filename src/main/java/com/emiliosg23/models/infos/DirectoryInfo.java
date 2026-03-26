package com.emiliosg23.models.infos;

import java.io.File;

/**
 * Representa un directorio dentro del árbol de información.
 *
 * <p>
 * El tamaño de un directorio es la suma acumulada de los tamaños de todos sus
 * hijos (archivos y subdirectorios), calculado de forma bottom-up durante la
 * construcción del árbol.</p>
 *
 * @see Info
 * @see FileInfo
 */
public class DirectoryInfo extends Info {

    /**
     * Crea un {@code DirectoryInfo} con nombre y tamaño conocidos.
     *
     * @param name nombre del directorio
     * @param size tamaño acumulado en bytes
     */
    public DirectoryInfo(String name, long size) {
        super(name, size);
    }

    /**
     * Crea un {@code DirectoryInfo} con tamaño inicial cero.
     *
     * @param name nombre del directorio
     */
    public DirectoryInfo(String name) {
        super(name);
    }

    /**
     * Crea un {@code DirectoryInfo} a partir de un objeto {@link File}.
     *
     * @param file directorio del sistema de archivos
     */
    public DirectoryInfo(File file) {
        super(file.getName());
    }
}
