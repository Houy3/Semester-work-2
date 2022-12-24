package com.example.clientgameapp.controllers.game;

import javafx.animation.PathTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public Button cityBtnFirst;
    public Button cityBtnSecond;
    public Button cityBtnThird;
    public Button cityBtnFourth;
    public Button cityBtnFifth;
    public Button cityBtnSixth;
    public Group group;
    public Pane pane;
    public Canvas canvas;
    public static final String CHOICE_MESSAGE = "Now choose city to conquer!";
    public static final String START_CITY_MESSAGE = "Start city can be chosen!";


    private boolean isFirst = true;
    private Button fromButton;


    private Color color;

    private int duration = 10;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        color = Color.RED;
    }


    private void drawBall(Button fromButton, Button toButton, Color color, int durationInSeconds) {
        Circle ball = new Circle(-fromButton.getLayoutX() - fromButton.getWidth() / 2, -fromButton.getLayoutY() - fromButton.getWidth() / 2, 10);
        ball.fillProperty().set(color);

        pane.getChildren().add(ball);
        PathTransition transition = new PathTransition();
        transition.setNode(ball);
        transition.setDuration(Duration.seconds(durationInSeconds));

        setPositionFixed(ball, fromButton);

        double toX = toButton.getLayoutX() + toButton.getWidth() / 2;
        double toY = toButton.getLayoutY() + toButton.getWidth() / 2;

        Path path = new Path();
        path.getElements().add(new MoveToAbs(ball));
        path.getElements().add(new LineToAbs(ball, toX, toY));

        transition.setPath(path);
        transition.play();

        ball.setTranslateX(toButton.getLayoutX() + toButton.getWidth() / 2);
        ball.setTranslateY(toButton.getLayoutY() + toButton.getWidth() / 2);
        removeBall(transition, durationInSeconds, ball);
    }

    public void btnFirstClicked(ActionEvent actionEvent) {
        handleButtonClick(cityBtnFirst, color, duration);
    }

    public void btnSecondClicked(ActionEvent actionEvent) {
        handleButtonClick(cityBtnSecond, color, duration);
    }

    public void btnThirdClicked(ActionEvent actionEvent) {
        handleButtonClick(cityBtnThird, color, duration);
    }

    public void btnFourthClicked(ActionEvent actionEvent) {
        handleButtonClick(cityBtnFourth, color, duration);

    }

    public void btnFifthClicked(ActionEvent actionEvent) {

        System.out.println("CITY 1: " + " X: " + getRelativeX(cityBtnFirst.getLayoutX()) + "      Y:      " + getRelativeY(cityBtnFirst.getLayoutY()));
        System.out.println("CITY 2: " + " X: " + getRelativeX(cityBtnSecond.getLayoutX()) + "     Y:     " + getRelativeY(cityBtnSecond.getLayoutY()));
        System.out.println("CITY 3: " + " X: " + getRelativeX(cityBtnThird.getLayoutX()) + "      Y:      " + getRelativeY(cityBtnThird.getLayoutY()));
        System.out.println("CITY 4: " + " X: " + getRelativeX(cityBtnFourth.getLayoutX()) + "     Y:      " + getRelativeY(cityBtnFourth.getLayoutY()));
        System.out.println("CITY 5: " + " X: " + getRelativeX(cityBtnFifth.getLayoutX()) + "      Y:       " + getRelativeY(cityBtnFifth.getLayoutY()));
        System.out.println("CITY 6: " + " X: " + getRelativeX(cityBtnSixth.getLayoutX()) + "      Y:       " + getRelativeY(cityBtnSixth.getLayoutY()));

        setStyle(cityBtnFifth, Color.YELLOW);
        handleButtonClick(cityBtnFifth, color, duration);
    }

    private int getRelativeX(double x) {
        return (int) (x / (pane.getWidth()) * (int) 100);
    }

    private int getRelativeY(double y) {
        return (int) (y / (pane.getHeight()) * (int) 100);
    }

    public void btnSixthClicked(ActionEvent actionEvent) {
        handleButtonClick(cityBtnSixth, color, duration);

    }

    private void handleButtonClick(Button clickedButton, Color color, int duration) {
        System.out.println();
        if (isFirst) {
            fromButton = clickedButton;
            drawText(CHOICE_MESSAGE);
            isFirst = false;
        } else {
            drawBall(fromButton, clickedButton, color, duration);
            isFirst = true;
            drawText(START_CITY_MESSAGE);
        }
    }

    public static class MoveToAbs extends MoveTo {
        public MoveToAbs(Node node) {
            super(node.getLayoutBounds().getWidth() / 2, node.getLayoutBounds().getHeight() / 2);
        }

        public MoveToAbs(Node node, double x, double y) {
            super(x - node.getLayoutX() + node.getLayoutBounds().getWidth() / 2, y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
        }
    }

    private void setPositionFixed(Node node, Button fromButton) {
        node.relocate(fromButton.getTranslateX() - fromButton.getWidth() / 2, fromButton.getTranslateY() - fromButton.getWidth() / 2);
    }

    public static class LineToAbs extends LineTo {
        public LineToAbs(Node node, double x, double y) {
            super(x - node.getLayoutX() + node.getLayoutBounds().getWidth() / 2, y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
        }
    }


    private void removeBall(Transition transition, int durationInSeconds, Circle ball) {
        new Thread(() -> {
            try {
                Thread.sleep(durationInSeconds * 1000L);
                transition.stop();
                Platform.runLater(() -> {
                    pane.getChildren().remove(ball);
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void drawText(String message) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.fillText(message, 200, 300);
    }

    private void setStyle(Button button, Color color) {
        button.setStyle("-fx-background-radius: 30px;\n" +
                "    -fx-min-width: 60px;\n" +
                "    -fx-min-height: 60px;\n" +
                "    -fx-max-width: 60px;\n" +
                "-fx-background-color: rgb(" + color.getRed() + ","
                + color.getGreen() + "," + color.getBlue() + ");" +
                "    -fx-max-height: 60px;");
    }


}
