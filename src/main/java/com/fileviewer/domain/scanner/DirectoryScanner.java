package com.fileviewer.domain.scanner;

import com.fileviewer.domain.model.Info;
import com.fileviewer.tdas.trees.MultiTree;

/**
 * Contrato para escanear un directorio del sistema de archivos y construir un
 * {@link MultiTree} de información.
 *
 * <p>
 * La implementación concreta
 * ({@link com.fileviewer.infrastructure.scanner.DirectoryTreeBuilder}) usa un
 * {@code ForkJoinPool} para escaneo paralelo, pero la aplicación depende solo
 * de esta interfaz para facilitar pruebas y futuras implementaciones
 * alternativas.</p>
 *
 * @see NodeMetricStrategy
 */
@FunctionalInterface
public interface DirectoryScanner {

    /**
     * Escanea el directorio y devuelve el árbol con tamaños acumulados.
     *
     * @param directoryPath ruta absoluta del directorio a escanear
     * @return árbol completo; nunca {@code null}
     */
    MultiTree<Info> scan(String directoryPath);
}
