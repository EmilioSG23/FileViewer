package com.fileviewer.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * Utilidad para abrir archivos con la aplicación predeterminada del sistema
 * operativo.
 *
 * <p>
 * Usa {@link Desktop#open(File)} cuando está disponible, y como fallback
 * ejecuta el comando nativo del SO (cmd, open, xdg-open).</p>
 */
public class FileOpener {

    /**
     * Abre un archivo con la aplicación predeterminada del sistema.
     *
     * @param file archivo a abrir
     * @throws IOException si no se puede abrir el archivo
     * @throws IllegalArgumentException si el archivo es nulo o no existe
     * @throws UnsupportedOperationException si el SO no es soportado
     */
    public static void openFile(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("El archivo es nulo o no existe: " + file);
        }

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file);
            return;
        }

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Windows
            new ProcessBuilder("cmd.exe", "/c", "start", "\"\"", "\"" + file.getAbsolutePath() + "\"").start();
        } else if (os.contains("mac")) {
            // macOS
            new ProcessBuilder("open", file.getAbsolutePath()).start();
        } else if (os.contains("nix") || os.contains("nux")) {
            // Linux
            new ProcessBuilder("xdg-open", file.getAbsolutePath()).start();
        } else {
            throw new UnsupportedOperationException("Sistema operativo no soportado: " + os);
        }
    }
}
