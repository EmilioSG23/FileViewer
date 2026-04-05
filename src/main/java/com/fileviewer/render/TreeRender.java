package com.fileviewer.render;

import java.util.Objects;

import com.fileviewer.domain.interaction.InteractionOptions;
import com.fileviewer.domain.model.Info;
import com.fileviewer.tdas.trees.MultiTree;
import com.fileviewer.view.PresentationNode;

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
     * @param interactionOptions opciones de interacción; {@code null} se trata
     * como {@link InteractionOptions#none()}
     */
    public TreeRender(MultiTree<Info> tree, RenderConfiguration config, InteractionOptions interactionOptions) {
        this.tree = Objects.requireNonNull(tree, "tree cannot be null");
        this.config = Objects.requireNonNull(config, "config cannot be null");
        this.interactionOptions = interactionOptions != null ? interactionOptions : InteractionOptions.none();
    }

    /**
     * Crea un renderizador vacío. Debe configurarse con setters antes de usar.
     */
    public TreeRender() {
    }

    /**
     */
    public void setTree(MultiTree<Info> tree) {
        this.tree = tree;
    }

    /**
     */
    public void setConfig(RenderConfiguration config) {
        this.config = config;
    }

    /**
     */
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
        PresentationTreeBuilder builder = new PresentationTreeBuilder();
        return builder.tree(tree)
                .paneRoot(paneRoot)
                .config(config)
                .policy(interactionOptions.getPolicy())
                .build();
    }

    /**
     * Inserta los nodos de presentación en el scene graph de JavaFX,
     * recorriendo el árbol recursivamente.
     *
     * @param presentationTree árbol de nodos de presentación
     * @param paneRoot panel donde se insertan los nodos
     */
    public void render(MultiTree<PresentationNode> presentationTree, Pane paneRoot) {
        SceneGraphRenderer renderer = new SceneGraphRenderer();
        renderer.render(presentationTree, paneRoot);
    }

}
