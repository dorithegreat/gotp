package com.gotp.GUIcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.IOException;

public class TestSceneController implements Initializable {

    /**
     * slider for selecting the size of the board.
     */
    @FXML
    private Slider selectorSlider;

    /**
     * chooses to play against a bot.
     */
    @FXML
    private RadioButton bot;

    /**
     * toggle group to make playing against a player or a bot mutually exclusive
     */
    @FXML
    private ToggleGroup mode;

    /**
     * chooses to play against another player.
     */
    @FXML
    private RadioButton otherPlayer;

    /**
     * default size of the board in case the size is somehow not selected.
     */
    private static final int DEFAULT_SIZE = 7;

    /**
     * size of the board that will be created.
     */
    private int size = DEFAULT_SIZE;


    //----------------------------------------------------------------------------------
    //                  methods and handlers
    //----------------------------------------------------------------------------------

    /**
     * adds a change listener to the selectorSlider when this object is created.
     */
    @Override
    public void initialize(final URL url, final ResourceBundle resources) {
        selectorSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(final ObservableValue<? extends Number> observable,
                                                final Number oldNumber, final Number newNumber) {
                size = (int) selectorSlider.getValue();
            }
        });
    }


    @FXML
    void chooseMode(ActionEvent event) {

    }

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

        //gets the controller for the next scene ahead of time so that it can call a few methods on it
        FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("board.fxml"));
        root = boardLoader.load();
        BoardController boardController = boardLoader.getController();

        //calls method in the other controller to create a board of certain size
        boardController.setSize(size);
        boardController.createBoard();

        if (bot.isSelected() == true) {
            boardController.requestMode("bot");
        }
        else if (otherPlayer.isSelected() == true) {
            boardController.requestMode("player");
        }
        else{
            //should never happen
            System.out.println("an error has occured");
            return;
        }

        //code that changes the scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}

