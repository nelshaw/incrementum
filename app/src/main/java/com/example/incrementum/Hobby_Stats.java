package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Hobby_Stats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby__stats);

        final Button backButton = findViewById(R.id.backbutton);
        final Button hobbyButton = findViewById(R.id.hobbybutton);
        final Button habitButton = findViewById(R.id.habitbutton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        hobbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHobby();
            }
        });

        habitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHabit();
            }
        });
    }

    public void goBack() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void goHobby() {
        Intent intent = new Intent(this, only_hobbies.class);
        startActivity(intent);
    }

    public void goHabit() {
        Intent intent = new Intent(this, only_habits.class);
        startActivity(intent);
    }
}
