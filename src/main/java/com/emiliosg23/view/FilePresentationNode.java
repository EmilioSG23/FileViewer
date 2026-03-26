package com.emiliosg23.view;

import java.io.File;
import java.io.IOException;

import com.emiliosg23.models.infos.FileInfo;
import com.emiliosg23.utils.AppUtils;
import com.emiliosg23.utils.FileExtensionUtils;
import com.emiliosg23.utils.FileOpener;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Nodo de presentación para archivos individuales.
 *
 * <p>
 * Muestra el nombre del archivo (si está habilitado) y aplica un color de fondo
 * según su extensión. En modo ejecutable, al hacer clic se abre el archivo.</p>
 *
 * @see PresentationNode
 * @see FileInfo
 */
public class FilePresentationNode extends PresentationNode {

    private final boolean showFilename;
    private final boolean executableMode;

    /**
     * Crea un nodo de presentación de archivo.
     *
     * @param info información del archivo
     * @param showFilename {@code true} para mostrar el nombre del archivo
     * @param executableMode {@code true} para habilitar apertura al hacer clic
     */
    public FilePresentationNode(FileInfo info, boolean showFilename, boolean executableMode) {
        super(info);
        this.showFilename = showFilename;
        this.executableMode = executableMode;
    }

    public boolean showFilename() {
        return this.showFilename;
    }

    public boolean isExecutableMode() {
        return this.executableMode;
    }

    @Override
    public VBox createNode(boolean showSize) {
        final int MIN_SIZE = 10;
        VBox rootPane = getTreePane();
        rootPane.setAlignment(Pos.CENTER);

        if (rootPane.getPrefWidth() > MIN_SIZE && rootPane.getPrefHeight() > MIN_SIZE) {
            Label filenameLabel = new Label(showFilename ? getInfo().getName() : "");
            filenameLabel.getStyleClass().add("title-file");
            rootPane.getChildren().add(filenameLabel);

            // Habilitar executable mode: ejecutar archivo al hacer clic en el nodo
            if (executableMode) {
                rootPane.setOnMouseClicked(event -> {
                    try {
                        FileInfo fi = (FileInfo) getInfo();
                        File file = new File(fi.getFullPath());
                        FileOpener.openFile(file);
                    } catch (IOException e) {
                        AppUtils.showErrorAlert("The file could not executed.");
                    }
                });
            }
        }

        String extension = ((FileInfo) getInfo()).getExtension();
        rootPane.setStyle("-fx-background-color:" + FileExtensionUtils.getColor(extension) + ";");

        setTreePane(rootPane);
        return rootPane;
    }
}
