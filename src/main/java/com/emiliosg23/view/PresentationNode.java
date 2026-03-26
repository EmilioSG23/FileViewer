package com.emiliosg23.view;

import com.emiliosg23.models.infos.Info;
import com.emiliosg23.utils.AppUtils;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Clase base abstracta para los nodos visuales del treemap.
 *
 * <p>
 * Cada nodo encapsula un {@link Info} y un {@link VBox} cuyas dimensiones se
 * calculan proporcionalmente al tamaño del elemento respecto a su padre. Las
 * subclases concretas
 * ({@link DirectoryPresentationNode}, {@link FilePresentationNode}, {@link ExtensionPresentationNode})
 * definen cómo se construye visualmente cada tipo de nodo.</p>
 *
 * @see DirectoryPresentationNode
 * @see FilePresentationNode
 * @see ExtensionPresentationNode
 */
public abstract class PresentationNode {

    private final Info info;
    private VBox treePane;
    private boolean vertical;

    /**
     * Crea un nodo de presentación para la información dada.
     *
     * @param info información del elemento (archivo, directorio o extensión)
     */
    public PresentationNode(Info info) {
        this.info = info;
        this.treePane = new VBox();
        this.vertical = false;
    }

    /**
     * @return panel visual de este nodo
     */
    public VBox getTreePane() {
        return treePane;
    }

    /**
     * @param treePane nuevo panel visual
     */
    public void setTreePane(VBox treePane) {
        this.treePane = treePane;
    }

    /**
     * @return información del elemento representado
     */
    public Info getInfo() {
        return this.info;
    }

    /**
     * @return {@code true} si la orientación actual es vertical
     */
    public boolean isVertical() {
        return this.vertical;
    }

    /**
     * Calcula y aplica las dimensiones del nodo según la proporción de su
     * tamaño respecto al tamaño total del padre.
     *
     * @param sizeParent tamaño del padre en bytes
     * @param parentPane contenedor padre (para obtener dimensiones de
     * referencia)
     * @param vertical {@code true} si el layout es vertical en este nivel
     */
    public void initializeNode(long sizeParent, Pane parentPane, boolean vertical) {
        this.vertical = vertical;
        double percent = (double) info.getSize() / sizeParent;
        double width, height;

        if (!vertical) {
            width = parentPane.getPrefWidth();
            height = parentPane.getPrefHeight() * percent;
        } else {
            width = parentPane.getPrefWidth() * percent;
            height = parentPane.getPrefHeight();
        }

        AppUtils.setWidth(treePane, width);
        AppUtils.setHeight(treePane, height);
    }

    /**
     * Construye el contenido visual del nodo (etiquetas, colores, estilos).
     *
     * @param showSize {@code true} para mostrar el tamaño en la etiqueta
     * @return panel visual construido
     */
    public abstract VBox createNode(boolean showSize);
}
