package com.fileviewer.domain.model;

import java.io.File;

import com.fileviewer.utils.FileUtils;

/**
 * Representa un archivo individual dentro del árbol de directorios.
 *
 * <p>
 * Almacena ruta completa y extensión además del nombre y tamaño heredados de
 * {@link Info}.</p>
 *
 * @see Info
 * @see DirectoryInfo
 */
public class FileInfo extends Info {

    private final String fullPath;
    private final String extension;

    public FileInfo(String name, long size, String fullPath, String extension) {
        super(name, size);
        this.fullPath = fullPath;
        this.extension = extension;
    }

    public FileInfo(String name, String fullPath, String extension) {
        super(name);
        this.fullPath = fullPath;
        this.extension = extension;
    }

    /**
     * Crea un {@code FileInfo} a partir de un {@link File}; la extensión se
     * extrae automáticamente del nombre.
     */
    public FileInfo(File file) {
        super(file.getName());
        this.fullPath = file.getAbsolutePath();
        this.extension = FileUtils.extractExtension(file.toString());
    }

    public String getExtension() {
        return this.extension;
    }

    public String getFullPath() {
        return this.fullPath;
    }
}
