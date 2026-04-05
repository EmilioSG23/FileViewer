package com.fileviewer.utils;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class UiUtils {

    /**
     * Fija las tres propiedades de tamaño (pref, min, max) de un panel.
     *
     * @param pane panel a dimensionar
     * @param width ancho en píxeles
     * @param height alto en píxeles
     */
    public static void setSize(Pane pane, double width, double height) {
        setWidth(pane, width);
        setHeight(pane, height);
    }

    /**
     * Fija pref/min/max width de un panel.
     *
     * @param pane panel a dimensionar
     * @param width ancho en píxeles
     */
    public static void setWidth(Pane pane, double width) {
        pane.setPrefWidth(width);
        pane.setMinWidth(width);
        pane.setMaxWidth(width);
    }

    /**
     * Fija pref/min/max height de un panel.
     *
     * @param pane panel a dimensionar
     * @param height alto en píxeles
     */
    public static void setHeight(Pane pane, double height) {
        pane.setPrefHeight(height);
        pane.setMinHeight(height);
        pane.setMaxHeight(height);
    }

    /**
     * Cambia el estilo visual de un botón según su estado.
     *
     * @param button botón a modificar
     * @param isActive {@code true} para estilo activo, {@code false} para
     * inactivo
     */
    public static void changeButtonState(Button button, boolean isActive) {
        if (isActive) {
            changeButtonToRight(button);
        } else {
            changeButtonToWrong(button);
        }
    }

    private static void changeButtonToWrong(Button b) {
        b.getStyleClass().remove("right-state");
        b.getStyleClass().add("wrong-state");
    }

    private static void changeButtonToRight(Button b) {
        b.getStyleClass().remove("wrong-state");
        b.getStyleClass().add("right-state");
    }

}
