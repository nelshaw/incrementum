package com.example.incrementum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    EditText startTimeText;
    EditText endTimeText;

    //from signup
    String email;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__habits__hobbies__time_);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        insertUser(username, email, password);




        EnterButton = findViewById(R.id.addHabitsHobbiesTimeButton);
        HobbyText = findViewById(R.id.editText5);
        startTimeText = findViewById(R.id.start);
        endTimeText = findViewById(R.id.end);
        EnterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openHabitActivity();
            }
        });


    }

    public void openHabitActivity(){
        Intent intent = new Intent(this, AddHabitActivity.class);
        getAllEntries();
        startActivity(intent);
    }

    public void insertUser(String user_name, String e_mail, String pass_word) {

        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Users");

        Toast.makeText(getBaseContext(), "Point reached", Toast.LENGTH_LONG).show();

        Document doc = new Document()
                .append("username", user_name)
                .append("email", e_mail)
                .append("password", pass_word);

        final Task<RemoteInsertOneResult> insert = coll.insertOne(doc);

        insert.addOnCompleteListener(new OnCompleteListener<RemoteInsertOneResult>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<RemoteInsertOneResult> task) {
                if (task.isSuccessful()) {
                    Log.i("STITCH", String.format("success inserting: %s",
                            task.getResult().getInsertedId()));
                }
            }
        });
    }

    public void getAllEntries(){

        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Users");

        Document doc = new Document()
                .append("sensitive_time_start", startTimeText.getText().toString())
                .append("sensitive_time_end", endTimeText.getText().toString())
                .append("hobbies", HobbyText.getText().toString());

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
    }

}
