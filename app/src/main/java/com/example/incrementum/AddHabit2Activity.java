package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddHabit2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit2);
        final Button saveButton = findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Continue();
            }
        });
    }
    public void Continue(){
        if(Validate()) {
            Back();
            //TO DO - ADD SAVE DB LOGIC
        }
        else{
            Toast.makeText(getBaseContext(), "Oops, You may have forgot to fill something in.", Toast.LENGTH_LONG).show();
        }
    }
    public Boolean Validate(){


        return true;
        //to do
    }
    public void Back(){
        Intent intent = new Intent(this, ViewHabitActivity.class);
        startActivity(intent);
    }

}
