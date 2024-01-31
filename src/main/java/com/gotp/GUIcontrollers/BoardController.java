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

//javafx controller for board.fxml
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

    /**
     * size of the board (nxn)
     */
    private int size;



    /**
     * function for passing the turn.
     * @param event the event that triggered this method
     */
     @FXML
    void pass(final ActionEvent event) throws InterruptedException, IOException {
        communicator.send("pass");
    }

    /**
     * function for resigning the game.
     * @param event the event that triggered this method
     */
    @FXML
    void resign(final ActionEvent event) throws InterruptedException, IOException {
        communicator.send("resign");
    }

    /**
     * changes to the end screen with an appropriate message (won or lost).
     * @param result
     * @throws IOException
     */
    public void swtichToEndScreen(EndScreenController.Result result) throws IOException {
        //a few declarations that will be necessary for changing the scene
        Parent root;
        Stage stage;
        Scene scene;

        FXMLLoader endLoader = new FXMLLoader(getClass().getResource("end_won.fxml"));
        root = endLoader.load();
        EndScreenController endController = endLoader.getController();

        endController.setMessage(result);
        
        //code that changes the scene

        //a workaround, gets stage for the grid, which is also the stage this controller is for
        stage = (Stage) leftSide.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * makes an empty board at the start of the game.
     */
    public void createBoard(){
        board = new DisplayBoard(size);
        leftSide.setCenter(board);

        communicator.setBoardContrller(this, size);
        communicator.setBoard(board);
    }

    //works because it's called right after the size is set
    public void requestMode(String mode) throws InterruptedException, IOException {
        communicator.sendGameRequest(mode, size);
    }

    /**
     * setter for the size of the board.
     * @param n
     */
    public void setSize(int n){
        size = n;
    }

}
