package com.fileviewer.controllers;

import com.fileviewer.application.AppService;
import com.fileviewer.application.Consts;
import com.fileviewer.domain.pipeline.AccumulatedTransformation;
import com.fileviewer.domain.pipeline.FileExtensionTransformation;
import com.fileviewer.utils.AppUtils;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Gestiona el estado visual de los controles de la interfaz de usuario.
 *
 * <p>
 * Encapsula la sincronización entre el estado del servicio y los widgets de la
 * UI (botones, labels, estados de habilitación).</p>
 */
public class UiStateManager {

    private final Button resetButton;
    private final Button showFileOrExtensionButton;
    private final Button acumulativeButton;
    private final Button executableButton;
    private final Button showFilenamesButton;
    private final Button incrementLevelButton;
    private final Button decrementLevelButton;
    private final Button incrementLevelTitleButton;
    private final Button decrementLevelTitleButton;
    private final Label levelLabel;
    private final Label titleLevelLabel;

    private final AppService service;

    public UiStateManager(@SuppressWarnings("exports") AppService service,
            Button resetButton, Button showFileOrExtensionButton,
            Button acumulativeButton, Button executableButton,
            Button showFilenamesButton, Button incrementLevelButton,
            Button decrementLevelButton, Button incrementLevelTitleButton,
            Button decrementLevelTitleButton, Label levelLabel, Label titleLevelLabel) {
        this.service = service;
        this.resetButton = resetButton;
        this.showFileOrExtensionButton = showFileOrExtensionButton;
        this.acumulativeButton = acumulativeButton;
        this.executableButton = executableButton;
        this.showFilenamesButton = showFilenamesButton;
        this.incrementLevelButton = incrementLevelButton;
        this.decrementLevelButton = decrementLevelButton;
        this.incrementLevelTitleButton = incrementLevelTitleButton;
        this.decrementLevelTitleButton = decrementLevelTitleButton;
        this.levelLabel = levelLabel;
        this.titleLevelLabel = titleLevelLabel;
    }

    /**
     * Sincroniza todos los controles con el estado actual del servicio.
     */
    public void syncWithService() {
        AppUtils.changeButtonState(showFileOrExtensionButton,
                service.isTransformationActive(FileExtensionTransformation.ID));
        AppUtils.changeButtonState(acumulativeButton,
                service.isTransformationActive(AccumulatedTransformation.ID));
        AppUtils.changeButtonState(executableButton, service.isExecutableMode());
        AppUtils.changeButtonState(showFilenamesButton,
                service.getRenderConfiguration().isShowFilenames());

        acumulativeButton.setDisable(
                !service.isTransformationActive(FileExtensionTransformation.ID));

        levelLabel.setText(Integer.toString(service.getRenderConfiguration().getLimitLevel()));
        titleLevelLabel.setText(Integer.toString(service.getRenderConfiguration().getLimitLevelTitle()));
    }

    /**
     * Actualiza los controles tras un reset completo.
     */
    public void resetControls() {
        AppUtils.changeButtonState(showFileOrExtensionButton, false);
        AppUtils.changeButtonState(acumulativeButton, false);
        AppUtils.changeButtonState(executableButton, false);
        AppUtils.changeButtonState(showFilenamesButton,
                service.getRenderConfiguration().isShowFilenames());

        showFileOrExtensionButton.setDisable(false);
        acumulativeButton.setDisable(true);
        showFilenamesButton.setDisable(false);
        executableButton.setDisable(false);

        int level = service.getRenderConfiguration().getLimitLevel();
        int titleLevel = service.getRenderConfiguration().getLimitLevelTitle();
        levelLabel.setText(Integer.toString(level));
        titleLevelLabel.setText(Integer.toString(titleLevel));

        incrementLevelButton.setDisable(level >= Consts.MAX_NUM_LEVEL_LIMIT);
        decrementLevelButton.setDisable(level <= 1);
        incrementLevelTitleButton.setDisable(titleLevel >= Consts.MAX_NUM_TITLE_LEVEL_LIMIT);
        decrementLevelTitleButton.setDisable(titleLevel <= 1);
    }

    /**
     * Actualiza los botones de nivel tras un cambio.
     */
    public void updateLevelButtons(int level, int titleLevel) {
        incrementLevelButton.setDisable(level >= Consts.MAX_NUM_LEVEL_LIMIT);
        decrementLevelButton.setDisable(level <= 1);
        incrementLevelTitleButton.setDisable(titleLevel >= Consts.MAX_NUM_TITLE_LEVEL_LIMIT);
        decrementLevelTitleButton.setDisable(titleLevel <= 1);
    }

    /**
     * Habilita o deshabilita todos los controles.
     */
    public void setControlsDisabled(boolean disabled) {
        resetButton.setDisable(disabled);
        showFileOrExtensionButton.setDisable(disabled);
        executableButton.setDisable(disabled);
        showFilenamesButton.setDisable(disabled);
        incrementLevelButton.setDisable(disabled);
        decrementLevelButton.setDisable(disabled);
        incrementLevelTitleButton.setDisable(disabled);
        decrementLevelTitleButton.setDisable(disabled);
    }

    /**
     * Deshabilita botones de transformación cuando el modo ejecutable está
     * activo.
     */
    public void disableTransformationButtons() {
        AppUtils.changeButtonState(showFileOrExtensionButton, false);
        AppUtils.changeButtonState(acumulativeButton, false);
        showFileOrExtensionButton.setDisable(true);
        acumulativeButton.setDisable(true);
    }

    /**
     * Restaura los botones de transformación cuando el modo ejecutable se
     * desactiva.
     */
    public void enableTransformationButtons() {
        showFileOrExtensionButton.setDisable(false);
        acumulativeButton.setDisable(
                !service.isTransformationActive(FileExtensionTransformation.ID));
    }
}
