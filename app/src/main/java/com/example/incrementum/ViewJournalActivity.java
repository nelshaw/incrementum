package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mongodb.Block;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewJournalActivity extends AppCompatActivity {

  Button addBtn;
  Button backBtn;

  TextView title;
  String user_id;

  List<String> journals;
  ListView listView;
  ListAdapter listAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_journal);

    journals = new ArrayList<>();

    addBtn = findViewById(R.id.addJournal);
    title = findViewById(R.id.title);
    backBtn = findViewById(R.id.back_button);
    listView = findViewById(R.id.myJournals);

    getAllEntries();

    //wait until journals list is filled
    try {
      Thread.sleep(1500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Get user_id from login to view journal entries for that user
    user_id = LoginActivity.user_id;

    // Add user's name to title
    // This will grab from database once dummy data has been inserted
    title.append(" " + getFirstName());

    // Direct to add journal entry
    addBtn.setOnClickListener(v -> openAddJournalActivity());

    // Go back to home page
    backBtn.setOnClickListener(v -> openMapActivity());

  }

  public void openAddJournalActivity(){
    Intent intent = new Intent(this, AddJournalActivity.class);
    startActivity(intent);
  }

  public void openMapActivity(){
    Intent intent = new Intent(this, MapActivity.class);
    startActivity(intent);
  }

  public String getFirstName(){

    // Connect to MongoDB client
    final StitchAppClient client =
      Stitch.getAppClient("incrementum-xjkms");

    final RemoteMongoClient mongoClient =
      client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

    final RemoteMongoCollection<Document> coll =
      mongoClient.getDatabase("Incrementum").getCollection("Users");

    // Only get journal entries from current user who is logged in
    Document filterDoc = new Document()
      .append("userid", user_id);

    // Get all entries with the criteria from filterDoc
    RemoteFindIterable results = coll.find(filterDoc);

    // Log all journal entries that are found in the logger
    Log.d("USER", String.valueOf(results));

    final String[] firstName = new String[1];

    results.forEach(item -> {
      Document doc = (Document) item;
      String fName = (String) doc.get("fname");

      if(fName != null){
        firstName[0] = fName;
      }

      Log.d("USER", fName);
    }
    );

    if(firstName[0] != null){
      return firstName[0];
    }

    return "John Smith";
  }

  public void getAllEntries(){

    // Connect to MongoDB client
    final StitchAppClient client =
      Stitch.getAppClient("incrementum-xjkms");

    final RemoteMongoClient mongoClient =
      client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

    final RemoteMongoCollection<Document> coll =
      mongoClient.getDatabase("Incrementum").getCollection("Journals");

    // Only get journal entries from current user who is logged in
    Document filterDoc = new Document();
//      .append("user_id", user_id);

    // Get all entries with the criteria from filterDoc
    RemoteFindIterable results = coll.find(filterDoc);

    // Log all journal entries that are found in the logger
    Log.d("JOURNALS", String.valueOf(results));

    results.forEach(new Block() {
                      int i = 1;

                      @Override
                      public void apply(Object item) {
                        Document doc = (Document) item;
                        String entry = (String) doc.get("entry");

                        Date date = (Date) doc.get("date");
                        String[] dateStr = date.toString().split(" ");

                        String userId = doc.get("user_id").toString();

                        String journalEntry = "Entry " + i++ + ": " + entry +
                          String.format("\nPosted by %s\n", userId) +
                          String.format("Added on %s\n\n", dateStr[1] + " " + dateStr[2]);
                        //append habit to habitNames list
                        journals.add(journalEntry);
                        Log.d("JOURNAL", entry);
                      }
                    }
    );

    //create list adapter for ListView
    listAdapter = new ArrayAdapter<>(this, R.layout.simplerow, journals);

    //attach adapter to ListView
    listView.setAdapter(listAdapter);
  }

}
