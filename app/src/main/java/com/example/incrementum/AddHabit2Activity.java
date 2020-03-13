package com.example.incrementum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AddHabit2Activity extends AppCompatActivity {

    private int optionsSelected = 0;
    private int timesSelected;

    /**************PARAMETERS OF HABIT***************/
    //triggers
    private EditText userIn;
    boolean triggerLocation = false;
    boolean triggerpe = false;
    boolean triggeres = false;
    boolean triggerop = false;
    boolean triggerTime = false;
    //times
    boolean timeMorning = false;
    boolean timeEvening = false;
    boolean timeAfternoon = false;
    boolean timeNight = false;
    //From previous activity
   String name;
   int length;
   String description;
   /**************PARAMETERS OF HABIT***************/

   @Override
   public void onBackPressed() {
       Intent intent = new Intent(this, AddHabitActivity.class);
       startActivity(intent);
   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
         name = intent.getStringExtra("name");
         length = Integer.valueOf(intent.getStringExtra("length"));
         description = intent.getStringExtra("description");

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
                        timeMorning = true;
                    } else {
                        morning.setBackgroundColor(Color.rgb(241, 226, 134));
                        timesSelected--;
                        timeMorning = false;
                    }
                }
            });

            evening.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        evening.setBackgroundColor(Color.rgb(209, 101, 75));
                        timesSelected++;
                        timeEvening = true;
                    } else {
                        evening.setBackgroundColor(Color.rgb(241, 226, 134));
                        timesSelected--;
                        timeEvening = false;
                    }
                }
            });

            af.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        af.setBackgroundColor(Color.rgb(209, 101, 75));
                        timesSelected++;
                        timeAfternoon = true;
                    } else {
                        af.setBackgroundColor(Color.rgb(241, 226, 134));
                        timesSelected--;
                        timeAfternoon = false;
                    }
                }
            });

            night.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        night.setBackgroundColor(Color.rgb(209, 101, 75));
                        timesSelected++;
                        timeNight = true;
                    } else {
                        night.setBackgroundColor(Color.rgb(241, 226, 134));
                        timesSelected--;
                        timeNight = false;
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
                        triggerpe = true;
                    } else {
                        pe.setBackgroundColor(Color.rgb(80, 183, 235));
                        optionsSelected--;
                        triggerpe = false;
                    }
                }
            });
            es.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        es.setBackgroundColor(Color.rgb(207, 80, 80));
                        optionsSelected++;
                        triggeres = true;
                    } else {
                        es.setBackgroundColor(Color.rgb(80, 183, 235));
                        optionsSelected--;
                        triggerpe = false;
                    }
                }
            });
            otherpeople.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        otherpeople.setBackgroundColor(Color.rgb(207, 80, 80));
                        optionsSelected++;
                        triggerop = true;
                    } else {
                        otherpeople.setBackgroundColor(Color.rgb(80, 183, 235));
                        optionsSelected--;
                        triggerop = false;
                    }
                }
            });
            time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        time.setBackgroundColor(Color.rgb(207, 80, 80));
                        optionsSelected++;
                        triggerTime = true;
                    } else {
                        time.setBackgroundColor(Color.rgb(80, 183, 235));
                        optionsSelected--;
                        triggerTime = false;
                    }
                }
            });
            location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        location.setBackgroundColor(Color.rgb(207, 80, 80));
                        optionsSelected++;
                        triggerLocation = true;

                    } else {
                        location.setBackgroundColor(Color.rgb(80, 183, 235));
                        optionsSelected--;
                        triggerLocation = false;
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
/*
    public String getAllEntries(){

        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Users");

        //Document doc = new Document().append("sensitive_time_start", startTimeText.getText().toString()).append("sensitive_time_end", endTimeText.getText().toString()).append("hobbies", HobbyText.getText().toString());

        final Task<RemoteInsertOneResult> insert = coll.insertOne(doc);
        insert.addOnCompleteListener(new OnCompleteListener<RemoteInsertOneResult>() {
            @Override
            public void onComplete(@NonNull Task<RemoteInsertOneResult> task) {
                if (task.isSuccessful()){
                    Log.d("STITCH", String.format("success inserting: %s",
                            task.getResult().getInsertedId()));
                }
            }
        });

        Document filterDoc = new Document();

        RemoteFindIterable results = coll.find(filterDoc);
        return "Test";
    }


*/






















}
