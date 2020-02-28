package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.Block;
import com.mongodb.internal.connection.Time;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ViewJournalActivity extends AppCompatActivity {

  Button addBtn;
  TextView title;
  TextView journalEntries;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_journal);
    addBtn = findViewById(R.id.addJournal);
    title = findViewById(R.id.Title);
    journalEntries = findViewById(R.id.list);

    System.out.println(getAllEntries());

    addBtn.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        openAddJournalActivity();
      }
    });
  }

  public void openAddJournalActivity(){
    Intent intent = new Intent(this, AddJournalActivity.class);
    startActivity(intent);
  }

  public String getAllEntries(){

    final StitchAppClient client =
      Stitch.getAppClient("incrementum-xjkms");

    final RemoteMongoClient mongoClient =
      client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

    final RemoteMongoCollection<Document> coll =
      mongoClient.getDatabase("Incrementum").getCollection("Journals");

    Document doc = new Document().append("trigger", true);

//    final Task<RemoteInsertOneResult> insert = coll.insertOne(doc);
//    insert.addOnCompleteListener(new OnCompleteListener<RemoteInsertOneResult>() {
//      @Override
//      public void onComplete(@NonNull Task<RemoteInsertOneResult> task) {
//        if (task.isSuccessful()){
//          Log.d("STITCH", String.format("success inserting: %s",
//            task.getResult().getInsertedId()));
//        }
//      }
//    });

    Document filterDoc = new Document();

    RemoteFindIterable results = coll.find(filterDoc);

    results.forEach(new Block() {
      @Override
      public void apply(Object item) {
        journalEntries.append(String.format("Journal is: %s", item.toString()));
      }
    });

    return "test";
  }
}
