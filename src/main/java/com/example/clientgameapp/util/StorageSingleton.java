package com.example.clientgameapp.util;

import java.awt.*;
import java.util.concurrent.ScheduledExecutorService;

public class StorageSingleton {
    private static StorageSingleton instance;
    private String roomId;

    private Color color;

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

    public void setScheduler(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
