package com.fileviewer.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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

}
