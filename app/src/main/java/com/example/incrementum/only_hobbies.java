package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class only_hobbies extends AppCompatActivity {

    String username;
    String completeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_hobbies);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        completeString = "Hobby Stats For " + username;

        TextView userText = findViewById(R.id.usertext);
        userText.setText(completeString);

        final Button backButton = findViewById(R.id.mainbutton);
        final Button habitButton = findViewById(R.id.habitbutton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
                sendData(username, 1);
            }
        });

        habitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHabit();
                sendData(username, 2);
            }
        });
    }

    public void sendData(String username, int location)
    {
        if(location == 1) {
            Intent intent = new Intent(getApplicationContext(), Hobby_Stats.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
        else if(location == 2) {
            Intent intent = new Intent(getApplicationContext(), only_habits.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }

    public void goBack() {
        Intent intent = new Intent(this, Hobby_Stats.class);
        startActivity(intent);
    }

    public void goHabit() {
        Intent intent = new Intent(this, only_habits.class);
        startActivity(intent);
    }
}
