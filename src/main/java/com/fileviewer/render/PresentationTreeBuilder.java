package com.fileviewer.render;

import java.util.Objects;

import com.fileviewer.domain.interaction.NodeInteractionPolicy;
import com.fileviewer.domain.model.DirectoryInfo;
import com.fileviewer.domain.model.Info;
import com.fileviewer.tdas.trees.MultiTree;
import com.fileviewer.view.PresentationNode;
import com.fileviewer.view.PresentationNodeFactory;
import com.fileviewer.view.nodes.DirectoryPresentationNode;

import javafx.scene.layout.Pane;

/**
 * Construye un árbol de {@link PresentationNode} a partir de un
 * {@link MultiTree}&lt;Info&gt;. Contiene la lógica recursiva de inicialización
 * y creación de nodos, sin tocar el scene graph.
 */
public class PresentationTreeBuilder {

    private MultiTree<Info> tree;
    private Pane paneRoot;
    private RenderConfiguration config;
    private NodeInteractionPolicy policy;

    public PresentationTreeBuilder() {
    }

    public PresentationTreeBuilder tree(MultiTree<Info> tree) {
        this.tree = tree;
        return this;
    }

    public PresentationTreeBuilder paneRoot(Pane paneRoot) {
        this.paneRoot = paneRoot;
        return this;
    }

    public PresentationTreeBuilder config(RenderConfiguration config) {
        this.config = config;
        return this;
    }

    public PresentationTreeBuilder policy(NodeInteractionPolicy policy) {
        this.policy = policy;
        return this;
    }

    /**
     * Construye el árbol de presentación inicializado. Calcula el totalSize a
     * partir del `tree` proporcionado.
     */
    public MultiTree<PresentationNode> build() {
        Objects.requireNonNull(tree, "tree cannot be null");
        Objects.requireNonNull(config, "config cannot be null");
        Objects.requireNonNull(paneRoot, "paneRoot cannot be null");

        if (config.getLimitLevel() < 0) {
            return null;
        }

        long totalSize = tree.getRoot().getContent().getSize();
        return buildNode(tree, paneRoot, totalSize, config);
    }

    private MultiTree<PresentationNode> buildNode(MultiTree<Info> tree, Pane paneRoot,
            long sizeParent, RenderConfiguration config) {

        Info info = tree.getRoot().getContent();
        PresentationNode presentationNode = PresentationNodeFactory.create(info, config, policy);
        presentationNode.initializeNode(sizeParent, paneRoot, config.isVerticalStart());
        presentationNode.createNode(true);

        MultiTree<PresentationNode> presentationTree = new MultiTree<>(presentationNode);

        if (info instanceof DirectoryInfo directoryInfo && config.getLimitLevel() > 0) {
            DirectoryPresentationNode dirNode = (DirectoryPresentationNode) presentationNode;
            Pane childPane = dirNode.getChildTreePane();
            RenderConfiguration childConfig = config.createChildConfiguration();

            for (MultiTree<Info> subtree : tree.getRoot().getChildren()) {
                MultiTree<PresentationNode> childTree = buildNode(subtree, childPane, directoryInfo.getSize(), childConfig);
                if (childTree != null) {
                    presentationTree.addChild(childTree);
                }
            }
        }
        return presentationTree;
    }
}
