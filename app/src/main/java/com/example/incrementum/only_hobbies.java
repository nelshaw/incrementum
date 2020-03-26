package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.client.model.Filters;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class only_hobbies extends AppCompatActivity {

    String username;
    String completeString;
    TextView h1;
    TextView h2;
    TextView h3;
    TextView th;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_hobbies);


        UserInfo user = (UserInfo) getApplication();

        username = user.getUserName();
        completeString = "Hobby Stats For " + username;


        TextView userText = findViewById(R.id.usertext);
        userText.setText(completeString);

        h1 = findViewById(R.id.H1);
        h1.setVisibility(View.INVISIBLE);
        h2 = findViewById(R.id.H2);
        h2.setVisibility(View.INVISIBLE);
        h3 = findViewById(R.id.H3);
        h3.setVisibility(View.INVISIBLE);
        th = findViewById(R.id.TH);

        getData(username);


        final Button backButton = findViewById(R.id.mainbutton);
        final Button habitButton = findViewById(R.id.habitbutton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
                sendData(username, 1);
            }
        });

        habitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHabit();
                sendData(username, 2);
            }
        });
    }

    public void getData(String username) {
        RemoteFindIterable<Document> results;
        Toast.makeText(getBaseContext(), username, Toast.LENGTH_LONG).show();

        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");
        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Users");
        results = coll.find(Filters.eq("username", username))
                .projection(
                        new Document());
        results.forEach(item -> {
            try {
                JSONObject obj = new JSONObject(item.toJson());
                JSONArray hobbies = obj.getJSONArray("hobbies");
                for (int i = 0; i < hobbies.length(); i++) {
                    JSONObject object = new JSONObject(hobbies.get(i).toString());
                    String hobby = object.getString("name");
                    fillTextBox(hobby, i);

                    JSONObject nextObject = new JSONObject(hobbies.get(i + 1).toString());
                    String nexthobby = nextObject.getString("name");
                    if(nexthobby == null)
                    {
                        return;
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void fillTextBox(String hobby, int num)
    {
        String newString;
        if(num == 0)
        {
            newString = "Hobby One: " + hobby;
            h1.setText(newString);
            h1.setVisibility(View.VISIBLE);
            th.setText("Total Hobbies: One");
        }
        else if (num == 1)
        {
            newString = "Hobby Two: " + hobby;
            h2.setText(newString);
            h2.setVisibility(View.VISIBLE);
            th.setText("Total Hobbies: Two");

        }
        else if (num == 2)
        {
            newString = "Hobby Three: " + hobby;
            h3.setText(newString);
            h3.setVisibility(View.VISIBLE);
            th.setText("Total Hobbies: Three");
        }
    }


    public void sendData(String username, int location)
    {
        if(location == 1) {
            Intent intent = new Intent(getApplicationContext(), Hobby_Stats.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
        else if(location == 2) {
            Intent intent = new Intent(getApplicationContext(), only_habits.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }

    public void goBack() {
        Intent intent = new Intent(this, Hobby_Stats.class);
        startActivity(intent);
    }

    public void goHabit() {
        Intent intent = new Intent(this, only_habits.class);
        startActivity(intent);
    }
}
