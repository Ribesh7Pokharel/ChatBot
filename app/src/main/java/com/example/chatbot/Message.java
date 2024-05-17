package com.example.chatbot;

public class Message {
    private String User;
    private boolean Llama; // true if the message is from the user, false if from the bot

    public Message(String content, boolean isUser) {
        this.User = content;
        this.Llama = isUser;
    }

    public String getContent() {
        return User;
    }

    public void setContent(String content) {
        this.User = content;
    }

    public boolean isUser() {
        return Llama;
    }

    public void setUser(boolean user) {
        Llama = user;
    }
}
