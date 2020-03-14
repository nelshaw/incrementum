package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

  Button habit, journal, logout;
  ImageView map;
  int mapClickCounter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);

    habit = findViewById(R.id.habit);
    journal = findViewById(R.id.journal);
    logout = findViewById(R.id.logout);
    map = findViewById(R.id.map);
    mapClickCounter = 0;
    map.setImageDrawable(getResources().getDrawable(R.drawable.map0));


    map.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mapClickCounter++;
        if(mapClickCounter ==  1)
          map.setImageDrawable(getResources().getDrawable(R.drawable.map1));
        else if(mapClickCounter ==  2)
          map.setImageDrawable(getResources().getDrawable(R.drawable.map2));
        else if(mapClickCounter ==  3)
          map.setImageDrawable(getResources().getDrawable(R.drawable.map3));
        else if(mapClickCounter ==  4)
          map.setImageDrawable(getResources().getDrawable(R.drawable.map4));
        else if(mapClickCounter ==  5)
          map.setImageDrawable(getResources().getDrawable(R.drawable.map5));
        else if(mapClickCounter ==  6)
          map.setImageDrawable(getResources().getDrawable(R.drawable.map6));
        else if(mapClickCounter ==  7)
          map.setImageDrawable(getResources().getDrawable(R.drawable.map7));
        else if(mapClickCounter ==  8)
          map.setImageDrawable(getResources().getDrawable(R.drawable.map8));
        else if(mapClickCounter ==  9)
          map.setImageDrawable(getResources().getDrawable(R.drawable.map9));
        else if(mapClickCounter ==  10)
          map.setImageDrawable(getResources().getDrawable(R.drawable.map10));
        else if(mapClickCounter ==  11)
          map.setImageDrawable(getResources().getDrawable(R.drawable.map11));
        else if(mapClickCounter ==  12)
          map.setImageDrawable(getResources().getDrawable(R.drawable.map12));
        else if(mapClickCounter ==  13)
          map.setImageDrawable(getResources().getDrawable(R.drawable.map13));
        else if(mapClickCounter ==  14)
          map.setImageDrawable(getResources().getDrawable(R.drawable.map14));
        else
          map.setImageDrawable(getResources().getDrawable(R.drawable.logo));
      }
    });

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
