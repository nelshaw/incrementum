package com.example.incrementum;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mongodb.client.model.Filters;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewHabitActivity extends AppCompatActivity {
    //TextView list;
    ArrayList<String> habits;
    ArrayList<String> habitsId;
    ArrayAdapter<String> adapter;
    Button refresh;
    String email;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);
        ListView list = findViewById(R.id.list);
        habits = new ArrayList<>();
        Intent intent = getIntent();

        UserInfo user = (UserInfo) getApplication();

        email = user.getEmail();
        id = user.getUserId();
        Toast.makeText(this.getBaseContext(),email, Toast.LENGTH_LONG).show();
        habitsId = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,habits);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendData(habitsId.get(position));
            }
        });
        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });

        Button addButton = findViewById(R.id.AddHabit);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddHabitActivity();
            }
        });

        DatabaseLoad load = new DatabaseLoad();
        load.execute();
        //getHabits();

        //Initalize and Assign Value
        BottomNavigationView bottomNavigationView2 = findViewById(R.id.bottom_navigation2);

        //Set home selected
        bottomNavigationView2.setSelectedItemId(R.id.habit_nav);

        //Perform ItemSelectedList
        bottomNavigationView2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.profile_nav:
                        finish();
                        startActivity(new Intent(getApplicationContext()
                                , ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.habit_nav:
//                        finish();
//                        startActivity(new Intent(getApplicationContext()
//                                ,ViewHabitActivity.class));
//                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
    }

    private class DatabaseLoad extends AsyncTask<Void,Void,Void>{
        RemoteFindIterable <Document>  results;
        @Override
        protected void onPreExecute() {

            final RemoteMongoCollection<Document> coll =
                    DatabaseHelper.mongoClient.getDatabase("Incrementum").getCollection("Habits");

            results = coll.find(Filters.eq("userId", id))
                    .projection(
                            new Document());
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("BACKGROUND","************************************");
            results.forEach(item ->{
                try{
                    JSONObject obj = new JSONObject(item.toJson());
                    String habit = obj.getString("name");
                    String _id = obj.getString("_id");
                    Log.d("*************",obj.toString());
                    habits.add(habit);
                    habitsId.add(_id);
                    Log.d("*************",_id);
                }
                catch(JSONException e){
                    Log.d("JSON exception:",e.toString());
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            runOnUiThread(() -> {   Log.d("POST","************************************");
                adapter.notifyDataSetChanged();
            });
            super.onPostExecute(aVoid);
        }
    }

    public void openAddHabitActivity() {
        Intent intent = new Intent(this, AddHabitActivity.class);
        startActivity(intent);
    }
    public void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
    public void sendData(String name){
        Intent intent = new Intent(getApplicationContext(),CalendarActivity.class);
        //get only habit id
        String habitId = name.split(":")[1].split("\"")[1];
        intent.putExtra("habit", habitId);
        startActivity(intent);
    }
}
