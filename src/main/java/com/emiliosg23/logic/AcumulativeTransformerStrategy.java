package com.emiliosg23.logic;

import com.emiliosg23.models.infos.ExtensionInfo;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.tdas.lists.List;
import com.emiliosg23.tdas.trees.MultiTree;

/**
 * Estrategia de transformación acumulativa.
 *
 * <p>
 * Consolida todas las extensiones del árbol en un único nivel, sumando los
 * tamaños de extensiones repetidas. <strong>Requiere que el árbol ya haya sido
 * transformado con {@link FileExtensionTransformerStrategy}</strong>.</p>
 *
 * @see FileExtensionTransformerStrategy
 * @see TreeTransformerStrategy
 */
public class AcumulativeTransformerStrategy implements TreeTransformerStrategy {

    /**
     * Transforma el árbol agrupando todas las extensiones en hijos directos de
     * la raíz, acumulando sus tamaños.
     *
     * @param tree árbol previamente transformado por extensión
     * @return nuevo árbol con extensiones consolidadas en un nivel
     */
    @Override
    public MultiTree<Info> transform(MultiTree<Info> tree) {
        if (tree == null) {
            return null;
        }

        List<Info> nodes = tree.traverseTree();
        MultiTree<Info> transformedTree = new MultiTree<>(tree.getRoot().getContent());

        for (Info node : nodes) {
            if (!(node instanceof ExtensionInfo)) {
                continue;
            }

            ExtensionInfo extNode = (ExtensionInfo) node;
            String ext = extNode.getName();
            boolean found = false;

            for (MultiTree<Info> child : transformedTree.getRoot().getChildren()) {
                Info childInfo = child.getRoot().getContent();

                if (childInfo instanceof ExtensionInfo) {
                    ExtensionInfo existing = (ExtensionInfo) childInfo;
                    if (existing.getName().equals(ext)) {
                        existing.setSize(existing.getSize() + extNode.getSize());
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                transformedTree.addChild(new ExtensionInfo(ext, extNode.getSize()));
            }
        }

        return transformedTree;
    }
}
