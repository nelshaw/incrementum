package com.example.incrementum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
  @InjectView(R.id.precedingEvent) ToggleButton precedingEvent;
  @InjectView(R.id.emotionalState) ToggleButton emotionalState;
  @InjectView(R.id.location) ToggleButton location;
  @InjectView(R.id.time) ToggleButton time;
  @InjectView(R.id.morning) ToggleButton morning;
  @InjectView(R.id.afternoon) ToggleButton afternoon;
  @InjectView(R.id.evening) ToggleButton evening;
  @InjectView(R.id.night) ToggleButton night;

  String user_id;
  Date date;

  // Trigger values
  boolean triggerLoc= false, triggerPEvent = false, triggerEState = false, triggerOPeople = false,
    triggerTime = false;
  // Time values
  boolean timeMorn = false, timeEve = false, timeAft = false, timeNight = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_journal);
    ButterKnife.inject(this);

    // Get user id from user who logged in successfully
    user_id = LoginActivity.user_id;

    // Get date from calendar
    date = CalendarActivity.dateSelected;

    int red = Color.rgb(237, 177, 162);
    int blue = Color.rgb(216, 242, 243);

    // Time buttons
    morning.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        morning.setBackgroundColor(red);
        timeMorn = true;
      } else {
        morning.setBackgroundColor(blue);
        timeMorn = false;
      }
    });

    evening.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        evening.setBackgroundColor(red);
        timeEve = true;
      } else {
        evening.setBackgroundColor(blue);
        timeEve = false;
      }
    });

    afternoon.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        afternoon.setBackgroundColor(red);
        timeAft = true;
      } else {
        afternoon.setBackgroundColor(blue);
        timeAft = false;
      }
    });

    night.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        night.setBackgroundColor(red);
        timeNight = true;
      } else {
        night.setBackgroundColor(blue);
        timeNight = false;
      }
    });

    // Trigger buttons
    precedingEvent.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        precedingEvent.setBackgroundColor(red);
        triggerPEvent = true;
      } else {
        precedingEvent.setBackgroundColor(blue);
        triggerPEvent = false;
      }
    });

    emotionalState.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        emotionalState.setBackgroundColor(red);
        triggerEState = true;
      } else {
        emotionalState.setBackgroundColor(blue);
        triggerEState = false;
      }
    });

    otherPeople.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        otherPeople.setBackgroundColor(red);
        triggerOPeople = true;
      } else {
        otherPeople.setBackgroundColor(blue);
        triggerOPeople = false;
      }
    });

    time.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        time.setBackgroundColor(red);
        triggerTime = true;
      } else {
        time.setBackgroundColor(blue);
        triggerTime = false;
      }
    });

    location.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        location.setBackgroundColor(red);
        triggerLoc = true;
      } else {
        location.setBackgroundColor(blue);
        triggerLoc = false;
      }
    });

    // Save entry and direct to view all journals
    saveBtn.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
          openViewJournalActivity();
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
    final StitchAppClient client =
      Stitch.getAppClient("incrementum-xjkms");

    final RemoteMongoClient mongoClient =
      client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

    final RemoteMongoCollection<Document> coll =
      mongoClient.getDatabase("Incrementum").getCollection("Journals");

    // Create new document from information and entry submitted
    // Add triggers to database
    Document doc = new Document()
      .append("user_id", user_id)
      .append("date", date)
      .append("entry", entry.getText().toString());

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
  }
}
