package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

  Button habit, journal, logout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);

    habit = findViewById(R.id.habit);
    journal = findViewById(R.id.journal);
    logout = findViewById(R.id.logout);

    habit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        openViewHabitActivity();
      }
    });

    journal.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        openViewJournalActivity();
      }
    });

    logout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        logout();
      }
    });

  }

  public void openViewHabitActivity() {
    Intent intent = new Intent(this, ViewHabitActivity.class);
    startActivity(intent);
  }

  public void openViewJournalActivity() {
    Intent intent = new Intent(this, ViewJournalActivity.class);
    startActivity(intent);
  }

  public void logout() {
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
  }
}
