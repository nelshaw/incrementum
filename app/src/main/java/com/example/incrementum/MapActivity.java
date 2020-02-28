package com.example.incrementum;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

  Button habit, journal, logout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_journal);

    habit = findViewById(R.id.habit);
    journal = findViewById(R.id.journal);
    logout = findViewById(R.id.logout);
//    addBtn = findViewById(R.id.addJournal);
//    title = findViewById(R.id.title);
//    journalEntries = findViewById(R.id.list);

//    addBtn.setOnClickListener(new View.OnClickListener() {
//      @Override
//        public void onClick(View v) {
//          openAddJournalActivity();
//        }
//      });
  }
}
