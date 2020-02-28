package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mongodb.Block;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;

public class ViewJournalActivity extends AppCompatActivity {

  Button addBtn;
  TextView title;
  TextView journalEntries;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_journal);
    addBtn = findViewById(R.id.addJournal);
    title = findViewById(R.id.title);
    journalEntries = findViewById(R.id.list);

    // Add user's name to title
    title.append("John Smith");

    getAllEntries();

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

  public void getAllEntries(){

    final StitchAppClient client =
      Stitch.getAppClient("incrementum-xjkms");

    final RemoteMongoClient mongoClient =
      client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

    final RemoteMongoCollection<Document> coll =
      mongoClient.getDatabase("Incrementum").getCollection("Journals");

    Document filterDoc = new Document();

    RemoteFindIterable results = coll.find(filterDoc);

    results.forEach(new Block() {
      @Override
      public void apply(Object item) {
        String[] ids = item.toString().split("=");
        String[] id = ids[1].split(",");
        journalEntries.append(String.format("\nID is: %s", id[0]));
      }
    });
  }
}
