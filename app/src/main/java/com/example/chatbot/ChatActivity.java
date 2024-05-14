package com.example.chatbot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView messagesRecyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private EditText messageInput;
    private Button sendButton;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(messageList);
        messagesRecyclerView.setAdapter(messageAdapter);
        apiService = APIService.Factory.create();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageInput.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    Message newMessage = new Message(messageText, true); // true indicates the message is from the user
                    messageList.add(newMessage);
                    messageAdapter.notifyDataSetChanged();
                    messageInput.setText("");
                    sendMessageToServer(messageText, messageList);

                }
            }
        });
    }

    private void sendMessageToServer(String userMessage, List<Message> chatHistory) {
        ChatRequest chatRequest = new ChatRequest(userMessage, chatHistory);
        for (Message message : chatRequest.getChatHistory()) {
            Log.d("ChatActivity", "Message content: " + message.getContent() + ", isUser: " + message.isUser());
        }
        apiService.getChatResponse(chatRequest).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Message responseMessage = new Message(response.body().getMessage(), false);
                    runOnUiThread(() -> {
                        messageList.add(responseMessage);
                        messageAdapter.notifyDataSetChanged();
                    });
                } else {
                    // Handle the case where the response is not successful
                    Log.e("ChatActivity", "Server error: " + response.code());
                    runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Server error: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                Log.e("ChatActivity", "Network error: " + t.getMessage());
                // Notify user of the failure on the main thread
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show());
            }

        });
    }
}

