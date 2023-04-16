package com.example.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FriendInfoActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView idTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        nameTextView = findViewById(R.id.nameTextView);
        idTextView = findViewById(R.id.idTextView);

        Intent intent = getIntent();
        String friendId = intent.getStringExtra("friendId");

        // In a real app, you would fetch the friend's information from a database or API
        // Here, we just display a placeholder message with the friend's ID
        nameTextView.setText("Friend Name");
        idTextView.setText(friendId);
    }
}
