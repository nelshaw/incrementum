package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mongodb.client.model.Filters;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

public class only_habits extends AppCompatActivity {

    String username;
    String completeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_habits);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        completeString = "Habit Stats For " + username;

        TextView userText = findViewById(R.id.usertext);
        userText.setText(completeString);


        final Button backButton = findViewById(R.id.mainbutton);
        final Button hobbyButton = findViewById(R.id.hobbybutton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
                sendData(username, 1);
            }
        });

        hobbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHobby();
                sendData(username, 2);
            }
        });
    }


    public void sendData(String username, int location)
    {
        if(location == 1) {
            Intent intent = new Intent(getApplicationContext(), Hobby_Stats.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
        else if(location == 2) {
            Intent intent = new Intent(getApplicationContext(), only_hobbies.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }

    public void goBack() {
        Intent intent = new Intent(this, Hobby_Stats.class);
        startActivity(intent);
    }

    public void goHobby() {
        Intent intent = new Intent(this, only_hobbies.class);
        startActivity(intent);
    }
}
