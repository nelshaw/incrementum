package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.mongodb.Block;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ViewHabitActivity extends AppCompatActivity {
    List<String> habitNames;
    ListView listView;
    ListAdapter listAdapter;
    Button addButton;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        //list for all habit entries from MongoDB
        habitNames = new ArrayList<>();

        //fill habitNames list
        getAllEntries();

        //wait until habitNames list is filled
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        addButton = findViewById(R.id.AddHabit);
        backButton = findViewById(R.id.back_button);
        listView = findViewById(R.id.myHabits);

        //create list adapter for ListView
        listAdapter = new ArrayAdapter<>(this, R.layout.simplerow, habitNames);

        //attach adapter to ListView
        listView.setAdapter(listAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddHabitActivity();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapActivity();
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

    public void getAllEntries() {


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

        results.forEach(new Block() {
            @Override
            public void apply(Object item) {
                Document doc = (Document) item;
                ArrayList<Document> habits = (ArrayList<Document>) doc.get("habits");
                for (Document s : habits) {
                    String habitName = (String) s.get("name");

                    //append habit to habitNames list
                    habitNames.add(habitName);
                    Log.d("Habit:", habitName);
                }
                //Log.d("Habit:", names.toString());
//                //list.append(names.toString());
//                for (String s : names) {
//                    list.append(s + "\n");
//
//                }
            }
        });
    }
}
