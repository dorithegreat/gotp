package com.gotp.GUIcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class EndScreenController {

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
    void startReplay(ActionEvent event) {

    }

}
