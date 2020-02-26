package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ViewJournalActivity extends AppCompatActivity {

  Button addBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_journal);
    addBtn = findViewById(R.id.addJournal);

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
}
