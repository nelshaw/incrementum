package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mongodb.Block;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewHabitActivity extends AppCompatActivity {
    List<String> names = new ArrayList<>();
    TextView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);
        list = findViewById(R.id.myhabits);
        Button addButton = findViewById(R.id.AddHabit);
        Button backButton = findViewById(R.id.back_button);
        //list.setText("");
        getAllEntries();
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
                mongoClient.getDatabase("Incrementum").getCollection("Habits");

        Document filterDoc = new Document();

        RemoteFindIterable <Document> results = coll.find(filterDoc)
                .projection(
                        new Document()
                                .append("description", 0)
                                .append("_id", 0)
                                .append("userId",0)
                                .append("length",0)
                                .append("isActive",0));

        Log.d("Habits", "*************************************");
        Log.d("Habits", String.valueOf(results));


        results.forEach(item ->{
            try{
                JSONObject obj = new JSONObject(item.toJson());
                Log.d("objjjjjjjjjjjjj",obj.toString());
            }

            catch(JSONException e){
                Log.d("JSON exception:",e.toString());
            }


        });

        while(list.getText()==""){
            getAllEntries();
        }
    }
}
