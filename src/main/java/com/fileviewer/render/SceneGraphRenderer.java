package com.fileviewer.render;

import com.fileviewer.tdas.trees.MultiTree;
import com.fileviewer.view.PresentationNode;
import com.fileviewer.view.nodes.DirectoryPresentationNode;

import javafx.scene.layout.Pane;

/**
 * Inserta un árbol de {@link PresentationNode} en el scene graph de JavaFX.
 */
public class SceneGraphRenderer {

    public void render(MultiTree<PresentationNode> presentationTree, Pane paneRoot) {
        PresentationNode node = presentationTree.getRoot().getContent();
        paneRoot.getChildren().add(node.getTreePane());

        if (node instanceof DirectoryPresentationNode dirNode) {
            Pane childPane = dirNode.getChildTreePane();
            for (MultiTree<PresentationNode> subtree : presentationTree.getRoot().getChildren()) {
                render(subtree, childPane);
            }
        }
    }
}
