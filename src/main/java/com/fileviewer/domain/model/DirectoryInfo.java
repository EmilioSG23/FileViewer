package com.fileviewer.domain.model;

import java.io.File;

/**
 * Representa un directorio dentro del árbol de información.
 *
 * <p>
 * El tamaño acumulado es la suma de todos sus hijos, calculado de forma
 * bottom-up por el scanner.</p>
 *
 * @see Info
 * @see FileInfo
 */
public class DirectoryInfo extends Info {

    public DirectoryInfo(String name, long size) {
        super(name, size);
    }

    public DirectoryInfo(String name) {
        super(name);
    }

    public DirectoryInfo(File file) {
        super(file.getName());
    }
}
