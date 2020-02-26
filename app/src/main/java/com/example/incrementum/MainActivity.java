package com.example.incrementum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //button to test habit effect activity, not there in final project
    Button button;
    Button goto_login_button;
    Button journal;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        Stitch.initializeDefaultAppClient("incrementum-xjkms");
      StitchAppClient client = Stitch.getDefaultAppClient();

      client.getAuth().loginWithCredential(new AnonymousCredential())
        .continueWithTask(new Continuation<StitchUser, Task<Void>>() {
          @Override
          public Task<Void> then(@NonNull Task<StitchUser> task) throws Exception {
            return null;
          }
        });

      final RemoteMongoClient mongoClient = client
        .getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

      Document doc = new Document().append("time", new Date());

      RemoteMongoCollection<Document> collection = mongoClient.getDatabase("Incrementum")
        .getCollection("Users");

      Log.d("TEST" , "TEST");

      collection.insertOne(doc)
        .addOnSuccessListener(new OnSuccessListener<RemoteInsertOneResult>() {
                                @Override
                                public void onSuccess(RemoteInsertOneResult remoteInsertOneResult) {
                                  Log.d("STITCH", "One doc inserted");
                                }

                              });






      // find button by id
        button = findViewById(R.id.button);
        goto_login_button = findViewById(R.id.goto_login_button);
        journal = findViewById(R.id.journal);

        // on click function
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openHabitEffectActivity();
            }
        });

        // on click function
        goto_login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openLoginActivity();
            }
        });

        journal.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            openJournalActivity();
          }
        });
    }

    // open habit effect
    public void openHabitEffectActivity(){
        Intent intent = new Intent(this, HabitEffectActivity.class);
        startActivity(intent);
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

  public void openJournalActivity(){
    Intent intent = new Intent(this, ViewJournalActivity.class);
    startActivity(intent);
  }

}
