package com.emiliosg23.domain.scanner;

import com.emiliosg23.domain.model.Info;
import com.emiliosg23.tdas.trees.MultiTree;

/**
 * Contrato para escanear un directorio del sistema de archivos y construir un
 * {@link MultiTree} de información.
 *
 * <p>
 * La implementación concreta
 * ({@link com.emiliosg23.infrastructure.scanner.DirectoryTreeBuilder}) usa un
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
