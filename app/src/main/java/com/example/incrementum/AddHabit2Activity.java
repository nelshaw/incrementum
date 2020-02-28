package com.example.incrementum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AddHabit2Activity extends AppCompatActivity {

    private int optionsSelected = 0;
    private int timesSelected;
    private EditText userIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit2);
        final Button saveButton = findViewById(R.id.save);
        userIn = findViewById(R.id.own);

        //trigger buttons
        final ToggleButton location = findViewById(R.id.Location);
        final ToggleButton pe = findViewById(R.id.pe);
        final ToggleButton es = findViewById(R.id.es);
        final ToggleButton otherpeople = findViewById(R.id.otherpeople);
        final ToggleButton time = findViewById(R.id.time);

        //time buttons
        final ToggleButton morning = findViewById(R.id.morning);
        final ToggleButton night = findViewById(R.id.night);
        final ToggleButton af = findViewById(R.id.noon);
        final ToggleButton evening = findViewById(R.id.evening);

//time buttons
        {
            morning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        morning.setBackgroundColor(Color.rgb(209, 101, 75));
                        timesSelected++;
                    } else {
                        morning.setBackgroundColor(Color.rgb(241, 226, 134));
                        timesSelected--;
                    }
                }
            });

            evening.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        evening.setBackgroundColor(Color.rgb(209, 101, 75));
                        timesSelected++;
                    } else {
                        evening.setBackgroundColor(Color.rgb(241, 226, 134));
                        timesSelected--;
                    }
                }
            });

            af.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        af.setBackgroundColor(Color.rgb(209, 101, 75));
                        timesSelected++;
                    } else {
                        af.setBackgroundColor(Color.rgb(241, 226, 134));
                        timesSelected--;
                    }
                }
            });

            night.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        night.setBackgroundColor(Color.rgb(209, 101, 75));
                        timesSelected++;
                    } else {
                        night.setBackgroundColor(Color.rgb(241, 226, 134));
                        timesSelected--;
                    }
                }
            });
        }

//trigger buttons
        {
            pe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        pe.setBackgroundColor(Color.rgb(207, 80, 80));
                        optionsSelected++;
                    } else {
                        pe.setBackgroundColor(Color.rgb(80, 183, 235));
                        optionsSelected--;
                    }
                }
            });
            es.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        es.setBackgroundColor(Color.rgb(207, 80, 80));
                        optionsSelected++;
                    } else {
                        es.setBackgroundColor(Color.rgb(80, 183, 235));
                        optionsSelected--;
                    }
                }
            });
            otherpeople.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        otherpeople.setBackgroundColor(Color.rgb(207, 80, 80));
                        optionsSelected++;
                    } else {
                        otherpeople.setBackgroundColor(Color.rgb(80, 183, 235));
                        optionsSelected--;
                    }
                }
            });
            time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        time.setBackgroundColor(Color.rgb(207, 80, 80));
                        optionsSelected++;
                    } else {
                        time.setBackgroundColor(Color.rgb(80, 183, 235));
                        optionsSelected--;
                    }
                }
            });
            location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        location.setBackgroundColor(Color.rgb(207, 80, 80));
                        optionsSelected++;

                    } else {
                        location.setBackgroundColor(Color.rgb(80, 183, 235));
                        optionsSelected--;
                    }
                }
            });
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Continue();
                }
            });


        }
    }

    public void Continue(){
        if(Validate()) {
            Back();
            //TO DO - ADD SAVE DB LOGIC
        }
    }
    public Boolean Validate(){

        //checks if no options selected
        if(optionsSelected==0 && userIn.getText().toString().length()== 0)
        {
            Toast.makeText(getBaseContext(), "Please select at least one trigger.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(timesSelected==0)
        {
            Toast.makeText(getBaseContext(), "Please select when your habit occurs.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
        //to do
    }
    //returns user to main menu
    public void Back(){
        Intent intent = new Intent(this, ViewHabitActivity.class);
        startActivity(intent);
    }

}
