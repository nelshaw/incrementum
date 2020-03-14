package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.client.model.Filters;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewAHabit extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewHabitActivity.class);
        startActivity(intent);
    }


    TextView name;
    TextView triggers;
    TextView times;
    String habitName;
    String _getData;
    TextView descriptionText;
    String description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_a_habit);
        Intent intent = getIntent();
        _getData  = intent.getStringExtra("habit");
        Toast.makeText(getBaseContext(),_getData, Toast.LENGTH_LONG).show();

        name = findViewById(R.id.name);
        triggers = findViewById(R.id.triggersListView);
        times = findViewById(R.id.timesListView);
        descriptionText = findViewById(R.id.description);
        DatabaseLoad load = new DatabaseLoad();
        load.execute();
    }
    private class DatabaseLoad extends AsyncTask<Void,Void,Void> {
        RemoteFindIterable <Document>  results;
        @Override
        protected void onPreExecute() {
            final StitchAppClient client =
                    Stitch.getAppClient("incrementum-xjkms");
            final RemoteMongoClient mongoClient =
                    client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
            final RemoteMongoCollection<Document> coll =
                    mongoClient.getDatabase("Incrementum").getCollection("Habits");
            results = coll.find(Filters.eq("name",_getData))
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
                    habitName = obj.getString("name");
                    JSONArray triggerlist = new JSONArray();
                    triggerlist = obj.getJSONArray("Triggers");
                    JSONArray timesList = new JSONArray();
                    timesList = obj.getJSONArray("Times");
                    name.setText(habitName);
                    description = obj.getString("description");
                    descriptionText.setText(description);
                    ArrayList<String> tempTriggers = new ArrayList<String>();
                    for(int i =0;i<triggerlist.length();i++)
                    {
                        tempTriggers.add(triggerlist.get(i).toString());
                        Log.d("*********************",triggerlist.get(i).toString());
                    }
                    for(String s:tempTriggers)
                    {
                        triggers.append(s+"\n");
                    }
                    ArrayList<String> tempTimes= new ArrayList<String>();
                    for(int i =0;i<timesList.length();i++)
                    {
                        tempTimes.add(timesList.get(i).toString());
                        Log.d("*********************",timesList.get(i).toString());
                    }
                    for(String s:tempTimes)
                    {
                        times.append(s+"\n");
                    }
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
                name.setText(habitName);
                descriptionText.setText(description);

            });
            super.onPostExecute(aVoid);
        }
    }
}