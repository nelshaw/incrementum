package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Log_Habits_Hobbies_Time_Activity extends AppCompatActivity {

    Button EnterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__habits__hobbies__time_);

        EnterButton = findViewById(R.id.addHabitsHobbiesTimeButton);

        EnterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openHabitActivity();
            }
        });
    }

    public void openHabitActivity(){
        Intent intent = new Intent(this, AddHabitActivity.class);
        startActivity(intent);
    }
}
