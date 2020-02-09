package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //button to test habit effect activity, not there in final project
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find button by id
        button = findViewById(R.id.button);

        // on click function
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openHabitEffectActivity();
            }
        });
    }

    // open habit effect
    public void openHabitEffectActivity(){
        Intent intent = new Intent(this, HabitEffectActivity.class);
        startActivity(intent);
    }

}
