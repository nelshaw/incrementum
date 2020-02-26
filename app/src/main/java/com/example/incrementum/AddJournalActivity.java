package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddJournalActivity extends AppCompatActivity {

  Button saveBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_journal);
    saveBtn = findViewById(R.id.save);
    saveBtn.setOnClickListener(new View.OnClickListener(){

        @Override
        public void onClick(View v) {
          openViewJournalActivity();
        }
      });
  }

  public void openViewJournalActivity(){
    Intent intent = new Intent(this, ViewJournalActivity.class);
    startActivity(intent);
  }
}
