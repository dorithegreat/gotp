package com.gotp.GUIcontrollers;

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
     * this is a temporary solution for alternating colors.
     * in the finished project this information will be coming from the server
     * and this field will be obsolete
     */
    private static State nextTurn = State.BLACK;

    /**
     * current state of the piece.
     * Should ideally only be changed at most once
     */
    private State currentState = State.EMPTY;

    /**
     * Constructor.
     * Also assigns a handler to change color of the piece when clicked
     */
    public BoardPiece(){
        super();
        Circle circle = new Circle(20);
        circle.setVisible(false);
        getChildren().add(circle);
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent e) {
                if (currentState == State.EMPTY) { //cannot put piece on a taken spot
                    if (nextTurn == State.WHITE) {
                        circle.setFill(Color.WHITE);
                        currentState = State.WHITE;
                        nextTurn = State.BLACK;
                    } else {
                        circle.setFill(Color.BLACK);
                        currentState = State.BLACK;
                        nextTurn = State.WHITE;
                    }
                    circle.setVisible(true);
                }
            }
        });
    }
}
