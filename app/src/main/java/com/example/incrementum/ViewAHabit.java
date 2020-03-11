package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String _getData = intent.getStringExtra("sss");

        TextView name = findViewById(R.id.name);
        TextView triggers = findViewById(R.id.triggersListView);
        TextView times = findViewById(R.id.timesListView);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_a_habit);
        TextView test = findViewById(R.id.triggersListView);
        test.setText(_getData);

        //new thread to get habit info
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

                RemoteFindIterable<Document> results = coll.find(filterDoc)
                        .projection(
                                new Document().append("name",1).append("Triggers",1).append("Times",1));


                results.forEach(item ->{
                    try{
                        JSONObject obj = new JSONObject(item.toJson());
                      String  Habitname = obj.getString("name");
                      name.setText(Habitname);
                      triggers.setText(obj.toString());

                    }
                    catch(JSONException e){
                        Log.d("JSON exception:",e.toString());
                    }
                });
            }
        }).start();







    }





}
