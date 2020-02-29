package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.mongodb.Block;
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
import java.util.List;


public class ViewHabitActivity extends AppCompatActivity {
    List<String> names = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);
        Button addButton = findViewById(R.id.AddHabit);
        Button backButton = findViewById(R.id.back_button);
        getAllEntries();
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openAddHabitActivity();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openMapActivity();
            }
        });

    }

    public void openAddHabitActivity(){
        Intent intent = new Intent(this, AddHabitActivity.class);
        startActivity(intent);
    }

    public void openMapActivity(){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }


    public void getAllEntries(){

        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Users");

        Document filterDoc = new Document();
//      .append("entry", new Document().append("$eq", true));

        RemoteFindIterable results = coll.find(filterDoc)
                .projection(
                        new Document()
                                .append("habits", 1)
                                .append("_id", 0));

        //RemoteFindIterable results = coll.find(filterDoc);

        Log.d("Habit", "*************************************test test test");
        Log.d("Habit", String.valueOf(results));


        results.forEach(new Block() {
            int i = 1;
            @Override
            public void apply(Object item) {
                Document doc = (Document) item;

                ArrayList<Document> habits =  (ArrayList<Document>) doc.get("habits");

                for (Document s :habits)
                {
                    String habitName =(String) s.get("name");
                    names.add(habitName);
                }


                Log.d("Habit", names.toString());

            }
        });
    }









}
