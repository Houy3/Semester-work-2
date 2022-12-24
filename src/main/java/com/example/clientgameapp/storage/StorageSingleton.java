package com.example.clientgameapp.storage;

import com.example.clientgameapp.GameApp;
import com.example.clientgameapp.controllers.lobby.LobbyController;

import java.awt.*;
import java.util.concurrent.ScheduledExecutorService;

public class StorageSingleton {
    private static StorageSingleton instance;
    private String roomId;

    private GameApp gameApp;
    private Color color;

   private LobbyController lobbyController;

    private ScheduledExecutorService scheduler;

    private StorageSingleton() {
    }

    static {
        try {
            instance = new StorageSingleton();
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred in creating singleton instance");
        }
    }

    public static StorageSingleton getInstance() {
        return instance;
    }

    public void nullifyAll() {
        this.color = null;
        this.roomId = null;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public GameApp getMainApp() {
        return gameApp;
    }

    public void setMainApp(GameApp gameApp) {
        this.gameApp = gameApp;
    }

    public void setScheduler(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    public LobbyController getLobbyController() {
        return lobbyController;
    }

    public void setLobbyController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
