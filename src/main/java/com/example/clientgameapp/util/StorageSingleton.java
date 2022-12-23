package com.example.clientgameapp.util;

import java.awt.*;

public class StorageSingleton {
    private static StorageSingleton instance;
    private String roomId;

    private Color color;

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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
