package com.emiliosg23.domain.pipeline;

import java.util.Set;

import com.emiliosg23.domain.model.Info;
import com.emiliosg23.tdas.trees.MultiTree;

/**
 * Contrato para una transformación del árbol de información de archivos.
 *
 * <p>
 * Cada implementación define un modo de agrupación/vista alternativa del árbol
 * (p. ej. por extensión, acumulado). Un modo puede declarar
 * {@link #requiredPredecessors()} para indicar qué otras transformaciones deben
 * aplicarse <em>antes</em> que ésta. El {@link TransformationPipeline} respeta
 * ese orden automáticamente.</p>
 *
 * <p>
 * Para añadir un nuevo modo:
 * <ol>
 * <li>Implementa esta interfaz con un {@code ID} estático único.</li>
 * <li>Regístrala en el pipeline:
 * {@code pipeline.register(new MyTransformation())}.</li>
 * <li>Declara prerequisitos si los hay sobreescribiendo
 * {@link #requiredPredecessors()}.</li>
 * </ol>
 * No es necesario modificar ningún otro fichero existente.</p>
 *
 * @see TransformationPipeline
 */
public interface TreeTransformation {

    /**
     * Identificador único de esta transformación (p. ej.
     * {@code "FILE_EXTENSION"}).
     */
    String getId();

    /**
     * Aplica la transformación al árbol dado y devuelve un nuevo árbol
     * transformado. El árbol de entrada no debe modificarse.
     *
     * @param tree árbol a transformar
     * @return nuevo árbol transformado; nunca {@code null} si la entrada no es
     * nula
     */
    MultiTree<Info> apply(MultiTree<Info> tree);

    /**
     * IDs de transformaciones que deben estar activas y aplicarse antes que
     * ésta. El pipeline gestiona el orden automáticamente.
     *
     * @return conjunto de IDs prerequisito (puede estar vacío)
     */
    default Set<String> requiredPredecessors() {
        return Set.of();
    }
}
