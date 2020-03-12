package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_a_habit);
        Intent intent = getIntent();
        String _getData = intent.getStringExtra("sss");

        name = findViewById(R.id.name);
        triggers = findViewById(R.id.triggersListView);
        times = findViewById(R.id.timesListView);
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
                  habitName = obj.getString("name");
                 JSONArray triggerlist = new JSONArray();
                  triggerlist = obj.getJSONArray("Triggers");
                  triggers.setText(triggerlist.toString());

                    JSONArray timesList = new JSONArray();
                    timesList = obj.getJSONArray("Times");
                    times.setText(timesList.toString());
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
            });
            super.onPostExecute(aVoid);
        }
    }













}