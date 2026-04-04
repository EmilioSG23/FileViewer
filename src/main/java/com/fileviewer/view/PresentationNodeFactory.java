package com.fileviewer.view;

import com.fileviewer.application.RenderConfiguration;
import com.fileviewer.domain.interaction.NodeInteractionPolicy;
import com.fileviewer.domain.model.DirectoryInfo;
import com.fileviewer.domain.model.ExtensionInfo;
import com.fileviewer.domain.model.FileInfo;
import com.fileviewer.domain.model.Info;
import com.fileviewer.view.nodes.DirectoryPresentationNode;
import com.fileviewer.view.nodes.ExtensionPresentationNode;
import com.fileviewer.view.nodes.FilePresentationNode;

/**
 * Factory para crear nodos de presentación según el tipo de informaci&oacute;n.
 *
 * <p>
 * Encapsula la l&oacute;gica de creaci&oacute;n de {@link PresentationNode}
 * concretos, eliminando los bloques {@code instanceof} dispersos en el c&oacute;digo.</p>
 */
public class PresentationNodeFactory {

    private PresentationNodeFactory() {
    }

    /**
     * Crea el nodo de presentaci&oacute;n adecuado para el tipo de informaci&oacute;n dado.
     *
     * @param info informaci&oacute;n del elemento
     * @param config configuraci&oacute;n de renderizado
     * @param policy pol&iacute;tica de interacci&oacute;n para nodos de archivo
     * @return nodo de presentaci&oacute;n concreto
     * @throws IllegalArgumentException si el tipo de Info no es reconocido
     */
    public static PresentationNode create(Info info, RenderConfiguration config,
            NodeInteractionPolicy policy) {
        if (info instanceof DirectoryInfo dirInfo) {
            return new DirectoryPresentationNode(dirInfo,
                    config.getLimitLevelTitle() > 0, config.getLimitLevel() == 0);
        }
        if (info instanceof FileInfo fileInfo) {
            return new FilePresentationNode(fileInfo, config.isShowFilenames(), policy);
        }
        if (info instanceof ExtensionInfo extInfo) {
            return new ExtensionPresentationNode(extInfo);
        }
        throw new IllegalArgumentException("Unknown Info subtype: " + info.getClass().getSimpleName());
    }
}
