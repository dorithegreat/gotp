package com.gotp.GUIcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;

public class TestSceneController {

    /**
     * slider for selecting the size of the board.
     */
    @FXML
    private Slider slider;
    /**
     * default size of the board in case the size is somehow not selected.
     */
    private final int DEFAULT_SIZE = 9;

    /**
     * size of the board that will be created.
     */
    private int size = DEFAULT_SIZE;

    /**
     * controller of the next scene.
     * Is needed so that the size of the board can be set from the current controller
     */
    private BoardController boardController = new BoardController();

    /**
     * Handler for the start button, goes to the board screen and starts the game.
     * @param event
     * @throws IOException //TODO figure out when
     */
    @FXML
    void startGame(final ActionEvent event) throws IOException {
        //a few declarations that will be necessary for changing the scene
        Parent root;
        Stage stage;
        Scene scene;

        //size = (int) slider.getValue();
        //System.out.println(slider.getValue());
        //size = 19;

        FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("board.fxml"));
        root = boardLoader.load();
        BoardController boardController = boardLoader.getController();

        //calls method in the other controller to create a board of certain size
        boardController.addToGrid(size);

        //code that changes the scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}

