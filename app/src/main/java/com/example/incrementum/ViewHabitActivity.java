package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ViewHabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);
        Button addButton = findViewById(R.id.AddHabit);

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openAddHabitActivity();
            }
        });
    }

    public void openAddHabitActivity(){
        Intent intent = new Intent(this, AddHabitActivity.class);
        startActivity(intent);
    }
}
