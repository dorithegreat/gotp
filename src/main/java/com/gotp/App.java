package com.gotp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * main class used mostly for GUI testing purposes.
 * will eventually be deleted
 */
public class App extends Application {

    /**
     * test.
     */
    private static Scene scene;

    // private final int SCENE_WIDTH = 640;
    // private final int SCENE_HEIGHT = 480;


    /**
     * standard javafx start method.
     * Currently starts the "testScene" scene
     */
    @Override
    public void start(final Stage stage) throws IOException {
        scene = new Scene(loadFXML("GUIcontrollers/testScene"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(final String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(final String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("testScene.fxml"));
        return fxmlLoader.load();
    }

    /**
     * Program starts executing here.
     * @param args
     */
    public static void main(final String[] args) {
        // launch();
    }

}
