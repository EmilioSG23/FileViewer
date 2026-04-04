package com.fileviewer.domain.model;

/**
 * Representa una extensión de archivo agrupada en el modo de visualización por
 * extensión.
 *
 * <p>
 * El nombre es la extensión (p. ej. {@code ".java"}) y el tamaño es la suma de
 * todos los archivos con esa extensión en el subárbol correspondiente.</p>
 *
 * @see Info
 * @see FileInfo
 */
public class ExtensionInfo extends Info {

    public ExtensionInfo(String name, long size) {
        super(name, size);
    }

    public ExtensionInfo(String name) {
        super(name);
    }
}
