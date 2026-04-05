package com.fileviewer.utils;

/**
 * Utilidades para operaciones con archivos y rutas.
 *
 * <p>
 * Proporciona métodos para extraer nombres de directorio, extensiones y
 * convertir tamaños en bytes a unidades legibles.</p>
 */
public class FileUtils {

    /**
     * Obtiene el último componente de una ruta de directorio.
     *
     * @param directory ruta del directorio
     * @return nombre del último directorio en la ruta
     */
    public static String lastDirectory(String directory) {
        int idx = Math.max(directory.lastIndexOf('/'), directory.lastIndexOf('\\'));
        return idx >= 0 ? directory.substring(idx + 1) : directory;
    }

    /**
     * Extrae la extensión de un nombre de archivo (incluyendo el punto).
     *
     * @param filename nombre o ruta del archivo
     * @return extensión con punto (por ejemplo {@code ".java"}), o cadena vacía si no tiene
     */
    public static String extractExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex);
        }
        return "";
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
