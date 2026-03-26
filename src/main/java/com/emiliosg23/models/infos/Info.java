package com.emiliosg23.models.infos;

import com.emiliosg23.utils.FileUtils;

/**
 * Clase base abstracta que representa un elemento del sistema de archivos
 * (archivo, directorio o extensión).
 *
 * <p>
 * Contiene el nombre del elemento y su tamaño en bytes. Para directorios, el
 * tamaño es la suma acumulada de todos sus hijos. La igualdad se define
 * exclusivamente por nombre.</p>
 *
 * @see FileInfo
 * @see DirectoryInfo
 * @see ExtensionInfo
 */
public class Info {

    private final String name;
    private long size;

    /**
     * Crea un {@code Info} con nombre y tamaño inicial.
     *
     * @param name nombre del elemento
     * @param size tamaño en bytes
     */
    public Info(String name, long size) {
        this.name = name;
        this.size = size;
    }

    /**
     * Crea un {@code Info} con tamaño inicial cero.
     *
     * @param name nombre del elemento
     */
    public Info(String name) {
        this.name = name;
        this.size = 0;
    }

    /**
     * @return nombre del elemento
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return tamaño en bytes
     */
    public long getSize() {
        return this.size;
    }

    /**
     * Establece el tamaño en bytes.
     *
     * @param size nuevo tamaño
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * Representación textual: nombre seguido del tamaño convertido a unidades
     * legibles.
     *
     * @return cadena con formato {@code "nombre (tamaño unidad)"}
     */
    @Override
    public String toString() {
        return getName() + " (" + FileUtils.getConvertedSize(this.size) + ")";
    }

    /**
     * Dos instancias de {@code Info} son iguales si tienen el mismo nombre.
     *
     * @param o objeto a comparar
     * @return {@code true} si {@code o} es un {@code Info} con el mismo nombre
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Info)) {
            return false;
        }
        Info other = (Info) o;
        return getName().equals(other.getName());
    }

    /**
     * @return hash basado en el nombre
     */
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
