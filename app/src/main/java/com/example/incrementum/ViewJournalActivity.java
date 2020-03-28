package com.example.incrementum;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ViewJournalActivity extends AppCompatActivity {

  @InjectView(R.id.addJournal)
  Button addBtn;
  @InjectView(R.id.myJournals)
  ListView listView;
  @InjectView(R.id.title)
  TextView title;
  @InjectView(R.id.updateJournal)
  EditText updateJournal;
  @InjectView(R.id.updateBtn)
  Button updateBtn;

  // User information
  UserInfo user;
  String user_id, habit_id;

  // Variables to display journals
  ArrayList<String> journalEntries;
  ArrayAdapter<String> arrayAdapter;
  HashMap<Integer, Date> entriesInformation;
  boolean isUpdateVisible;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_journal);
    ButterKnife.inject(this);

    journalEntries = new ArrayList<>();
    entriesInformation = new HashMap<>();

    arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, journalEntries);
    listView.setAdapter(arrayAdapter);

    updateJournal.setVisibility(View.INVISIBLE);
    updateBtn.setVisibility(View.INVISIBLE);
    isUpdateVisible = false;

    // Get user information
    user = (UserInfo) getApplication();

    // Get user_id from login to view journal entries for that user
    user_id = user.getUserId();

    // Add user's name to title
    // This will grab from database
    title.append(" " + user.getUserName());

    // pull values from MongoDB
    DatabaseLoad load = new DatabaseLoad();
    load.execute();

    // by clicking listView entry, allow user to edit a journal on click
    listView.setOnItemClickListener((parent, view, position, id) -> {
      if (!isUpdateVisible) {
        updateJournal.setVisibility(View.VISIBLE);
        updateBtn.setVisibility(View.VISIBLE);
        isUpdateVisible = true;
      } else {
        updateJournal.setVisibility(View.INVISIBLE);
        updateBtn.setVisibility(View.INVISIBLE);
        isUpdateVisible = false;
      }
      updateEntry(position);
      Log.d("VIEW", "Position: " + position + " id: " + id);
    });

    //Initalize and Assign Value
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

    //Set home selected
    bottomNavigationView.setSelectedItemId(R.id.journal_nav);

    //Perform ItemSelectedList
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
          case R.id.calendar_nav:
            finish();
            startActivity(new Intent(getApplicationContext()
              , CalendarActivity.class));
            overridePendingTransition(0, 0);
            return true;

          case R.id.journal_nav:
//            finish();
//            startActivity(new Intent(getApplicationContext()
//                    ,ViewJournalActivity.class));
//            overridePendingTransition(0,0);
            return true;

          case R.id.habit_nav:
            finish();
            startActivity(new Intent(getApplicationContext()
              , ViewHabitActivity.class));
            overridePendingTransition(0, 0);
            return true;

          case R.id.map_nav:
            startActivity(new Intent(getApplicationContext()
              , MapActivity.class));
            overridePendingTransition(0, 0);
            finish();
            return true;
        }
        return false;
      }
    });

    // Direct to add journal entry
    addBtn.setOnClickListener(v -> openAddJournalActivity());

  }

  public void openAddJournalActivity() {
    Intent intent = new Intent(this, AddJournalActivity.class);
    startActivity(intent);
  }

  public void updateEntry(int position) {

    updateBtn.setOnClickListener(v -> {

      // Call base to make query
      DatabaseHelper.updateJournal(entriesInformation, position, updateJournal.getText().toString());

      // Refresh the page
      Intent intent = new Intent(this, ViewJournalActivity.class);
      startActivity(intent);
    });
  }

  private class DatabaseLoad extends AsyncTask<Void,Void,Void> {
    RemoteFindIterable<Document> results;

    @Override
    protected void onPreExecute() {
      Log.d("PRE","************************************");

      // Get habit_id from habit page to view journal entries for that habit
      habit_id = user.getHabitId();

      while(true){
        if(habit_id != null && !habit_id.equals(""))
          break;
      }

      Log.d("Habit id*****", habit_id);

      super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
      Log.d("BACKGROUND","************************************");

      DatabaseHelper.getAllJournals(journalEntries, entriesInformation, user_id, habit_id);

      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      runOnUiThread(() -> {Log.d("POST","************************************");});

      arrayAdapter.notifyDataSetChanged();

      super.onPostExecute(aVoid);
    }
  }
}
