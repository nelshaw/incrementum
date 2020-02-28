package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import butterknife.InjectView;

public class AddHabitActivity extends AppCompatActivity {

    //parameters of habit
    int trackingLength;
    String HabitName;
    String HabitDescription;


    private EditText name;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        final Button saveButton = findViewById(R.id.save);
        final TextView SBlength = findViewById(R.id.length);
        final SeekBar seekBar = findViewById(R.id.SeekBar);
         name = findViewById(R.id.name);
         description = findViewById(R.id.description);
         HabitName = name.getText().toString();
         HabitDescription = description.getText().toString();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                trackingLength = progress+1;
            if(progress ==0)
                    SBlength.setText(progress +1 + " week");
            if(progress >0)
                    SBlength.setText(progress +1 + " weeks");
        }

        @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
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
            sendData();
        }
        else{return;}
    }
    public Boolean Validate(){

        if(name.getText().toString().length()==0)
        {
            name.setError("Field Cannot be Empty");
            return false;
        }
        if(description.getText().toString().length()==0)
        {
            description.setError("Field Cannot be Empty");
            return false;
        }
        else return true;
        //to do
    }
//pass data to next activity
    public void sendData()
    {
        Intent intent = new Intent(getApplicationContext(),AddHabit2Activity.class);
        intent.putExtra("name",name.getText().toString());
        intent.putExtra("length",Integer.toString(trackingLength));
        intent.putExtra("description",description.getText().toString());
        startActivity(intent);
    }
}
