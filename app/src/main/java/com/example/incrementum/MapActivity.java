package com.example.incrementum;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;


public class MapActivity extends AppCompatActivity {

  Button habit, journal, logout;
  ImageView map;
  int nDays;
  String User_id;
  String _getHabitId;
  JSONArray days;
  UserInfo user;
  final RemoteMongoCollection<Document> coll = DatabaseHelper.mongoClient.getDatabase("Incrementum").getCollection("Calendar");

private class DatabaseLoad extends AsyncTask<Void,Void,Void> {
        RemoteFindIterable<Document> results;

        @Override
        protected void onPreExecute() {
            Log.d("PRE","************************************");
            _getHabitId = user.getHabitId();
            while(true){
                if(_getHabitId != null && !_getHabitId.equals(""))
                    break;
            }
            Log.d("Habit id*****", _getHabitId);
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            nDays = 1;
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
            Log.d("RESULTS:", String.valueOf(results.first().isSuccessful()));
            results.forEach( result -> {
                Log.d("HI", "HI");
                try{
                    JSONObject obj = new JSONObject(result.toJson());
                    days = obj.getJSONArray("Days");
                    int i = days.length() - 1;
                    boolean streakStart = false;
                    try {
                        while (i > 0) {
                            JSONObject object = new JSONObject(days.get(i).toString());
                            boolean stat = object.getBoolean("Status");
                            Log.d("DAY " + i + ":", String.valueOf(stat));
                            if (streakStart) {
                                if (stat) nDays++;
                                else break;
                            } else {
                                if (stat) streakStart = true;
                            }
                            i--;
                        }
                    }
                    catch(Exception e){
                        Log.e("ERROR", e.getMessage());
                    }
////        Log.d("HI", days.toString());
//                    boolean startStreak = false;
//                    for (int i =  days.length() - 1; i > 0; i--) {
//                        JSONObject object = new JSONObject(days.get(i).toString());
//                        boolean stat = object.getBoolean("Status");
//                        Log.d("DAY " + i + ":", String.valueOf(stat));
//
//                    }
                }
                catch(Exception e) {
                    Log.e("ERROR!!", e.getMessage());
                }

                Log.d("STREAK::::::::::", String.valueOf(nDays));
                if(nDays ==  1)
                    map.setImageDrawable(getResources().getDrawable(R.drawable.map1));
                else if(nDays ==  2)
                    map.setImageDrawable(getResources().getDrawable(R.drawable.map2));
                else if(nDays ==  3)
                    map.setImageDrawable(getResources().getDrawable(R.drawable.map3));
                else if(nDays ==  4)
                    map.setImageDrawable(getResources().getDrawable(R.drawable.map4));
                else if(nDays ==  5)
                    map.setImageDrawable(getResources().getDrawable(R.drawable.map5));
                else if(nDays ==  6)
                    map.setImageDrawable(getResources().getDrawable(R.drawable.map6));
                else if(nDays ==  7)
                    map.setImageDrawable(getResources().getDrawable(R.drawable.map7));
                else if(nDays ==  8)
                    map.setImageDrawable(getResources().getDrawable(R.drawable.map8));
                else if(nDays ==  9)
                    map.setImageDrawable(getResources().getDrawable(R.drawable.map9));
                else if(nDays ==  10)
                    map.setImageDrawable(getResources().getDrawable(R.drawable.map10));
                else if(nDays ==  11)
                    map.setImageDrawable(getResources().getDrawable(R.drawable.map11));
                else if(nDays ==  12)
                    map.setImageDrawable(getResources().getDrawable(R.drawable.map12));
                else if(nDays ==  13)
                    map.setImageDrawable(getResources().getDrawable(R.drawable.map13));
                else if(nDays ==  14)
                    map.setImageDrawable(getResources().getDrawable(R.drawable.map14));
                else
                    map.setImageDrawable(getResources().getDrawable(R.drawable.logo));
                finish();
                startActivity(getIntent());
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            runOnUiThread(() -> {   Log.d("POST","************************************");
            });


            super.onPostExecute(aVoid);
        }
    }


@Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);
    Log.d("MAPACTIVITY", "START");
    map = findViewById(R.id.map);

    nDays = 1;
    user = (UserInfo) getApplication();
    User_id = user.getUserId();//    _getHabitId = "5e65ba9a1c9d440000d8a29d";
//
    //connect to collection
    DatabaseLoad load = new DatabaseLoad();
    load.execute();

    //Initalize and Assign Value
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

    //Set home selected
    bottomNavigationView.setSelectedItemId(R.id.map_nav);

    //Perform ItemSelectedList
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
          case R.id.calendar_nav:
            finish();
            startActivity(new Intent(getApplicationContext()
                    ,CalendarActivity.class));
            overridePendingTransition(0,0);
            return true;

          case R.id.journal_nav:
            finish();
            startActivity(new Intent(getApplicationContext()
                    ,ViewJournalActivity.class));
            overridePendingTransition(0,0);
            return true;

          case R.id.habit_nav:
            finish();
            startActivity(new Intent(getApplicationContext()
              ,ViewHabitActivity.class));
            overridePendingTransition(0,0);
            return true;

          case R.id.map_nav:
//            startActivity(new Intent(getApplicationContext()
//                    ,MapActivity.class));
//            overridePendingTransition(0,0);
//            finish();
            return true;
        }
        return false;
      }
    });

  }
}
