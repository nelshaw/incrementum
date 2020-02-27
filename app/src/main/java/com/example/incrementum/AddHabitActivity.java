package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddHabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        final Button saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Continue();
            }
        });
    }

    public void openAddHabit2Activity(){
        Intent intent = new Intent(this, AddHabit2Activity.class);
        startActivity(intent);
    }

    public void Continue(){
        if(Validate()) {
            openAddHabit2Activity();
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


}
