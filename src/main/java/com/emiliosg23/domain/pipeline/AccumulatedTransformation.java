package com.emiliosg23.domain.pipeline;

import java.util.Set;

import com.emiliosg23.domain.model.ExtensionInfo;
import com.emiliosg23.domain.model.Info;
import com.emiliosg23.tdas.lists.List;
import com.emiliosg23.tdas.trees.MultiTree;

/**
 * Transformación que consolida todas las extensiones del árbol en un único
 * nivel directo bajo la raíz, acumulando los tamaños de extensiones repetidas.
 *
 * <p>
 * <strong>Prerequisito:</strong> {@link FileExtensionTransformation} debe
 * haberse aplicado antes. El {@link TransformationPipeline} lo garantiza
 * automáticamente.</p>
 *
 * @see FileExtensionTransformation
 * @see TransformationPipeline
 */
public class AccumulatedTransformation implements TreeTransformation {

    /**
     * ID estable de esta transformación.
     */
    public static final String ID = "ACCUMULATED";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public Set<String> requiredPredecessors() {
        return Set.of(FileExtensionTransformation.ID);
    }

    @Override
    public MultiTree<Info> apply(MultiTree<Info> tree) {
        if (tree == null) {
            return null;
        }
        List<Info> allNodes = tree.traverseTree();
        MultiTree<Info> result = new MultiTree<>(tree.getRoot().getContent());

        for (Info node : allNodes) {
            if (!(node instanceof ExtensionInfo extNode)) {
                continue;
            }
            String ext = extNode.getName();
            boolean merged = false;

            for (MultiTree<Info> child : result.getRoot().getChildren()) {
                Info childInfo = child.getRoot().getContent();
                if (childInfo instanceof ExtensionInfo existing && existing.getName().equals(ext)) {
                    existing.setSize(existing.getSize() + extNode.getSize());
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                result.addChild(new ExtensionInfo(ext, extNode.getSize()));
            }
        }
        return result;
    }
}
