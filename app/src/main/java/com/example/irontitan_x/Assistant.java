// Assistant.java
package com.example.irontitan_x;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Assistant extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText sendBox;
    private ImageView sendIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

        recyclerView = findViewById(R.id.Chats);
        sendBox = findViewById(R.id.send_box);
        sendIcon = findViewById(R.id.send_icon);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        sendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = sendBox.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    // Add user message to the list
                    messageList.add(new Message(messageText, Message.TYPE_USER));
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);

                    // Clear the input box
                    sendBox.setText("");

                    // Add bot response after user message
                    messageList.add(new Message("This is your assistant! How can I assist you?", Message.TYPE_BOT));
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                }
            }
        });
    }
}
