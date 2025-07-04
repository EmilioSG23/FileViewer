package com.emiliosg23;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.emiliosg23.utils.Consts;

import javafx.scene.image.Image;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;

    public static Scene getScene() {
        return scene;
    }

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("app"), 1360, 768);
        stage.setScene(scene);
				stage.setMaximized(true);
				stage.setTitle(Consts.PROGRAM_NAME);
				stage.getIcons().add(new Image(App.class.getResource("imagenes/fileviewer.png").toString()));
        stage.show();
        App.stage=stage;
    }

    static void setRoot(String fxml,int anc,int alt) throws IOException {
        scene.setRoot(loadFXML(fxml));
        stage.setWidth(anc);
        stage.setHeight(alt);
        stage.centerOnScreen();
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}