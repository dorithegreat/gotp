package com.gotp.GUIcontrollers;

import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.utilities.Vector;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BoardPiece extends StackPane {
    /**
     * enum containing all valid state of a board piece.
     * (either white, black or not yet assigned)
     */
    public enum State {
        WHITE, BLACK, EMPTY
    }

    /**
     * the board that this piece is part of.
     */
    private DisplayBoard board;

    /**
     * this is a temporary solution for alternating colors.
     * in the finished project this information will be coming from the server
     * and this field will be obsolete
     */
    private static PieceType player = PieceType.BLACK;

    /**
     * current state of the piece.
     * Should ideally only be changed at most once
     */
    private PieceType currentState = PieceType.EMPTY;

    private Vector coordinates;

    /**
     * Constructor.
     * Also assigns a handler to change color of the piece when clicked
     */
    public BoardPiece() {
        super();

        //sets the background of every tile to a nice light blue
        setStyle("-fx-background-color: #abcdef");

        //creates a circle (player's stone) and hides it by default
        //this circle is the only content of this stack pane, but it can be expanded in the future
        Circle circle = new Circle(20);
        circle.setVisible(false);
        getChildren().add(circle);

        //adds handler to the circle that manages it's behavior when clicked
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent e) {
                board.notifyOfChange(coordinates, currentState);
                if (player == PieceType.WHITE) {
                    circle.setFill(Color.WHITE);
                    currentState = PieceType.WHITE;
                }
                else if(player == PieceType.BLACK) {
                    circle.setFill(Color.BLACK);
                    currentState = PieceType.BLACK;
                }
                circle.setVisible(true);
            }
        });
    }

    /**
     * setter for board.
     */
    public void linkToBoard(DisplayBoard board){
        this.board = board;
    }

    public PieceType getCurrentColor(){
        return currentState;
    }

    public void setColor(PieceType color){
        currentState = color;
    }

    public void setCoordinates(Vector vector){
        coordinates = vector;
    }
}
