package com.example.incrementum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.incrementum.DatabaseHelper.mongoClient;

public class AddJournalActivity extends AppCompatActivity {

  @InjectView(R.id.save) Button saveBtn;
  @InjectView(R.id.cancelBtn) Button cancelBtn;
  @InjectView(R.id.newTriggerCheck) CheckBox newTriggerCheck;
  @InjectView(R.id.journalEntry) EditText entry;
  @InjectView(R.id.triggerTitle) TextView triggerTitle;
  @InjectView(R.id.triggerTime) TextView timeTitle;

  @InjectView(R.id.triggers_1) LinearLayout triggerLayout;
  @InjectView(R.id.triggers_2) LinearLayout trigger2Layout;
  @InjectView(R.id.time_1) LinearLayout timeLayout;
  @InjectView(R.id.time_2) LinearLayout time2Layout;

  @InjectView(R.id.otherPeople) ToggleButton otherPeople;
  @InjectView(R.id.pe) ToggleButton precedingEvent;
  @InjectView(R.id.es) ToggleButton emotionalState;
  @InjectView(R.id.location) ToggleButton location;
  @InjectView(R.id.time) ToggleButton time;
  @InjectView(R.id.morning) ToggleButton morning;
  @InjectView(R.id.afternoon) ToggleButton afternoon;
  @InjectView(R.id.evening) ToggleButton evening;
  @InjectView(R.id.night) ToggleButton night;

  // Get user information
  UserInfo user;
  String user_id, habit_id;
  Date date;

  // Trigger values
  boolean triggerLoc = false, triggerPEvent = false, triggerEState = false, triggerOPeople = false,
    triggerTime = false;
  // Time values
  boolean timeMorn = false, timeEve = false, timeAft = false, timeNight = false;

