package com.gotp.GUIcontrollers;

import com.gotp.game_mechanics.board.PieceType;
import com.gotp.server.BoardCommunicator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class EndScreenController {

    /**
     * enum containing both possibles results of the game so that the message isn't set to some nonsense.
     * it's impossible for a game to end in a tie
     */
    public enum Result {
        WON, LOST
    }

    /**
     * text displaying whether the player won or lost the game.
     */
    @FXML
    private Text message;

    /**
     * button for choosing the option to see a replay of the game.
     */
    @FXML
    private Button replayButton;

    /**
     * sets the message on the text field.
     * ? should probably replace the parameter with an enum
     * @param message
     */
    public void setMessage(Result result) {
        if (result == Result.LOST) {
            message.setText("YOU LOST");
        }
        else if (result == Result.WON) {
            message.setText("YOU WON");
        }
    }

    @FXML
    void startReplay(ActionEvent event) throws IOException, InterruptedException{
        System.out.println("clicked");
        BoardCommunicator.getInstance().sendDatabaseRequest();
        BoardCommunicator.getInstance().setPlayer(PieceType.EMPTY);
        System.out.println("request sent");
    }

    public void changeToBoard(int n) throws IOException{
        //a few declarations that will be necessary for changing the scene
        Parent root;
        Stage stage;

        FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("board.fxml"));
        root = boardLoader.load();
        BoardController boardController = boardLoader.getController();

        // System.out.println("got controller");
        boardController.setSize(n);
        boardController.createBoard();
        
        System.out.println("changing scene");
        //code that changes the scene
        stage = (Stage) message.getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.show();
    }

}
