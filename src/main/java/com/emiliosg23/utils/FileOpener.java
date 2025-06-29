package com.emiliosg23.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class FileOpener {

    public static void openFile(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("El archivo es nulo o no existe: " + file);
        }

        // Método 1: Java Desktop API (portable)
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file);
            return;
        }

        // Método 2: Comando del sistema según el SO
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
