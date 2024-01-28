package com.gotp.GUIcontrollers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.gotp.server.BoardCommunicator;
import com.gotp.server.Client;

public class BoardController {

    /**
     * left side of the scene, that will host the grid part of the board.
     */
    @FXML
    private BorderPane leftSide;


    /**
     * button for passing the turn.
     */
    @FXML
    private Button passButton;

    /**
     * button for resigning the game.
     */
    @FXML
    private Button resignButton;

    /**
     * the client app controlling this board.
     */
    private BoardCommunicator communicator = BoardCommunicator.getInstance();

    private DisplayBoard board;


    public BoardController(){
        communicator.setBoardContrller(this);
    }

    public void doSomething(){
        System.out.println("did something");
    }

    /**
     * function for passing the turn.
     * @param event the event that triggered this method
     */
     @FXML
    void pass(final ActionEvent event) {
        communicator.send("pass");
    }

    /**
     * function for resigning the game.
     * @param event the event that triggered this method
     */
    @FXML
    void resign(final ActionEvent event) {
        communicator.send("resign");
    }

    public void swtichToEndScreen(/*indicator if the game is won or lost */) throws IOException {
        //a few declarations that will be necessary for changing the scene
        Parent root;
        Stage stage;
        Scene scene;

        FXMLLoader endLoader = new FXMLLoader(getClass().getResource("end_won.fxml"));
        root = endLoader.load();
        //EndScreenController endController = endLoader.getController();

        //TODO do whatever is necessery with the end screen here

        //code that changes the scene

        //a workaround, gets stage for the grid, which is also the stage this controller is for
        stage = (Stage) leftSide.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void createBoard(int n){
        board = new DisplayBoard(n);
        leftSide.setCenter(board);
    }

    public void requestMode(String mode) {
        communicator.send(mode);
    }
}
