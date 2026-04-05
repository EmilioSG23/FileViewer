package com.fileviewer.view.nodes;

import com.fileviewer.domain.interaction.NodeInteractionPolicy;
import com.fileviewer.domain.model.FileInfo;
import com.fileviewer.utils.FileExtensionUtils;
import com.fileviewer.view.PresentationNode;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Nodo de presentación para archivos individuales.
 *
 * <p>
 * Muestra el nombre del archivo (si está habilitado) y aplica un color de fondo
 * según su extensión. La acción al hacer clic está delegada a una
 * {@link NodeInteractionPolicy} inyectable, desacoplando el comportamiento de
 * las decisiones de render.</p>
 *
 * <ul>
 * <li>Sin interacción: usar {@link NodeInteractionPolicy#NO_OP}.</li>
 * <li>Modo ejecutable: usar
 * {@link com.fileviewer.infrastructure.interaction.OpenFileInteractionPolicy}.</li>
 * <li>Futuras acciones: implementar {@link NodeInteractionPolicy} e
 * inyectar.</li>
 * </ul>
 *
 * @see PresentationNode
 * @see FileInfo
 * @see NodeInteractionPolicy
 */
public class FilePresentationNode extends PresentationNode {

    private static final int MIN_SIZE = MIN_NODE_SIZE;

    private final boolean showFilename;
    private final NodeInteractionPolicy interactionPolicy;

    /**
     * Crea un nodo de presentación de archivo.
     *
     * @param info información del archivo
     * @param showFilename {@code true} para mostrar el nombre en el nodo
     * @param interactionPolicy política de clic; usa
     * {@link NodeInteractionPolicy#NO_OP} para deshabilitar la interacción
     */
    public FilePresentationNode(FileInfo info, boolean showFilename,
            NodeInteractionPolicy interactionPolicy) {
        super(info);
        this.showFilename = showFilename;
        this.interactionPolicy = interactionPolicy;
    }

    public boolean isShowFilename() {
        return showFilename;
    }

    @Override
    public VBox createNode(boolean showSize) {
        VBox rootPane = getTreePane();
        rootPane.setAlignment(Pos.CENTER);

        if (rootPane.getPrefWidth() > MIN_SIZE && rootPane.getPrefHeight() > MIN_SIZE) {
            Label filenameLabel = new Label(showFilename ? getInfo().getName() : "");
            filenameLabel.getStyleClass().add("title-file");
            rootPane.getChildren().add(filenameLabel);

            // La política decide qué ocurre al hacer clic; NO_OP no hace nada.
            final NodeInteractionPolicy policy = interactionPolicy;
            rootPane.setOnMouseClicked(event -> policy.onNodeClicked(getInfo()));
        }

        String extension = ((FileInfo) getInfo()).getExtension();
        rootPane.setStyle("-fx-background-color:" + FileExtensionUtils.getColor(extension) + ";");

        setTreePane(rootPane);
        return rootPane;
    }
}
