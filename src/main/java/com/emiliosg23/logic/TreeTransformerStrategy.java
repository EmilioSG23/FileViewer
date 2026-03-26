package com.emiliosg23.logic;

import com.emiliosg23.models.infos.Info;
import com.emiliosg23.tdas.trees.MultiTree;

/**
 * Estrategia de transformación de árboles de información.
 *
 * <p>
 * Define el contrato para transformar un {@link MultiTree} de {@link Info} en
 * una representación alternativa (por extensión, acumulativa, etc.). Las
 * implementaciones deben producir un nuevo árbol sin modificar el original.</p>
 *
 * @see AcumulativeTransformerStrategy
 * @see FileExtensionTransformerStrategy
 */
public interface TreeTransformerStrategy {

    /**
     * Transforma el árbol dado en una nueva representación.
     *
     * @param tree árbol original (no debe modificarse)
     * @return nuevo árbol transformado, o {@code null} si la entrada es
     * {@code null}
     */
    MultiTree<Info> transform(MultiTree<Info> tree);
}
