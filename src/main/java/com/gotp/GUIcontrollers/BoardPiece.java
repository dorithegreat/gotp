package com.gotp.GUIcontrollers;

import java.io.IOException;

import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.utilities.Vector;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BoardPiece extends StackPane {

    /**
     * the board that this piece is part of.
     */
    private DisplayBoard board;

    /**
     * current state of the piece.
     * Should ideally only be changed at most once
     */
    private PieceType currentState = PieceType.EMPTY;

    private Vector coordinates;

    private Circle circle;

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
        circle = new Circle(20);
        circle.setVisible(false);
        getChildren().add(circle);

        //adds handler to the circle that manages it's behavior when clicked
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent e) {
                System.out.println("clicked " + coordinates.getX() + " " + coordinates.getY());
                if (currentState == PieceType.EMPTY) {
                    if (board.getPlayer() == PieceType.BLACK) {
                        circle.setFill(Color.BLACK);
                    }
                    else if (board.getPlayer() == PieceType.WHITE) {
                        circle.setFill(Color.WHITE);
                    }
                    circle.setVisible(true);

                    try {
                        board.check(coordinates);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }


    public void makeMove(PieceType color){
        currentState = color;
        if (color == PieceType.WHITE) {
            circle.setFill(Color.WHITE);
            circle.setVisible(true);
        }
        else if (color == PieceType.BLACK) {
            circle.setFill(Color.BLACK);
            circle.setVisible(true);
        }
        else {
            circle.setVisible(false);
        }
        //System.out.println("changed color");
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
