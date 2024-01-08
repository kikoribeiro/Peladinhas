package com.example.peladinhas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText messageEditText;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        messageEditText = findViewById(R.id.messageEditText);
        ListView messageListView = findViewById(R.id.messageListView);
        messageAdapter = new MessageAdapter(this);
        messageListView.setAdapter(messageAdapter);

        subscribeToTopic("group_chat"); // Subscribe to the group chat topic

        loadMessages();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // If the user is not logged in, navigate to the login activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void loadMessages() {
        db.collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener(this, (value, error) -> {
                    if (error != null) {
                        // Handle error
                        return;
                    }

                    for (DocumentChange dc : value.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                com.example.peladinhas.Classes.Message message = dc.getDocument().toObject(com.example.peladinhas.Classes.Message.class);
                                messageAdapter.addMessage(message);
                                break;
                        }
                    }
                });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            Message message = new Message();
            db.collection("messages").add(message)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                messageEditText.setText("");
                            } else {
                                // Handle error
                            }
                        }
                    });
        }
    }

    private void subscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            // Handle error
                        }
                    }
                });
    }
}

