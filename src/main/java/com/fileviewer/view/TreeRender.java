package com.fileviewer.view;

import java.util.Objects;

import com.fileviewer.application.RenderConfiguration;
import com.fileviewer.domain.interaction.InteractionOptions;
import com.fileviewer.domain.model.DirectoryInfo;
import com.fileviewer.domain.model.Info;
import com.fileviewer.tdas.trees.MultiTree;
import com.fileviewer.view.nodes.DirectoryPresentationNode;

import javafx.scene.layout.Pane;

/**
 * Motor de renderizado del treemap.
 *
 * <p>
 * Transforma un árbol de {@link Info} en un árbol de {@link PresentationNode}
 * (fase de inicialización) y luego inserta los nodos JavaFX en el panel visual
 * (fase de renderizado). Ambas fases deben ejecutarse en el hilo de JavaFX.</p>
 *
 * <h3>Flujo de uso:</h3>
 * <ol>
 * <li>Constructor con tree, config e interactionOptions</li>
 * <li>{@link #initialize(Pane)} — crea los nodos de presentación</li>
 * <li>{@link #render(MultiTree, Pane)} — inserta los nodos en el scene
 * graph</li>
 * </ol>
 *
 * <p>
 * Las opciones de interacción ({@link InteractionOptions}) controlan qué ocurre
 * al hacer clic en un nodo archivo; el render no necesita conocer los detalles
 * de la política concreta.</p>
 *
 * @see PresentationNode
 * @see RenderConfiguration
 * @see InteractionOptions
 */
public class TreeRender {

    private MultiTree<Info> tree;
    private RenderConfiguration config;
    private InteractionOptions interactionOptions = InteractionOptions.none();

    /**
     * Crea un renderizador con los parámetros requeridos.
     *
     * @param tree árbol de información a renderizar
     * @param config configuración de renderizado
     * @param interactionOptions opciones de interacción; {@code null} se trata como {@link InteractionOptions#none()}
     */
    public TreeRender(MultiTree<Info> tree, RenderConfiguration config, InteractionOptions interactionOptions) {
        this.tree = Objects.requireNonNull(tree, "tree cannot be null");
        this.config = Objects.requireNonNull(config, "config cannot be null");
        this.interactionOptions = interactionOptions != null ? interactionOptions : InteractionOptions.none();
    }

    /**
     * Crea un renderizador vacío. Debe configurarse con setters antes de usar.
     * @deprecated Use {@link #TreeRender(MultiTree, RenderConfiguration, InteractionOptions)}
     */
    @Deprecated
    public TreeRender() {
    }

    /**
     * @deprecated Pass tree in constructor
     */
    @Deprecated
    public void setTree(MultiTree<Info> tree) {
        this.tree = tree;
    }

    /**
     * @deprecated Pass config in constructor
     */
    @Deprecated
    public void setConfig(RenderConfiguration config) {
        this.config = config;
    }

    /**
     * @deprecated Pass interactionOptions in constructor
     */
    @Deprecated
    public void setInteractionOptions(InteractionOptions interactionOptions) {
        this.interactionOptions = interactionOptions != null
                ? interactionOptions
                : InteractionOptions.none();
    }

    /**
     * Inicializa el árbol de presentación: crea nodos visuales con dimensiones
     * proporcionales al tamaño de cada elemento.
     *
     * @param paneRoot panel raíz donde se calcularán las proporciones
     * @return árbol de nodos de presentación listo para renderizar
     */
    public MultiTree<PresentationNode> initialize(Pane paneRoot) {
        paneRoot.getChildren().clear();
        long totalSize = tree.getRoot().getContent().getSize();
        return initializeRecursive(tree, paneRoot, totalSize, config);
    }

    /**
     * Inserta los nodos de presentación en el scene graph de JavaFX,
     * recorriendo el árbol recursivamente.
     *
     * @param presentationTree árbol de nodos de presentación
     * @param paneRoot panel donde se insertan los nodos
     */
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

    // -------------------------------------------------------------------------
    // Inicialización recursiva
    // -------------------------------------------------------------------------
    private MultiTree<PresentationNode> initializeRecursive(
            MultiTree<Info> tree, Pane paneRoot, long sizeParent, RenderConfiguration config) {

        if (config.getLimitLevel() < 0) {
            return null;
        }

        Info info = tree.getRoot().getContent();
        PresentationNode presentationNode = PresentationNodeFactory.create(info, config,
                interactionOptions.getPolicy());
        presentationNode.initializeNode(sizeParent, paneRoot, config.isVerticalStart());
        presentationNode.createNode(true);

        MultiTree<PresentationNode> presentationTree = new MultiTree<>(presentationNode);

        if (info instanceof DirectoryInfo directoryInfo && config.getLimitLevel() > 0) {
            DirectoryPresentationNode dirNode = (DirectoryPresentationNode) presentationNode;
            Pane childPane = dirNode.getChildTreePane();
            RenderConfiguration childConfig = config.createChildConfiguration();

            for (MultiTree<Info> subtree : tree.getRoot().getChildren()) {
                MultiTree<PresentationNode> childTree = initializeRecursive(
                        subtree, childPane, directoryInfo.getSize(), childConfig);
                if (childTree != null) {
                    presentationTree.addChild(childTree);
                }
            }
        }
        return presentationTree;
    }
}
