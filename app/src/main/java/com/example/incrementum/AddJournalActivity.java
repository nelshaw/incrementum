package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;

import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {

  Button saveBtn;
  TextView title;
  EditText entry;
  EditText trigger;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_journal);
    saveBtn = findViewById(R.id.addJournal);
    title = findViewById(R.id.title);
    entry = findViewById(R.id.journalEntry);
    trigger = findViewById(R.id.newTrigger);

    saveBtn.setOnClickListener(new View.OnClickListener(){

        @Override
        public void onClick(View v) {
          openViewJournalActivity();
        }
      });
  }

  public void openViewJournalActivity(){
    addEntry();
    Intent intent = new Intent(this, ViewJournalActivity.class);

    startActivity(intent);
  }

  public void addEntry(){

    final StitchAppClient client =
      Stitch.getAppClient("incrementum-xjkms");

    final RemoteMongoClient mongoClient =
      client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

    final RemoteMongoCollection<Document> coll =
      mongoClient.getDatabase("Incrementum").getCollection("Journals");


    Document doc = new Document()
      .append("user_id", 12345)
      .append("date", new Date())
      .append("entry", entry.getText().toString())
      .append("trigger", false);

    final Task<RemoteInsertOneResult> insert = coll.insertOne(doc);

    insert.addOnCompleteListener(new OnCompleteListener<RemoteInsertOneResult>() {
      @Override
      public void onComplete(@NonNull Task<RemoteInsertOneResult> task) {
        if (task.isSuccessful()){
          Log.d("STITCH", String.format("success inserting: %s",
            task.getResult().getInsertedId()));
        }
        else{
          Log.d("STITCH", "Unsuccessful");
        }
      }
    });
  }
}
