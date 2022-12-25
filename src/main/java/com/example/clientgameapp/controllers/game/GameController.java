package com.example.clientgameapp.controllers.game;

import Protocol.HighLevelMessageManager;
import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.Game.Game;
import Protocol.MessageValues.Response.ResponseSuccess;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;
import Protocol.exceptions.ProtocolVersionException;
import com.example.clientgameapp.DestinationsManager;
import com.example.clientgameapp.controllers.error.ErrorAlert;
import com.example.clientgameapp.models.CitiesGameMap;
import com.example.clientgameapp.models.Route;
import com.example.clientgameapp.storage.GameStorage;
import com.example.clientgameapp.storage.GlobalStorage;
import com.example.clientgameapp.storage.generator.MapGenerator;
import connection.ClientConnectionSingleton;
import exceptions.ClientConnectionException;
import javafx.animation.PathTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import util.Converter;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public Button cityBtnFirst;
    public Button cityBtnSecond;
    public Button cityBtnThird;
    public Button cityBtnFourth;
    public Button cityBtnFifth;
    public Button cityBtnSixth;
    public Pane pane;
    public Canvas canvas;

    private CitiesGameMap citiesGameMap;

    private GraphicsContext gc;
    private boolean isFirst = true;
    private Button fromButton;
    private boolean isFinished = true;

    private Color color;

    private int duration = 3;

    private boolean isInitialized = false;

    private double widthMargin;
    private List<Button> allAvailableCities;

    private ClientConnectionSingleton connection;
    private HighLevelMessageManager mManager;

    private GameStorage gameStorage;
    private Socket socket;
    private int incrementRate = 2;


    private DestinationsManager destinationsManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            connection = ClientConnectionSingleton.getInstance();
            mManager = new HighLevelMessageManager();
            socket = connection.getSocket();
            destinationsManager = DestinationsManager.getInstance();
            gameStorage = GameStorage.getInstance();
            pane.requestFocus();
        } catch (ClientConnectionException e) {
            ErrorAlert.show(e.getMessage());
        }
    }

    private void initGame() {
        initMap();
        citiesGameMap = gameStorage.getMaps().get(0);
        color = Color.RED;
        // startGameProcess();
        drawWays();
        setAllNumbersValue(0);
        startIncrementingAll();
    }

    private void startGameProcess() {
        try {
            Message startGame = HighLevelMessageManager.startGame(socket);
            if (startGame.type() == MessageManager.MessageType.RESPONSE_SUCCESS) {
                ResponseSuccess success = (ResponseSuccess) startGame.value();
                Game game = (Game) success.getResponseValue();
            } else {
                ErrorAlert.show("Couldn't start the game");
                GlobalStorage.getInstance().getMainApp().closeGame();
            }
        } catch (IOException e) {
            //     ErrorAlert.show(e.getMessage());
            //   GlobalStorage.getInstance().getMainApp().closeGame();
        } catch (MismatchedClassException | BadResponseException e) {
            ErrorAlert.show(e.getMessage());
        }
    }

    private void initMap() {
        allAvailableCities = new ArrayList<>();
        allAvailableCities.add(cityBtnFirst);
        allAvailableCities.add(cityBtnSecond);
        allAvailableCities.add(cityBtnThird);
        allAvailableCities.add(cityBtnFourth);
        allAvailableCities.add(cityBtnFifth);
        allAvailableCities.add(cityBtnSixth);
        MapGenerator mapGenerator = new MapGenerator(allAvailableCities);
        mapGenerator.generate();
        gameStorage.addMap(mapGenerator.getMap());
    }

    private void setAllNumbersValue(int value) {
        for (Button button : allAvailableCities) {
            setButtonText(button, value);
        }
    }

    private void startIncrementingAll() {
        for (Button button : allAvailableCities) {
            startNumberIncrement(button, incrementRate);
        }
    }

    private void startNumberIncrement(Button button, int incrementRate) {
        new Thread(
                () -> {
                    while (isFinished) {
                        int value = Integer.parseInt(button.getText()) + 1;
                        try {
                            Thread.sleep(incrementRate * 1000L);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Platform.runLater(() -> {
                            button.setText(String.valueOf(value));
                        });
                    }
                }
        ).start();
    }

    private void makeMove(Button fromButton, Button toButton) {
        /*
        try {
            City fromCity = getAppropriateCity(fromButton);
            City toCity = getAppropriateCity(toButton);
            Route way = new Route(fromCity, toCity);
            int armyCount = Integer.parseInt(fromButton.getText());
            ArmyMovement movement = new ArmyMovement(way, armyCount);
            Message message = HighLevelMessageManager.moveArmy(
                    movement, socket
            );
            if (message.type() == MessageManager.MessageType.RESPONSE_SUCCESS) {
             //   makeMove(fromButton, toButton);
                drawBall(fromButton, toButton, color, duration);
            }
        } catch (MismatchedClassException | BadResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            ErrorAlert.show(e.getMessage());
            GlobalStorage.getInstance().getMainApp().closeGame();
        }

         */

        drawBall(fromButton, toButton, color, duration);

    }

    private void listenForMovements() {
        new Thread(
                () -> {

                    try {
                        HighLevelMessageManager.readMessage(socket.getInputStream());
                    } catch (MismatchedClassException | ProtocolVersionException e) {
                        throw new RuntimeException(e);
                    } catch (IOException ex) {

                    }

                }
        ).start();
    }


    private void drawBall(Button fromButton, Button toButton, Color color, int durationInSeconds) {
        System.out.println(new Route(fromButton, toButton));
        if (!citiesGameMap.routes().contains(new Route(fromButton, toButton))) {
            return;
        }
        widthMargin = cityBtnFirst.getWidth() / 2;
        Circle ball = new Circle(-fromButton.getLayoutX() - widthMargin, -fromButton.getLayoutY() - widthMargin, widthMargin / 2);
        ball.fillProperty().set(color);

        pane.getChildren().add(ball);
        PathTransition transition = new PathTransition();
        transition.setNode(ball);
        transition.setDuration(Duration.seconds(durationInSeconds));

        setPositionFixed(ball, fromButton);

        double toX = toButton.getLayoutX() + widthMargin / 2;
        double toY = toButton.getLayoutY() + widthMargin / 2;

        Path path = new Path();
        path.getElements().add(new MoveToAbs(ball));
        path.getElements().add(new LineToAbs(ball, toX, toY));

        transition.setPath(path);
        transition.play();

        ball.setTranslateX(toButton.getLayoutX());
        ball.setTranslateY(toButton.getLayoutY());
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
        handleButtonClick(cityBtnFifth, color, duration);
    }

    public void btnSixthClicked(ActionEvent actionEvent) {
        handleButtonClick(cityBtnSixth, color, duration);

    }

    private void handleButtonClick(Button clickedButton, Color color, int duration) {
        if (!isInitialized) {
            initGame();
            isInitialized = true;
        }
        if (isFirst) {
            fromButton = clickedButton;
            drawSelectionBorder(fromButton);
            isFirst = false;
        } else {
            if (fromButton != null) {
                clearSelectionBorder(fromButton);
            }
            makeMove(fromButton, clickedButton);
            isFirst = true;
        }
        pane.requestFocus();
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
        node.relocate(fromButton.getTranslateX() - widthMargin, fromButton.getTranslateY() - widthMargin);
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


    private void drawWays() {
        gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.RED);
        widthMargin = cityBtnFirst.getWidth()/2;
        for (Route route : citiesGameMap.routes()) {
            drawStrokeLine(route.fromCity(), route.toCity());
        }
    }

    private void drawStrokeLine(Button buttonFrom, Button buttonTo) {
        gc.strokeLine(
                buttonFrom.getLayoutX() + widthMargin, buttonFrom.getLayoutY() + widthMargin, buttonTo.getLayoutX() + widthMargin, buttonTo.getLayoutY() + widthMargin
        );
    }

    private void setStyle(Button button, Color color) {
        java.awt.Color newColor = Converter.convertColor(color);
        button.setStyle("-fx-background-color: rgb(" + newColor.getRed() + ","
                + newColor.getGreen() + "," + newColor.getBlue() + ");");
    }



    private void setButtonText(Button button, int number) {
        button.setText(String.valueOf(number));
    }

    private void drawSelectionBorder(Button button) {
        button.setStyle("-fx-border-width: 2px");
    }

    private void clearSelectionBorder(Button button) {
        button.setStyle("-fx-border-width: 0px");
    }


}
