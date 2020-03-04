package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Date;

public class ViewJournalActivity extends AppCompatActivity {

  Button addBtn;
  Button backBtn;
  TextView title;
  TextView journalEntries;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_journal);
    addBtn = findViewById(R.id.addJournal);
    title = findViewById(R.id.title);
    journalEntries = findViewById(R.id.list);

    backBtn = findViewById(R.id.back_button);

    // Add user's name to title
    title.append(" John Smith");

    journalEntries.setText("");

    getAllEntries();

    addBtn.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        openAddJournalActivity();
      }
    });

    backBtn.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        openMapActivity();
      }
    });


  }


  public void openAddJournalActivity(){
    Intent intent = new Intent(this, AddJournalActivity.class);
    startActivity(intent);
  }

  public void openMapActivity(){
    Intent intent = new Intent(this, MapActivity.class);
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
//      .append("entry", new Document().append("$eq", true));

    RemoteFindIterable results = coll.find(filterDoc);

    Log.d("Journal", "*************************************test test test");
    Log.d("journal", String.valueOf(results));

    results.forEach(new Block() {
      int i = 1;
      @Override
      public void apply(Object item) {
        Log.d("Journal", item.toString());
        Document doc = (Document) item;

        Date date = (Date) doc.get("date");
        String[] dateStr = date.toString().split(" ");

        String entry = (String) doc.get("entry");
        String userId = doc.get("user_id").toString();

        journalEntries.append("Entry " + i++ +": " + entry);
        journalEntries.append(String.format("\nPosted by %s\n", userId));
        journalEntries.append(String.format("Added on %s\n\n", dateStr[1] + " " + dateStr[2]));

      }
    });
  }
}
