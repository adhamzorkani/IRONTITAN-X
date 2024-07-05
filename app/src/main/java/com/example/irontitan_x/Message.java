package com.example.irontitan_x;

// Message.java
public class Message {
    public static final int TYPE_USER = 0;
    public static final int TYPE_BOT = 1;

    private String text;
    private int type;

    public Message(String text, int type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }
}

