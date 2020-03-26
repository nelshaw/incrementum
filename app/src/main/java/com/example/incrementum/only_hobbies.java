package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.mongodb.client.model.Filters;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class only_hobbies extends AppCompatActivity {
//fields
    String username;
    String completeString;
    //array list to hold hobbies
    ArrayList<String> hobbies;
    ArrayAdapter<String> adapter;
    String id;
    String email;
    EditText hobby;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_hobbies);
        ListView list = findViewById(R.id.list);

        hobby = findViewById(R.id.newh);
        hobbies = new ArrayList<>();
        //make adapter for list
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,hobbies);
        //set adapter for list
        list.setAdapter(adapter);



        UserInfo user = (UserInfo) getApplication();
        username = user.getUserName();
        id = user.getUserId();
        email = user.getEmail();
        completeString = "Hobby Stats For " + username;


        TextView userText = findViewById(R.id.usertext);
        userText.setText(completeString);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    delete(hobbies.get(position));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        final Button backButton = findViewById(R.id.profile);
        final Button addButton = findViewById(R.id.add);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addHobby();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        DatabaseLoad load = new DatabaseLoad();
        load.execute();
    }

    private class DatabaseLoad extends AsyncTask<Void,Void,Void> {
        RemoteFindIterable <Document>  results;
        @Override
        protected void onPreExecute() {

            final RemoteMongoCollection<Document> coll =
                    DatabaseHelper.mongoClient.getDatabase("Incrementum").getCollection("Users");

            results = coll.find(Filters.eq("email", email))
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
                    JSONArray hobbylist = new JSONArray();
                    hobbylist = obj.getJSONArray("hobbies");

                    Log.d("hooooobiesssss",hobbylist.toString());
                    Log.d("*************",obj.toString());
                    ArrayList<String> temp = new ArrayList<String>();
                    for(int i =0;i<hobbylist.length();i++)
                    {
                       //JSONObject tempObj;
                        //tempObj = hobbylist.getJSONObject(i);
                        temp.add(hobbylist.getString(i));
                    }

                    for(String s:temp)
                    {
                        hobbies.add(s);
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
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {   Log.d("POST","************************************");
                adapter.notifyDataSetChanged();
            });
            super.onPostExecute(aVoid);
        }
    }

    public void goBack() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void addHobby() throws InterruptedException {
        //check if field empty
        if(hobby.getText().toString().length()==0||hobby.getText().toString().equals("i.e. Running"))
        {
            Toast.makeText(this.getBaseContext(),"Please enter a hobby", Toast.LENGTH_LONG).show();
            return;
        }

        hobbies.add(hobby.getText().toString());

        Document query = new Document().append("email",email);

        Hobby temp = new Hobby(hobby.getText().toString(),0);



        Document update = new Document().append("$push",new Document()
        .append("hobbies",hobby.getText().toString()));




        final RemoteMongoCollection<Document> coll =
                DatabaseHelper.mongoClient.getDatabase("Incrementum").getCollection("Users");
        final Task<RemoteUpdateResult> updateTask = coll.updateOne(query,update);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
        Thread.sleep(300);
    }

    private class Hobby{
        private String name;
        private int times;


        public Hobby(String name, int times) {
            this.name = name;
            this.times = times;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }
    }

    public void delete(String name) throws InterruptedException {
        Document query = new Document().append("email",email);
        final RemoteMongoCollection<Document> coll =
                DatabaseHelper.mongoClient.getDatabase("Incrementum").getCollection("Users");


        Document update = new Document().append("$pull",new Document()
                .append("hobbies",name));


        final Task<RemoteUpdateResult> updateTask = coll.updateOne(query,update);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
        Thread.sleep(500);
    }



}
