package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.mongodb.Block;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ViewJournalActivity extends AppCompatActivity {

  @InjectView(R.id.addJournal) Button addBtn;
  @InjectView(R.id.myJournals) ListView listView;
  @InjectView(R.id.title) TextView title;
  @InjectView(R.id.updateJournal) EditText updateJournal;
  @InjectView(R.id.updateBtn) Button updateBtn;

  String user_id;
  List<String> journals;
  ListAdapter listAdapter;
  HashMap<Integer, Date> entriesInformation;
  boolean isUpdateVisible;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_journal);
    ButterKnife.inject(this);

    journals = new ArrayList<>();
    entriesInformation = new HashMap<>();

    updateJournal.setVisibility(View.INVISIBLE);
    updateBtn.setVisibility(View.INVISIBLE);
    isUpdateVisible = false;

    // Get user_id from login to view journal entries for that user
    user_id = LoginActivity.user_id;

    getAllEntries();

    //wait until journals list is filled
    try {
      Thread.sleep(1500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Add user's name to title
    // This will grab from database once dummy data has been inserted
    title.append(" " + getFirstName());

    listView.setOnItemClickListener((parent, view, position, id) -> {
      if(!isUpdateVisible) {
        updateJournal.setVisibility(View.VISIBLE);
        updateBtn.setVisibility(View.VISIBLE);
        isUpdateVisible = true;
      }
      else {
        updateJournal.setVisibility(View.INVISIBLE);
        updateBtn.setVisibility(View.INVISIBLE);
        isUpdateVisible = false;
      }
      updateEntry(position);
      Log.d("VIEW", "Position: " + position + " id: " + id);
    });

    //Initalize and Assign Value
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

    //Set home selected
    bottomNavigationView.setSelectedItemId(R.id.journal_nav);

    //Perform ItemSelectedList
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
          case R.id.calender_nav:
            finish();
            startActivity(new Intent(getApplicationContext()
                    ,CalendarActivity.class));
            overridePendingTransition(0,0);
            return true;

//          case R.id.journal_nav:
//            finish();
//            startActivity(new Intent(getApplicationContext()
//                    ,ViewJournalActivity.class));
//            overridePendingTransition(0,0);
//            return true;

          case R.id.map_nav:
            startActivity(new Intent(getApplicationContext()
                    ,MapActivity.class));
            overridePendingTransition(0,0);
            finish();
            return true;
        }
        return false;
      }
    });

      // Direct to add journal entry
      addBtn.setOnClickListener(v -> openAddJournalActivity());
  }

  public void openAddJournalActivity(){
    Intent intent = new Intent(this, AddJournalActivity.class);
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

    final RemoteMongoCollection<Document> collection =
      mongoClient.getDatabase("Incrementum").getCollection("Journals");

    // Only get journal entries from current user who is logged in
    Document filterDoc = new Document();
//      .append("user_id", user_id);

    // Get all entries with the criteria from filterDoc
    RemoteFindIterable results = collection.find(filterDoc);

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

                        entriesInformation.put(i-1, date);
                        String journalEntry = "Entry " + i++ + ": " + entry +
                          String.format("\nPosted by %s\n", userId) +
                          String.format("Added on %s\n\n", dateStr[1] + " " + dateStr[2]);

                        // Append journal to journal list
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

  public void updateEntry(int position){

    updateBtn.setOnClickListener(v -> {
      // Connect to MongoDB client
      final StitchAppClient client =
        Stitch.getAppClient("incrementum-xjkms");

      final RemoteMongoClient mongoClient =
        client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
      final RemoteMongoCollection<Document> coll =
        mongoClient.getDatabase("Incrementum").getCollection("Journals");

      Document filterDoc = new Document().append("date", entriesInformation.get(position));
      Document updateDoc = new Document()
        .append("$push", new Document().append("entry", updateJournal.getText().toString()));

      final Task<RemoteUpdateResult> updateTask = coll.updateOne(filterDoc, updateDoc);
      updateTask.addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          long numMatched = task.getResult().getMatchedCount();
          long numModified = task.getResult().getModifiedCount();
          Log.d("app", String.format("successfully matched %d and modified %d documents",
            numMatched, numModified));
        } else {
          Log.e("app", "failed to update document with: ", task.getException());
        }
      });
    });

  }

}
