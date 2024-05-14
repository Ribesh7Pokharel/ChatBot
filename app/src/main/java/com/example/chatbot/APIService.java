package com.example.chatbot;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public interface APIService {
    @POST("/chat")
    Call<ChatResponse> getChatResponse(@Body ChatRequest chatRequest);

    class Factory {
        public static APIService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:5000/") // Ensure this is the correct URL for your backend
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder().readTimeout(10, java.util.concurrent.TimeUnit.MINUTES).build())
                    .build();
            return retrofit.create(APIService.class);
        }
    }
}

class ChatRequest {
    String userMessage;
    List<Message> chatHistory;

    // Constructors
    public ChatRequest(String userMessage, List<Message> chatHistory) {
        this.userMessage = userMessage;
        this.chatHistory = chatHistory;
    }

    // Getters and setters
    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public List<Message> getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(List<Message> chatHistory) {
        this.chatHistory = chatHistory;
    }
}

class ChatResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
