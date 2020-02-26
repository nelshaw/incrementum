package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HabitEffectActivity extends AppCompatActivity {

    Button nButton;
    Button pButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_effect);

        // find button by id
        nButton = findViewById(R.id.negative_button);
        pButton = findViewById(R.id.positive_button);

        // on click function negative button
        nButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openJournalActivity();
            }
        });

        // on click function positive button
        pButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openHomeActivity();
            }
        });

    }

    // open journal
    public void openJournalActivity(){
        Intent intent = new Intent(this, AddJournalActivity.class);
        startActivity(intent);
    }

    // open home
    public void openHomeActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
