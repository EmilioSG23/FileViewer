package com.emiliosg23.view;

import com.emiliosg23.models.RenderConfiguration;
import com.emiliosg23.models.infos.DirectoryInfo;
import com.emiliosg23.models.infos.ExtensionInfo;
import com.emiliosg23.models.infos.FileInfo;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.tdas.trees.MultiTree;

import javafx.scene.layout.Pane;

/**
 * Motor de renderizado del treemap.
 *
 * <p>
 * Transforma un árbol de {@link Info} en un árbol de {@link PresentationNode}
 * (fase de inicialización) y luego inserta los nodos JavaFX en el panel visual
 * (fase de renderizado). Ambas fases deben ejecutarse en el hilo de JavaFX.</p>
 *
 * <h3>Flujo:</h3>
 * <ol>
 * <li>{@link #setTree(MultiTree)} y
 * {@link #setConfig(RenderConfiguration)}</li>
 * <li>{@link #initialize(Pane)} — crea los nodos de presentación con sus
 * dimensiones</li>
 * <li>{@link #render(MultiTree, Pane)} — inserta los nodos en el scene
 * graph</li>
 * </ol>
 *
 * @see PresentationNode
 * @see RenderConfiguration
 */
public class TreeRender {

    private MultiTree<Info> tree;
    private RenderConfiguration config;

    /**
     * Crea un renderizador con árbol y configuración iniciales.
     *
     * @param tree árbol de información a visualizar
     * @param config configuración de renderizado
     */
    public TreeRender(MultiTree<Info> tree, RenderConfiguration config) {
        this.tree = tree;
        this.config = config;
    }

    /**
     * Crea un renderizador vacío. Debe configurarse con setters antes de usar.
     */
    public TreeRender() {
    }

    /**
     * @param tree árbol de información a renderizar
     */
    public void setTree(MultiTree<Info> tree) {
        this.tree = tree;
    }

    /**
     * @param config configuración de renderizado
     */
    public void setConfig(RenderConfiguration config) {
        this.config = config;
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
        PresentationNode presentationNode = presentationTree.getRoot().getContent();
        paneRoot.getChildren().add(presentationNode.getTreePane());

        if (presentationNode instanceof DirectoryPresentationNode) {
            DirectoryPresentationNode directoryNode = (DirectoryPresentationNode) presentationNode;
            Pane childTreePane = directoryNode.getChildTreePane();

            for (MultiTree<PresentationNode> subtree : presentationTree.getRoot().getChildren()) {
                render(subtree, childTreePane);
            }
        }
    }

    /**
     * Crea recursivamente nodos de presentación para cada nivel del árbol,
     * respetando el límite de profundidad configurado.
     *
     * @param tree subárbol actual
     * @param paneRoot contenedor padre para calcular proporciones
     * @param sizeParent tamaño del padre para calcular el porcentaje relativo
     * @param config configuración de renderizado (con nivel decrementado)
     * @return subárbol de presentación, o {@code null} si se excede la
     * profundidad
     */
    private MultiTree<PresentationNode> initializeRecursive(MultiTree<Info> tree, Pane paneRoot, long sizeParent, RenderConfiguration config) {
        if (config.getLimitLevel() < 0) {
            return null;
        }

        Info info = tree.getRoot().getContent();
        PresentationNode presentationInfoNode = createPresentationNode(info, config);
        presentationInfoNode.initializeNode(sizeParent, paneRoot, config.isVerticalStart());
        presentationInfoNode.createNode(true);

        MultiTree<PresentationNode> presentationTree = new MultiTree<>(presentationInfoNode);

        if (info instanceof DirectoryInfo && config.getLimitLevel() > 0) {
            DirectoryInfo directoryInfo = (DirectoryInfo) info;
            DirectoryPresentationNode directoryNode = (DirectoryPresentationNode) presentationInfoNode;
            Pane childTreePane = directoryNode.getChildTreePane();
            RenderConfiguration childConfig = config.createChildConfiguration();

            for (MultiTree<Info> subtree : tree.getRoot().getChildren()) {
                MultiTree<PresentationNode> childTree = initializeRecursive(subtree, childTreePane, directoryInfo.getSize(), childConfig);
                if (childTree != null) {
                    presentationTree.addChild(childTree);
                }
            }
        }
        return presentationTree;
    }

    /**
     * Crea la instancia de {@link PresentationNode} adecuada según el tipo de
     * {@link Info}.
     *
     * @param info información del nodo
     * @param config configuración de renderizado actual
     * @return nodo de presentación específico
     * @throws IllegalArgumentException si el tipo de info no es soportado
     */
    private PresentationNode createPresentationNode(Info info, RenderConfiguration config) {
        if (info instanceof DirectoryInfo) {
            return new DirectoryPresentationNode((DirectoryInfo) info, config.getLimitLevelTitle() > 0, config.getLimitLevel() == 0); 
        }else if (info instanceof FileInfo) {
            return new FilePresentationNode((FileInfo) info, config.isShowFilenames(), config.isExecutableMode()); 
        }else if (info instanceof ExtensionInfo) {
            return new ExtensionPresentationNode((ExtensionInfo) info); 
        }else {
            throw new IllegalArgumentException("Info must be either DirectoryInfo or FileInfo");
        }
    }
}
