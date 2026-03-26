package com.emiliosg23.utils;

/**
 * Utilidades para operaciones con archivos y rutas.
 *
 * <p>
 * Proporciona métodos para extraer nombres de directorio, extensiones y
 * convertir tamaños en bytes a unidades legibles.</p>
 */
public class FileUtils {

    /**
     * Obtiene el último componente de una ruta de directorio (separador:
     * {@code \\}).
     *
     * @param directory ruta del directorio
     * @return nombre del último directorio en la ruta
     */
    public static String lastDirectory(String directory) {
        String[] tokens = directory.split("\\\\");
        return tokens[tokens.length - 1];
    }

    /**
     * Extrae la extensión de un nombre de archivo (incluyendo el punto).
     *
     * @param filename nombre o ruta del archivo
     * @return extensión con punto (por ejemplo {@code ".java"})
     */
    public static String extractExtension(String filename) {
        String[] tokens = filename.split("\\.");
        return "." + tokens[tokens.length - 1];
    }

    /**
     * Convierte un tamaño en bytes a una cadena legible con la unidad adecuada.
     *
     * @param size tamaño en bytes
     * @return cadena formateada (por ejemplo {@code "1.50 MB"})
     */
    public static String getConvertedSize(long size) {
        if (size <= 0) {
            return "0 bytes";
        }

        final String[] units = {"bytes", "KB", "MB", "GB", "TB", "PB", "EB"};
        final int base = 1024;

        int unitIndex = 0;
        double readableSize = size;

        while (readableSize >= base && unitIndex < units.length - 1) {
            readableSize /= base;
            unitIndex++;
        }

        return String.format("%.2f %s", readableSize, units[unitIndex]);
    }
}
