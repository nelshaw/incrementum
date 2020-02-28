package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewJournalActivity extends AppCompatActivity {

  Button addBtn;
  TextView journalEntries;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_journal);
    addBtn = findViewById(R.id.addJournal);
    journalEntries = findViewById(R.id.Title);

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

//    MongoClientURI uri = new MongoClientURI(
//      "mongodb+srv://groupnotfound:abcde12345@incrementum-zhwf9.mongodb.net/test?retryWrites=true&w=majority");
//
//    MongoClient mongoClient = new MongoClient(uri);
//    MongoDatabase database = mongoClient.getDatabase("incrementum");
//
//    Document journals = database.getCollection("Journals")
//      .find().first();

//    return journals.toJson();
    return "test";
  }
}
