package com.example.incrementum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.Block;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;

public class Log_Habits_Hobbies_Time_Activity extends AppCompatActivity {

    Button EnterButton;
    EditText HobbyText;
    EditText TimeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__habits__hobbies__time_);

        EnterButton = findViewById(R.id.addHabitsHobbiesTimeButton);
        HobbyText = findViewById(R.id.editText5);
        TimeText = findViewById(R.id.editText6);
        EnterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openHabitActivity();
            }
        });
    }

    public void openHabitActivity(){
        Intent intent = new Intent(this, AddHabitActivity.class);
        startActivity(intent);
    }

    public String getAllEntries(){

        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Users");

        Document doc = new Document().append("sensitive_time", TimeText.getText().toString()).append("hobbies", TimeText.getText().toString());

    final Task<RemoteInsertOneResult> insert = coll.insertOne(doc);
    insert.addOnCompleteListener(new OnCompleteListener<RemoteInsertOneResult>() {
      @Override
      public void onComplete(@NonNull Task<RemoteInsertOneResult> task) {
        if (task.isSuccessful()){
          Log.d("STITCH", String.format("success inserting: %s",
            task.getResult().getInsertedId()));
        }
      }
    });

        Document filterDoc = new Document();

        RemoteFindIterable results = coll.find(filterDoc);
        return "Test";
    }

}
