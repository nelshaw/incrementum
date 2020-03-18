package com.example.incrementum;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mongodb.client.model.Updates;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

  MaterialCalendarView calendarView;
  HashSet<CalendarDay> didNotDoHabitDates;
  HashSet<CalendarDay> didDoHabitDates;

  ImageButton didNotButton;
  ImageButton didButton;

  //test values
  //String User_id = "5e587cbed6292c4d1074b5d8";
  //String Habit_id = "5e65ba9a1c9d440000d8a29d";

  //get user id from login
  String User_id = LoginActivity.user_id;
  //get habit id from chosen habit
  String _getHabitId;

  static Date dateSelected;

  //connect to collection
  final RemoteMongoCollection<Document> coll =
    DatabaseHelper.mongoClient.getDatabase("Incrementum").getCollection("Calendar");

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);

    didNotDoHabitDates = new HashSet<>();
    didDoHabitDates = new HashSet<>();

    //pull values from MongoDB
    DatabaseLoad load = new DatabaseLoad();
    load.execute();

    calendarView = findViewById(R.id.calendarView);
    didNotButton = findViewById(R.id.didNotButton);
    didButton = findViewById(R.id.didButton);

    calendarView.state().edit()
      .setMaximumDate(CalendarDay.today())
      .commit();

    didNotButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        CalendarDay date = calendarView.getSelectedDate();
        String selectedDate = formatDate(date);

        Document query = new Document()
          .append("User_id", User_id)
          .append("Habit_id", _getHabitId);

        Document dayStatus = new Document()
          .append("Date", selectedDate)
          .append("Status", false);

        final Task<RemoteUpdateResult> update = coll.updateOne(query, Updates.addToSet("Days", dayStatus));

        update.addOnCompleteListener(new OnCompleteListener<RemoteUpdateResult>() {
          @Override
          public void onComplete(@NonNull Task<RemoteUpdateResult> task) {
            if (task.isSuccessful()) {
              Log.d("STITCH", String.format("success inserting: %s",
                task.getResult().getUpsertedId()));
            } else {
              Log.d("STITCH", "Unsuccessful");
            }
          }
        });
        finish();
        startActivity(getIntent());
      }
    });

    didButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        CalendarDay date = calendarView.getSelectedDate();
        String selectedDate = formatDate(date);

        Document query = new Document()
          .append("User_id", User_id)
          .append("Habit_id", _getHabitId);

        Document dayStatus = new Document()
          .append("Date", selectedDate)
          .append("Status", true);

        final Task<RemoteUpdateResult> update = coll.updateOne(query, Updates.addToSet("Days", dayStatus));

        update.addOnCompleteListener(new OnCompleteListener<RemoteUpdateResult>() {
          @Override
          public void onComplete(@NonNull Task<RemoteUpdateResult> task) {
            if (task.isSuccessful()) {
              Log.d("STITCH", String.format("success inserting: %s",
                task.getResult().getUpsertedId()));
            } else {
              Log.d("STITCH", "Unsuccessful");
            }
          }
        });

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        try {
          dateSelected = format.parse(selectedDate);
        } catch (ParseException e) {
          e.printStackTrace();
        }

        openAddHabitOccurrenceActivity();
        Log.d("CALENDAR", "date added " + dateSelected);
      }
    });
    //Initalize and Assign Value
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

    //Set home selected
    bottomNavigationView.setSelectedItemId(R.id.calendar_nav);

    //Perform ItemSelectedList
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
          case R.id.calendar_nav:
//                        startActivity(new Intent(getApplicationContext()
//                                ,CalendarActivity.class));
//                        overridePendingTransition(0,0);
            return true;

          case R.id.journal_nav:
            //finish();
            startActivity(new Intent(getApplicationContext()
              , ViewJournalActivity.class));
            overridePendingTransition(0, 0);
            return true;

          case R.id.map_nav:
            //finish();
            startActivity(new Intent(getApplicationContext()
              , MapActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
        return false;
      }
    });
  }

  //function to format date
  public String formatDate(CalendarDay date) {
    String day = date.getDay() + "";
    String month = (date.getMonth() + 1) + "";
    if (day.length() != 2)
      day = "0" + day;
    if (month.length() != 2)
      month = "0" + month;

    return date.getYear() + "-" + month + "-" + day;
  }

  //function to convert string to date
  public Date StringToDate(String s) {

    Date result = null;
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      result = dateFormat.parse(s);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return result;
  }

  private class DatabaseLoad extends AsyncTask<Void,Void,Void> {
    RemoteFindIterable<Document> results;
    String User_id = "5e587cbed6292c4d1074b5d8";

    @Override
    protected void onPreExecute() {
      Log.d("PRE","************************************");

      Intent intent = getIntent();

      _getHabitId = intent.getStringExtra("habit");

      while(true){
        if(_getHabitId != null && !_getHabitId.equals(""))
          break;
      }

      Log.d("Habit id*****", _getHabitId);

      super.onPreExecute();
    }
    @Override
    protected Void doInBackground(Void... voids) {
      Log.d("BACKGROUND","************************************");

      Document filterDoc = new Document()
              .append("User_id", User_id)
              .append("Habit_id", _getHabitId);

      //find all documents
      results = coll.find(filterDoc)
              .projection(
                      new Document()
                              .append("_id", 0)
                              .append("Habit_id", 0)
                              .append("User_id", 0));
      //for each document in the collection
      results.forEach(item -> {
        try {
          //convert document to json
          JSONObject obj = new JSONObject(item.toJson());

          //find days array
          JSONArray days = obj.getJSONArray("Days");

          //for indices in days array
          for (int i = 0; i < days.length(); i++) {
            //find date and status
            JSONObject object = new JSONObject(days.get(i).toString());
            String date = object.getString("Date");
            boolean stat = object.getBoolean("Status");

            //convert date into a CalendarDay
            CalendarDay day = CalendarDay.from(StringToDate(date));

            //based on status, add to hashset to make red or green highlight circles
            if (stat) {
              didDoHabitDates.add(day);
            } else {
              didNotDoHabitDates.add(day);
            }
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      });
      return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
      runOnUiThread(() -> {   Log.d("POST","************************************");
      });

      //green
      calendarView.addDecorator(new CalendarDecorator(getBaseContext(), Color.parseColor("#90EE90"), didNotDoHabitDates));
      //red
      calendarView.addDecorator(new CalendarDecorator(getBaseContext(), Color.parseColor("#F64D4E"), didDoHabitDates));

      calendarView.setSelectedDate(CalendarDay.today());

      super.onPostExecute(aVoid);
    }
  }

  //function to open add habit occurance
  public void openAddHabitOccurrenceActivity() {
    Intent intent = new Intent(this, AddHabitOccurrenceActivity.class);
    startActivity(intent);
  }
}
