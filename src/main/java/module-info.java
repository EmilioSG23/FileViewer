/**
 * Modulo principal de FileViewer.
 *
 * <p>Aplicacion JavaFX para visualizar la estructura de directorios como treemaps
 * interactivos, con modos de agrupacion por extension, vista acumulativa y apertura
 * de archivos.</p>
 *
 * <p>Arquitectura por capas:</p>
 * <ul>
 *   <li>{@code domain} — modelo, pipeline de transformaciones, scanner e interacción.</li>
 *   <li>{@code infrastructure} — implementaciones concretas (scanner paralelo,
 *       políticas de interacción, preferencias de UI).</li>
 *   <li>{@code application} — casos de uso: AppService, AppLogic y RenderConfiguration.</li>
 *   <li>{@code view} + {@code controllers} — presentación JavaFX.</li>
 *   <li>{@code tdas} — estructuras de datos propias (árboles y listas).</li>
 * </ul>
 */
module com.fileviewer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.prefs;
    requires transitive javafx.graphics;

    opens com.fileviewer to javafx.fxml;
    exports com.fileviewer;
    opens com.fileviewer.controllers to javafx.fxml;
    exports com.fileviewer.controllers;
}
