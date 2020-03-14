package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

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

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddHabitOccurrenceActivity extends AppCompatActivity {

  @InjectView(R.id.moodSeekBar)  SeekBar seekBar;
  @InjectView(R.id.backBtn)  Button backBtn;
  @InjectView(R.id.nextBtn) Button nextBtn;
  @InjectView(R.id.todayActivity) EditText todayActivity;

  int progressChangedValue = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_habit_occurance);
    ButterKnife.inject(this);

    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      // Get value of current seek bar
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progressChangedValue = progress;
      }

      public void onStartTrackingTouch(SeekBar seekBar) {
        progressChangedValue = seekBar.getProgress();
      }

      public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d("SEEK BAR", "Progress is " + progressChangedValue);
      }
    });

    nextBtn.setOnClickListener(v -> openHabitEffectActivity());
    backBtn.setOnClickListener(v -> openCalendarActivity());

  }

  public void openHabitEffectActivity(){
    if(validate()) {
      addEntry();
      Intent intent = new Intent(this, HabitEffectActivity.class);
      startActivity(intent);
    }
  }

  public void openCalendarActivity(){
    Intent intent = new Intent(this, CalendarActivity.class);
    startActivity(intent);
  }

  public boolean validate(){
    if(todayActivity.getText().toString().length() == 0){
      return false;
    }
    return true;
  }

  // Add to journal entry
  public void addEntry(){

    // Connect to MongoDB client
    final StitchAppClient client =
      Stitch.getAppClient("incrementum-xjkms");

    final RemoteMongoClient mongoClient =
      client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

    final RemoteMongoCollection<Document> coll =
      mongoClient.getDatabase("Incrementum").getCollection("Reflection");

    // Create new document from information and entry submitted
    // Add triggers to database
    Document doc = new Document()
      .append("Mood", progressChangedValue)
      .append("Entry", todayActivity.getText().toString());

    // Insert document
    final Task<RemoteInsertOneResult> insert = coll.insertOne(doc);

    insert.addOnCompleteListener(task -> {
      if (task.isSuccessful()){
        Log.d("STITCH", String.format("success inserting: %s",
          task.getResult().getInsertedId()));
      }
      else{
        Log.d("STITCH", "Unsuccessful adding reflection entry");
      }
    });
  }
}
