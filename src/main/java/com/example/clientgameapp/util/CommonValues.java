package com.example.clientgameapp.util;

public class CommonValues {
    private static CommonValues instance;
    private String roomId;

    private CommonValues() {
    }

    static {
        try {
            instance = new CommonValues();
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred in creating singleton instance");
        }
    }

    public static CommonValues getInstance() {
        return instance;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

}
