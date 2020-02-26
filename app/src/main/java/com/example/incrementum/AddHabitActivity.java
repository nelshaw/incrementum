package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddHabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        Button saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            openViewHabitActivity();
            }
        });


    }

    public void openViewHabitActivity(){
        Intent intent = new Intent(this, ViewHabitActivity.class);
        startActivity(intent);
    }



}
