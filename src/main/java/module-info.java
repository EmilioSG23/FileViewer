/**
 * Modulo principal de FileViewer.
 *
 * <p>Aplicacion JavaFX para visualizar la estructura de directorios como treemaps
 * interactivos, con modos de agrupacion por extension, vista acumulativa y apertura
 * de archivos.</p>
 */
module com.emiliosg23 {
    requires javafx.controls;
    requires javafx.fxml;
		requires java.desktop;
		requires java.prefs;
		requires transitive javafx.graphics;

    opens com.emiliosg23 to javafx.fxml;
    exports com.emiliosg23;
    opens com.emiliosg23.controllers to javafx.fxml;
		exports com.emiliosg23.controllers;
}