  int red = Color.rgb(237, 177, 162);
  int blue = Color.rgb(216, 242, 243);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_journal);
    ButterKnife.inject(this);

    // Get user
    user = (UserInfo) getApplication();

    // Get user id from user who logged in successfully
    user_id = user.getUserId();

    // Get habit_id from habit page to view journal entries for that habit
    habit_id = user.getHabitId();

    // Get date from calendar
    date = CalendarActivity.dateSelected;
    if(date == null){
      date = new Date();
    }

    // Time buttons
    setListener(morning);
    setListener(afternoon);
    setListener(evening);
    setListener(night);

    // Trigger buttons
    setListener(precedingEvent);
    setListener(emotionalState);
    setListener(otherPeople);
    setListener(time);
    setListener(location);

    // Save entry and direct to view all journals
    saveBtn.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
          if(!isEntryEmpty(entry.getText().toString())) openViewJournalActivity();
          else{
            CharSequence text = "Unable to add an empty journal entry... \nTry again.";

            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
            toast.show();
          }
        }
      });

    // Cancel process
    cancelBtn.setOnClickListener(v -> {
      Intent intent = new Intent(this, ViewJournalActivity.class);
      startActivity(intent);
    });
  }

  public void openViewJournalActivity(){
    addEntry();
    Intent intent = new Intent(this, ViewJournalActivity.class);

    startActivity(intent);
  }

  public void onCheckboxClicked(View view){
    boolean checked = ((CheckBox) view).isChecked();

    switch(view.getId()){
      case R.id.newTriggerCheck:
        if(checked){
          triggerLayout.setVisibility(View.VISIBLE);
          trigger2Layout.setVisibility(View.VISIBLE);
          timeLayout.setVisibility(View.VISIBLE);
          time2Layout.setVisibility(View.VISIBLE);

          triggerTitle.setVisibility(View.VISIBLE);
          timeTitle.setVisibility(View.VISIBLE);
        }
        else{
          triggerLayout.setVisibility(View.INVISIBLE);
          trigger2Layout.setVisibility(View.INVISIBLE);
          timeLayout.setVisibility(View.INVISIBLE);
          time2Layout.setVisibility(View.INVISIBLE);

          triggerTitle.setVisibility(View.INVISIBLE);
          timeTitle.setVisibility(View.INVISIBLE);
        }
        break;
    }

  }

  public void addEntry(){

    // Connect to MongoDB client
    final RemoteMongoCollection<Document> coll =
      mongoClient.getDatabase("Incrementum").getCollection("Journals");

    // Create new document from information and entry submitted
    Document doc = new Document()
      .append("user_id", user_id)
      .append("date", date)
      .append("entry", entry.getText().toString())
      .append("habit_id", habit_id);

    // Insert document
    final Task<RemoteInsertOneResult> insert = coll.insertOne(doc);

    insert.addOnCompleteListener(new OnCompleteListener<RemoteInsertOneResult>() {
      @Override
      public void onComplete(@NonNull Task<RemoteInsertOneResult> task) {
        if (task.isSuccessful()){
          Log.d("STITCH", String.format("success inserting: %s",
            task.getResult().getInsertedId()));
        }
        else{
          Log.d("STITCH", "Unsuccessful adding journal entry");
        }
      }
    });

    // if new trigger, add them to habit database
    if(newTriggerCheck.isChecked()) {
      addHabit();
    }
  }

  public void addHabit(){

    // Connect to MongoDB
    final RemoteMongoCollection<Document> coll =
      mongoClient.getDatabase("Incrementum").getCollection("Habits");

    ArrayList<String> triggerList = new ArrayList<>();
    ArrayList<String> timeList = new ArrayList<>();

    // check what triggers are added
    if(triggerEState)
    {
      triggerList.add("Emotional State");
    }
    if(triggerPEvent)
    {
      triggerList.add("Preceding Event");
    }
    if(triggerOPeople)
    {
      triggerList.add("Other People");
    }
    if(triggerTime)
    {
      triggerList.add("Time");
    }
    if(triggerLoc)
    {
      triggerList.add("Location");
    }

    // check what times are added
    if(timeAft)
    {
      timeList.add("Afternoon");
    }
    if(timeEve)
    {
      timeList.add("Evening");
    }
    if(timeMorn)
    {
      timeList.add("Morning");
    }
    if(timeNight)
    {
      timeList.add("Night");
    }

    ObjectId habit = new ObjectId(habit_id);

    // Create document
    Document filterDoc = new Document()
      .append("userId", user_id)
      .append("_id", habit);

    Document updateDoc = new Document()
      .append("$addToSet", new Document()
        .append("Triggers", new Document().append("$each", triggerList))
        .append("Times", new Document().append("$each", timeList))
      );

    final Task<RemoteUpdateResult> updateTask = coll.updateOne(filterDoc, updateDoc);

    updateTask.addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        long numMatched = task.getResult().getMatchedCount();
        long numModified = task.getResult().getModifiedCount();
        Log.d("app", String.format("successfully matched %d and modified %d documents",
          numMatched, numModified));
      } else {
        Log.e("app", "failed to update document with: ", task.getException());
      }
    });
  }

  public boolean isEntryEmpty(String entry){
    if(entry.isEmpty() || entry.equals("")){
      Log.d("JOURNAL", "Unable to add an empty journal");
      return true;
    }
    return false;
  }

  public void setEntry(String newEntry){
    entry.setText(newEntry);
  }

  public void setUserId(String userId){
    user_id = userId;
  }

  public void setHabitId(String habitId){
    habit_id = habitId;
  }

  public void setNewTriggerCheck(boolean isChecked){
    newTriggerCheck.setChecked(isChecked);
  }

  public void setTriggers(boolean values){
    timeMorn = values;
    triggerEState = values;
  }

  public void setListener(ToggleButton button){
    button.setOnCheckedChangeListener(((buttonView, isChecked) -> {
      if(isChecked){
        button.setBackgroundColor(red);
        switch(button.getTextOn().toString()){
          case "Location":
            triggerLoc = true;
            break;
          case "Time":
            triggerTime = true;
            break;
          case "Other People":
            triggerOPeople = true;
            break;
          case "Preceding Event":
            triggerPEvent = true;
            break;
          case "Emotional State":
            triggerEState = true;
            break;
          case "morning":
            timeMorn = true;
            break;
          case "Afternoon":
            timeAft = true;
            break;
          case "evening":
            timeEve = true;
            break;
          case "night":
            timeNight = true;
            break;
        }
      } else {
        button.setBackgroundColor(blue);
        switch(button.getTextOn().toString()){
          case "Location":
            triggerLoc = false;
            break;
          case "Time":
            triggerTime = false;
            break;
          case "Other People":
            triggerOPeople = false;
            break;
          case "Preceding Event":
            triggerPEvent = false;
            break;
          case "Emotional State":
            triggerEState = false;
            break;
          case "morning":
            timeMorn = false;
            break;
          case "Afternoon":
            timeAft = false;
            break;
          case "evening":
            timeEve = false;
            break;
          case "night":
            timeNight = false;
            break;
        }
      }
    }));
  }
}
