package com.example.incrementum;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mongodb.client.model.Filters;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewHabitActivity extends AppCompatActivity {
    //TextView list;
    ArrayList<String> habits;
    ArrayList<String> habitsId;
    ArrayAdapter<String> adapter;
    String email;
    String id;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);
        list = findViewById(R.id.list);
        habits = new ArrayList<>();

        UserInfo user = (UserInfo) getApplication();

        email = user.getEmail();
        id = user.getUserId();

        habitsId = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,habits);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sendData(habitsId.get(position));
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(ViewHabitActivity.this);
                dialog.setMessage("Are you sure you want to delete this habit?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    delete(habitsId.get(position));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                dialog.show();
                return true;
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
                    String _id = obj.getJSONObject("_id").getString("$oid");
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
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while(list.getItemAtPosition(0)==null)
                {
                    adapter.notifyDataSetChanged();
                }

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

    public void delete(String id) throws InterruptedException {


//collection.deleteOne(new Document("_id", new ObjectId("57a49c6c33b10927ff09623e")));

        final RemoteMongoCollection<Document> coll =
                DatabaseHelper.mongoClient.getDatabase("Incrementum").getCollection("Habits");

        coll.deleteOne(new Document("_id",new ObjectId(id)));
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
        Thread.sleep(100);
    }
}
