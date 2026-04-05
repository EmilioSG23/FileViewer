package com.fileviewer.domain.model;

import com.fileviewer.utils.FileUtils;

/**
 * Clase base abstracta que representa un elemento del sistema de archivos
 * (archivo, directorio o extensión).
 *
 * <p>
 * Contiene el nombre del elemento y su tamaño en bytes. Para directorios, el
 * tamaño es la suma acumulada de todos sus hijos.</p>
 *
 * @see FileInfo
 * @see DirectoryInfo
 * @see ExtensionInfo
 */
public class Info {

    private final String name;
    private long size;

    public Info(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public Info(String name) {
        this.name = name;
        this.size = 0;
    }

    public String getName() {
        return this.name;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return getName() + " (" + FileUtils.getConvertedSize(this.size) + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Info other)) {
            return false;
        }
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode() + Long.hashCode(size);
    }
}
