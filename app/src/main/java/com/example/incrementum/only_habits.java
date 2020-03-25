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
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class only_habits extends AppCompatActivity {

    String username;
    String completeString;
    String email;
    int counter = 0;

    TextView h1;
    TextView t1;
    TextView st1;
    TextView h2;
    TextView t2;
    TextView st2;
    TextView h3;
    TextView t3;
    TextView st3;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_habits);

        UserInfo user = (UserInfo) getApplication();
        username = user.getUserName();
        email = user.getEmail();

        completeString = "Habit Stats For " + username;

        TextView userText = findViewById(R.id.usertext);
        userText.setText(completeString);

        h1 = findViewById(R.id.H1);
        h1.setVisibility(View.INVISIBLE);
        h2 = findViewById(R.id.H2);
        h2.setVisibility(View.INVISIBLE);
        h3 = findViewById(R.id.H3);
        h3.setVisibility(View.INVISIBLE);

        t1 = findViewById(R.id.T1);
        t1.setVisibility(View.INVISIBLE);
        t2 = findViewById(R.id.T2);
        t2.setVisibility(View.INVISIBLE);
        t3 = findViewById(R.id.T3);
        t3.setVisibility(View.INVISIBLE);

        st1 = findViewById(R.id.ST1);
        st1.setVisibility(View.INVISIBLE);
        st2 = findViewById(R.id.ST2);
        st2.setVisibility(View.INVISIBLE);
        st3 = findViewById(R.id.ST3);
        st3.setVisibility(View.INVISIBLE);

        total = findViewById(R.id.TTL);

        getData(username);


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

    public void getData(String username) {
        RemoteFindIterable<Document> results;
        RemoteFindIterable<Document> habitresults;

        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");
        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");



        final RemoteMongoCollection<Document> habitcoll =
                mongoClient.getDatabase("Incrementum").getCollection("Habits");
        habitresults = habitcoll.find(Filters.eq("email",email))
                .projection(
                        new Document());
        habitresults.forEach(item -> {
            String name = "";
            try {

                JSONObject obj = new JSONObject(item.toJson());
                Log.d("**********",obj.toString());
                name = obj.getString("name");
                JSONArray triggArr = new JSONArray();
                triggArr = obj.getJSONArray("Triggers");
                JSONArray timesArr = new JSONArray();

                ArrayList<String> trigList = new ArrayList();
                for(int i = 0; i < triggArr.length(); i++)
                {
                    trigList.add(triggArr.get(i).toString());
                }

                ArrayList<String> timeList = new ArrayList();
                for(int i = 0; i < timesArr.length(); i++)
                {
                    timeList.add(timesArr.get(i).toString());
                }


            updateUI(name, trigList, timeList);

        } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendData(String username, int location)
    {
        if(location == 1) {
            Intent intent = new Intent(getApplicationContext(), Hobby_Stats.class);
            startActivity(intent);
        }
        else if(location == 2) {
            Intent intent = new Intent(getApplicationContext(), only_hobbies.class);
            startActivity(intent);
        }
    }

    public void updateUI(String name, ArrayList triggArr, ArrayList timesArr)
    {
        String newString;
        if(counter == 0) {
            newString = "Habit One: " + name;
            h1.setText(newString);
            h1.setVisibility(View.VISIBLE);

            newString = "Triggers: ";
            for(int i = 0; i < triggArr.size(); i++)
            {
                newString = newString + i + ": " + triggArr.get(i);
            }
            t1.setText(newString);
            t1.setVisibility(View.VISIBLE);

            newString = "Times: ";
            for(int i = 0; i < timesArr.size(); i++)
            {
                newString = newString + i + ": " + timesArr.get(i);
            }
            st1.setText(newString);
            st1.setVisibility(View.VISIBLE);

            total.setText("Total Habits: " + counter);
            counter++;
        } else if(counter == 1) {
            newString = "Habit Two: " + name;
            h2.setText(newString);
            h2.setVisibility(View.VISIBLE);

            newString = "Triggers: ";
            for(int i = 0; i < triggArr.size(); i++)
            {
                newString = newString + i + ": " + triggArr.get(i);
            }
            t2.setText(newString);
            t2.setVisibility(View.VISIBLE);

            newString = "Times: ";
            for(int i = 0; i < timesArr.size(); i++)
            {
                newString = newString + i + ": " + timesArr.get(i);
            }
            st2.setText(newString);
            st2.setVisibility(View.VISIBLE);

            total.setText("Total Habits: " + counter);
            counter++;
        } else if(counter == 2) {
            newString = "Habit Three: " + name;
            h3.setText(newString);
            h3.setVisibility(View.VISIBLE);

            newString = "Triggers: ";
            for(int i = 0; i < triggArr.size(); i++)
            {
                newString = newString + i + ": " + triggArr.get(i);
            }
            t3.setText(newString);
            t3.setVisibility(View.VISIBLE);

            newString = "Times: ";
            for(int i = 0; i < timesArr.size(); i++)
            {
                newString = newString + i + ": " + timesArr.get(i);
            }
            st3.setText(newString);
            st3.setVisibility(View.VISIBLE);

            total.setText("Total Habits: " + counter);
            counter++;
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
