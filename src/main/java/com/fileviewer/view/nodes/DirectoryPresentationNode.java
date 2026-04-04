package com.fileviewer.view.nodes;

import com.fileviewer.domain.model.DirectoryInfo;
import com.fileviewer.utils.AppUtils;
import com.fileviewer.utils.FileExtensionUtils;
import com.fileviewer.view.PresentationNode;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Nodo de presentación para directorios.
 *
 * <p>
 * Muestra un título con el nombre y tamaño del directorio, y contiene un panel
 * hijo donde se renderizan los elementos contenidos. En el último nivel
 * visible, se muestra solo el nombre sin hijos expandidos.</p>
 *
 * @see PresentationNode
 * @see DirectoryInfo
 */
public class DirectoryPresentationNode extends PresentationNode {

    private static final int MIN_TITLE_SIZE = 30;
    private static final int MIN_LAST_LEVEL_SIZE = 10;
    private static final int TITLE_HEIGHT_OFFSET = 10;

    private VBox title;
    private Pane childTreePane;
    private final boolean showTitle;
    private final boolean lastLevel;

    /**
     * Crea un nodo de presentación de directorio.
     *
     * @param info información del directorio
     * @param showTitle {@code true} si se debe mostrar el título
     * @param lastLevel {@code true} si es el último nivel visible de
     * profundidad
     */
    public DirectoryPresentationNode(DirectoryInfo info, boolean showTitle, boolean lastLevel) {
        super(info);
        this.showTitle = showTitle;
        this.lastLevel = lastLevel;
    }

    /**
     * @return panel de título del directorio
     */
    public VBox getTitle() {
        return title;
    }

    /**
     * @return panel contenedor de los nodos hijos
     */
    public Pane getChildTreePane() {
        return childTreePane;
    }

    /**
     * Crea el panel de título con el nombre y tamaño del directorio.
     */
    public void createTitle() {
        title = new VBox();
        Label label = new Label(getInfo().toString());

        title.getStyleClass().add("title-box");
        label.getStyleClass().add("title-label");

        title.getChildren().add(label);
    }

    @Override
    public VBox createNode(boolean showSize) {
        VBox rootPane = getTreePane();

        rootPane.setAlignment(Pos.CENTER);

        if (lastLevel) {
            if (rootPane.getPrefWidth() > MIN_LAST_LEVEL_SIZE && rootPane.getPrefHeight() > MIN_LAST_LEVEL_SIZE) {
                Label filenameLabel = new Label(getInfo().getName());
                filenameLabel.getStyleClass().add("title-file");
                rootPane.getChildren().add(filenameLabel);
            }
            rootPane.setStyle("-fx-background-color:" + FileExtensionUtils.getColor("") + ";");

            setTreePane(rootPane);
            return rootPane;
        }

        if (showTitle && rootPane.getPrefWidth() > MIN_TITLE_SIZE && rootPane.getPrefHeight() > MIN_TITLE_SIZE) {
            createTitle();
            rootPane.getChildren().add(title);
        }
        if (isVertical()) {
            childTreePane = new VBox(); 
        }else {
            childTreePane = new HBox();
        }

        if (title != null) {
            AppUtils.setHeight(childTreePane, rootPane.getPrefHeight() - TITLE_HEIGHT_OFFSET); 
        }else {
            AppUtils.setHeight(childTreePane, rootPane.getPrefHeight());
        }

        AppUtils.setWidth(childTreePane, rootPane.getPrefWidth());

        rootPane.getChildren().add(childTreePane);
        setTreePane(rootPane);
        return rootPane;
    }
}
