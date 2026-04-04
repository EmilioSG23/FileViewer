package com.fileviewer.infrastructure.interaction;

import java.io.File;
import java.io.IOException;

import com.fileviewer.domain.interaction.NodeInteractionPolicy;
import com.fileviewer.domain.model.FileInfo;
import com.fileviewer.domain.model.Info;
import com.fileviewer.utils.AppUtils;
import com.fileviewer.utils.FileOpener;

/**
 * Política de interacción que abre el archivo con la aplicación predeterminada
 * del sistema operativo al hacer clic en su nodo del treemap.
 *
 * <p>
 * Singleton inmutable aconsejado para evitar instancias redundantes.</p>
 *
 * @see NodeInteractionPolicy
 * @see FileOpener
 */
public class OpenFileInteractionPolicy implements NodeInteractionPolicy {

    /**
     * Instancia singleton.
     */
    public static final OpenFileInteractionPolicy INSTANCE = new OpenFileInteractionPolicy();

    private OpenFileInteractionPolicy() {
    }

    @Override
    public void onNodeClicked(Info info) {
        if (!(info instanceof FileInfo file)) {
            return;
        }
        try {
            FileOpener.openFile(new File(file.getFullPath()));
        } catch (IOException e) {
            AppUtils.showErrorAlert("The file could not be executed.");
        }
    }
}
