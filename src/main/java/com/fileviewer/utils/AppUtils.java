package com.fileviewer.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;

/**
 * Utilidades de interfaz para JavaFX.
 *
 * <p>
 * Proporciona métodos estáticos para mostrar alertas (error, éxito,
 * confirmación, carga), cambiar el estado visual de botones y dimensionar
 * paneles.</p>
 */
public class AppUtils {

    /**
     * Muestra una alerta de error bloqueante.
     *
     * @param msg mensaje a mostrar
     */
    public static void showErrorAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta informativa bloqueante.
     *
     * @param msg mensaje a mostrar
     */
    public static void showSuccessfulAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta de confirmación y espera la respuesta del usuario.
     *
     * @param msg mensaje a mostrar
     * @return {@code true} si el usuario acepta (OK)
     */
    public static boolean showConfirmationAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msg);
        alert.setHeaderText(null);
        return alert.showAndWait().get() == ButtonType.OK;
    }

    /**
     * Muestra una alerta no bloqueante de carga (loading).
     *
     * @param msg mensaje de estado
     * @return referencia a la alerta para cerrarla cuando termine la operación
     */
    public static Alert showLoadingAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setHeaderText(null);
        alert.show();
        return alert;
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
        }else {
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

    /**
     * Fija las tres propiedades de tamaño (pref, min, max) de un panel.
     *
     * @param pane panel a dimensionar
     * @param width ancho en píxeles
     * @param height alto en píxeles
     */
    public static void setSize(Pane pane, double width, double height) {
        AppUtils.setWidth(pane, width);
        AppUtils.setHeight(pane, height);
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
}
