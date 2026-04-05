package com.fileviewer;

import java.io.IOException;

import com.fileviewer.application.AppService;
import com.fileviewer.application.Consts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación JavaFX FileViewer.
 *
 * <p>
 * Inicializa la escena principal, aplica el ícono y título de la ventana, y
 * proporciona métodos estáticos para cargar archivos FXML y cambiar
 * escenas.</p>
 */
public class App extends Application {

    private static final int DEFAULT_WIDTH = 1360;
    private static final int DEFAULT_HEIGHT = 768;

    private static AppService appService;

    /**
     * Establece la referencia al servicio de la aplicación para su ciclo de
     * vida.
     *
     * @param service instancia del servicio
     */
    public static void setService(@SuppressWarnings("exports") AppService service) {
        App.appService = service;
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(loadFXML("app"), DEFAULT_WIDTH, DEFAULT_HEIGHT);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle(Consts.PROGRAM_NAME);
        stage.getIcons().add(new Image(App.class.getResource("imagenes/fileviewer.png").toString()));
        stage.show();
    }

    /**
     * Cambia la raíz de la escena y redimensiona la ventana.
     *
     * @param fxml nombre del archivo FXML (sin extensión)
     * @param anc nuevo ancho de la ventana
     * @param alt nuevo alto de la ventana
     * @throws IOException si el FXML no se puede cargar
     */
    @SuppressWarnings("unused")
    static void setRoot(Scene scene, Stage stage, String fxml, int anc, int alt) throws IOException {
        scene.setRoot(loadFXML(fxml));
        stage.setWidth(anc);
        stage.setHeight(alt);
        stage.centerOnScreen();
    }

    /**
     * Carga un archivo FXML como nodo raíz.
     *
     * @param fxml nombre del archivo FXML (sin extensión)
     * @return nodo raíz cargado
     * @throws IOException si el archivo no se encuentra o no se puede parsear
     */
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() {
        if (appService != null) {
            appService.shutdown();
        }
    }
}
