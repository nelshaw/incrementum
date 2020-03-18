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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);
        ListView list = findViewById(R.id.list);
        habits = new ArrayList<>();
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
            final StitchAppClient client =
                    Stitch.getAppClient("incrementum-xjkms");

            final RemoteMongoClient mongoClient =
                    client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

            final RemoteMongoCollection<Document> coll =
                    mongoClient.getDatabase("Incrementum").getCollection("Habits");

            Document filterDoc = new Document();

            results = coll.find(filterDoc)
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
                    adapter.notifyDataSetChanged();
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

    public void getHabits(){

        RemoteFindIterable <Document>  results;

        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Habits");

        Document filterDoc = new Document();

        results = coll.find(filterDoc)
                .projection(
                        new Document());

        results.forEach(item ->{
            try{
                JSONObject obj = new JSONObject(item.toJson());
                String habit = obj.getString("name");
                Log.d("*************",obj.toString());
                habits.add(habit);
                adapter.notifyDataSetChanged();
                Log.d("*************",habit);
            }
            catch(JSONException e){
                Log.d("JSON exception:",e.toString());
            }
        });
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
        intent.putExtra("habit", name);
        startActivity(intent);
    }
}
