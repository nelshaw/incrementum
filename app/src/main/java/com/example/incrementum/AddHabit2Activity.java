package com.example.incrementum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

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
    String HabitId;
    String ownTrigger;
    /**************PARAMETERS OF HABIT***************/
    String email;
    String id;
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


        UserInfo user = (UserInfo) getApplication();

        email = user.getEmail();
        id = user.getUserId();
        Toast.makeText(this.getBaseContext(),email, Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit2);
        final Button saveButton = findViewById(R.id.save);
        
        //trigger buttons
        final ToggleButton location = findViewById(R.id.location);
        final ToggleButton pe = findViewById(R.id.pe);
        final ToggleButton es = findViewById(R.id.es);
        final ToggleButton otherpeople = findViewById(R.id.otherPeople);
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
                        morning.setBackgroundColor(Color.rgb(216, 242, 243));
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
                        evening.setBackgroundColor(Color.rgb(216, 242, 243));
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
                        af.setBackgroundColor(Color.rgb(216, 242, 243));
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
                        pe.setBackgroundColor(Color.rgb(216, 242, 243));
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
                        es.setBackgroundColor(Color.rgb(216, 242, 243));
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
                        otherpeople.setBackgroundColor(Color.rgb(216, 242, 243));
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
                        time.setBackgroundColor(Color.rgb(216, 242, 243));
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
                        location.setBackgroundColor(Color.rgb(216, 242, 243));
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
            addHabit();
//            addEmptyCalendarEntry();
//            MyAsyncTask task = new MyAsyncTask();
//            task.execute();
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

    public void addHabit(){

        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Habits");

//        String name;
//        int length;
//        String description;

        ArrayList<String> triggerList = new ArrayList<>();
        ArrayList<String> timeList = new ArrayList<>();

//        boolean triggerLocation = false;
//        boolean triggerpe = false;
//        boolean triggeres = false;
//        boolean triggerop = false;
//        boolean triggerTime = false;

        //checks what triggers are added
        if(triggeres)
        {
            triggerList.add("Emotional State");
        }
        if(triggerpe)
        {
            triggerList.add("Preceding Event");
        }
        if(triggerop)
        {
            triggerList.add("Other People");
        }
        if(triggerTime)
        {
            triggerList.add("Time");
        }
        if(triggerLocation)
        {
            triggerList.add("Location");
        }

        //times
//        boolean timeMorning = false;
//        boolean timeEvening = false;
//        boolean timeAfternoon = false;
//        boolean timeNight = false;

//times
        if(timeAfternoon)
        {
            timeList.add("Afternoon");
        }
        if(timeEvening)
        {
            timeList.add("Evening");
        }
        if(timeMorning)
        {
            timeList.add("Morning");
        }
        if(timeNight)
        {
            timeList.add("Night");
        }
        Document doc = new Document()
                .append("name", name)
                .append("length",length*14)
                .append("description",description)
                .append("Triggers",triggerList)
                .append("Times",timeList)
                .append("userId",id);
        final Task<RemoteInsertOneResult> insert = coll.insertOne(doc);
        ObjectId id = doc.getObjectId("_id");
        insert.addOnCompleteListener(new OnCompleteListener<RemoteInsertOneResult>() {
            @Override
            public void onComplete(@NonNull Task<RemoteInsertOneResult> task) {
                Log.d("before sucess", "mesg");
                if (task.isSuccessful()){
                    Log.d("STITCH", String.format("success inserting: %s",
                            task.getResult().getInsertedId()));
                    String id = task.getResult().getInsertedId().toString();
                    String id1 = id.split("=")[1];
                    HabitId = id1.substring(0, id1.length() - 1);
                    Log.d("Habit id ********", HabitId);
                    addEmptyCalendarEntry();
                }
                else{
                    Log.d("STITCH", "Unsuccessful");
                }
            }
        });
    }

    public void addEmptyCalendarEntry(){
        //connect to collection
        final RemoteMongoCollection<Document> coll =
                DatabaseHelper.mongoClient.getDatabase("Incrementum").getCollection("Calendar");

        Log.d("habit id from calendar", HabitId);

        List<String> Days = new ArrayList<>();

        Document doc = new Document()
                .append("User_id", id)
                .append("Habit_id", HabitId)
                .append("Days", Days);

        final Task<RemoteInsertOneResult> insert = coll.insertOne(doc);
        ObjectId id = doc.getObjectId("_id");
        insert.addOnCompleteListener(new OnCompleteListener<RemoteInsertOneResult>() {
            @Override
            public void onComplete(@NonNull Task<RemoteInsertOneResult> task) {
                if (task.isSuccessful()){
                    Log.d("STITCH", String.format("success inserting: %s",
                            task.getResult().getInsertedId()));
                }
                else{
                    Log.d("STITCH", "Unsuccessful");
                }
            }
        });
    }

}