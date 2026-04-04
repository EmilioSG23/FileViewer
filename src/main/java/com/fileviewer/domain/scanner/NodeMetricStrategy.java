package com.fileviewer.domain.scanner;

import java.io.File;

/**
 * Estrategia para calcular la métrica de un nodo del sistema de archivos.
 *
 * <p>
 * El valor por defecto ({@link #fileSize()}) devuelve {@link File#length()},
 * pero una implementación alternativa podría devolver, por ejemplo, el conteo
 * de líneas, el peso según tipo de archivo, etc.</p>
 *
 * <p>
 * Para añadir una nueva métrica basta con proveer una implementación de esta
 * interfaz e inyectarla en el scanner.</p>
 *
 * @see DirectoryScanner
 */
@FunctionalInterface
public interface NodeMetricStrategy {

    /**
     * Calcula la métrica para el archivo dado.
     *
     * @param file archivo del sistema de archivos (no puede ser un directorio)
     * @return valor de la métrica (usualmente bytes)
     */
    long computeMetric(File file);

    /**
     * Estrategia por defecto: tamaño real en bytes del archivo.
     */
    static NodeMetricStrategy fileSize() {
        return File::length;
    }
}
