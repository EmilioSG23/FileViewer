package com.emiliosg23.models.infos;

import java.io.File;

import com.emiliosg23.utils.FileUtils;

/**
 * Representa un archivo individual dentro del árbol de directorios.
 *
 * <p>
 * Además de nombre y tamaño (heredados de {@link Info}), almacena la ruta
 * completa y la extensión del archivo.</p>
 *
 * @see Info
 * @see DirectoryInfo
 */
public class FileInfo extends Info {

    private final String fullPath;
    private final String extension;

    /**
     * Crea un {@code FileInfo} con todos los campos explícitos.
     *
     * @param name nombre del archivo
     * @param size tamaño en bytes
     * @param fullPath ruta absoluta
     * @param extension extensión del archivo (por ejemplo {@code ".java"})
     */
    public FileInfo(String name, long size, String fullPath, String extension) {
        super(name, size);
        this.fullPath = fullPath;
        this.extension = extension;
    }

    /**
     * Crea un {@code FileInfo} con tamaño inicial cero.
     *
     * @param name nombre del archivo
     * @param fullPath ruta absoluta
     * @param extension extensión del archivo
     */
    public FileInfo(String name, String fullPath, String extension) {
        super(name);
        this.fullPath = fullPath;
        this.extension = extension;
    }

    /**
     * Crea un {@code FileInfo} a partir de un objeto {@link File}. La extensión
     * se extrae automáticamente del nombre del archivo.
     *
     * @param file archivo del sistema de archivos
     */
    public FileInfo(File file) {
        super(file.getName());
        this.fullPath = file.getAbsolutePath();
        this.extension = FileUtils.extractExtension(file.toString());
    }

    /**
     * @return extensión del archivo (por ejemplo {@code ".java"})
     */
    public String getExtension() {
        return this.extension;
    }

    /**
     * @return ruta absoluta del archivo
     */
    public String getFullPath() {
        return this.fullPath;
    }
}
