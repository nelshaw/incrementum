package com.example.incrementum;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.mongodb.Block;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ViewHabitActivity extends AppCompatActivity {
    //TextView list;
ArrayList<String> habits = new ArrayList<>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);
        ListView list = findViewById(R.id.list);
         adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,habits);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ViewHabitActivity.this,habits.get(position),Toast.LENGTH_SHORT).show();
            }
        });
        Button addButton = findViewById(R.id.AddHabit);
        Button backButton = findViewById(R.id.back_button);
        // = findViewById(R.id.list);
        //list.setText("");
        //getAllEntries();
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

        new Thread(new Runnable(){
            public void run()
            {
                final StitchAppClient client =
                        Stitch.getAppClient("incrementum-xjkms");

                final RemoteMongoClient mongoClient =
                        client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

                final RemoteMongoCollection<Document> coll =
                        mongoClient.getDatabase("Incrementum").getCollection("Habits");

                Document filterDoc = new Document();

                RemoteFindIterable <Document>  results = coll.find(filterDoc)
                        .projection(
                                new Document());

                results.forEach(item ->{
                    try{
                        JSONObject obj = new JSONObject(item.toJson());
                        String habit = obj.getString("name");
                        habits.add(habit);
                        adapter.notifyDataSetChanged();
                        Log.d("*************",habit);
                    }
                    catch(JSONException e){
                        Log.d("JSON exception:",e.toString());
                    }
                });
            }

        }).start();
    }





    public void openAddHabitActivity() {
        Intent intent = new Intent(this, AddHabitActivity.class);
        startActivity(intent);
    }

    public void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void  getAllEntries() {

        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Habits");

        Document filterDoc = new Document();

        RemoteFindIterable <Document> results = coll.find(filterDoc)
                .projection(
                        new Document());

        results.forEach(item ->{
            try{
                JSONObject obj = new JSONObject(item.toJson());
                String habit = obj.getString("name");
                habits.add(habit);
                adapter.notifyDataSetChanged();
            }
            catch(JSONException e){
                Log.d("JSON exception:",e.toString());
            }
        });

    }

}
