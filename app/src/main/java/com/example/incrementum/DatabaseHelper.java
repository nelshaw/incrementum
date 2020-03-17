package com.example.incrementum;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.Task;
import com.mongodb.Block;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper {

  // Connect to MongoDB client
  final static StitchAppClient client =
    Stitch.getAppClient("incrementum-xjkms");

  final static RemoteMongoClient mongoClient =
    client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

  public static void getAllJournals(List<String> journals, HashMap<Integer, Date> entriesInformation) {

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

                        entriesInformation.put(i - 1, date);
                        String journalEntry = "Entry " + i++ + ": " + entry +
                          String.format("\nPosted by %s\n", userId) +
                          String.format("Added on %s\n\n", dateStr[1] + " " + dateStr[2]);

                        // Append journal to journal list
                        journals.add(journalEntry);

                        Log.d("JOURNAL", entry);
                      }
                    }
    );
  }

  public static void updateJournal(HashMap<Integer, Date> entriesInformation, int position, String journalEntry) {

    final RemoteMongoCollection<Document> coll =
      mongoClient.getDatabase("Incrementum").getCollection("Journals");

    Document filterDoc = new Document().append("date", entriesInformation.get(position));
    Document updateDoc = new Document()
      .append("$set", new Document()
        .append("entry", journalEntry)
      );

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
  };

  public static String getFirstName(String user_id){

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

}
