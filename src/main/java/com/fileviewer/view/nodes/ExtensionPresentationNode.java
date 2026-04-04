package com.fileviewer.view.nodes;

import com.fileviewer.domain.model.ExtensionInfo;
import com.fileviewer.utils.FileExtensionUtils;
import com.fileviewer.view.PresentationNode;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Nodo de presentación para extensiones agrupadas.
 *
 * <p>
 * Muestra la extensión (y opcionalmente su tamaño acumulado) con un color de
 * fondo basado en el tipo de extensión.</p>
 *
 * @see PresentationNode
 * @see ExtensionInfo
 */
public class ExtensionPresentationNode extends PresentationNode {

    /**
     * Crea un nodo de presentación de extensión.
     *
     * @param info información de la extensión agrupada
     */
    public ExtensionPresentationNode(ExtensionInfo info) {
        super(info);
    }

    @Override
    public VBox createNode(boolean showSize) {
        VBox rootPane = getTreePane();
        rootPane.setAlignment(Pos.CENTER);

        if (rootPane.getPrefWidth() > MIN_NODE_SIZE && rootPane.getPrefHeight() > MIN_NODE_SIZE) {
            Label extensionLabel = new Label(showSize ? getInfo().toString() : getInfo().getName());
            extensionLabel.getStyleClass().add("title-file");
            rootPane.getChildren().add(extensionLabel);
        }

        rootPane.setStyle("-fx-background-color:" + FileExtensionUtils.getColor(getInfo().getName()) + ";");

        setTreePane(rootPane);
        return rootPane;
    }
}
