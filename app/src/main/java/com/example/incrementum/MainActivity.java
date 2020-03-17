package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;

public class MainActivity extends AppCompatActivity {

  //button to test habit effect activity, not there in final project
  Button button;
  Button goto_login_button;
  Button journal;
  Button calendarButton;
  TextView title;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Initalize and Assign Value
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

    //Set home selected
    bottomNavigationView.setSelectedItemId(R.id.map_nav);

    //Perform ItemSelectedList
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
          case R.id.calendar_nav:
            finish();
            startActivity(new Intent(getApplicationContext()
                    ,CalendarActivity.class));
            overridePendingTransition(0,0);
            return true;

          case R.id.journal_nav:
            finish();
            startActivity(new Intent(getApplicationContext()
                    ,ViewJournalActivity.class));
            overridePendingTransition(0,0);
            return true;

          case R.id.habit_nav:
            startActivity(new Intent(getApplicationContext()
              ,ViewHabitActivity.class));
            overridePendingTransition(0,0);
            finish();
            return true;

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

    title = findViewById(R.id.title);

    Button log = findViewById(R.id.logs);
    log.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        openlogs();
      }
    });

    Button habit = findViewById(R.id.habit);
    habit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        openHabitActivity();
      }
    });

    final StitchAppClient client =
            Stitch.initializeDefaultAppClient("incrementum-xjkms");

    final RemoteMongoClient mongoClient =
            client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

    // find button by id
    button = findViewById(R.id.button);
    goto_login_button = findViewById(R.id.goto_login_button);
    journal = findViewById(R.id.journal);
    calendarButton = findViewById(R.id.calendarButton);
    Button addHabitBtn = findViewById(R.id.button2);

    addHabitBtn.setOnClickListener( v -> {
      Intent intent = new Intent(this, AddHabitActivity.class);
      startActivity(intent);
    });


    // on click function
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        openHabitEffectActivity();
      }
    });
    // on click function
    goto_login_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        openLoginActivity();
      }
    });

    journal.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        openJournalActivity();
      }
    });

    calendarButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v){
        openCalendarActivity();
      }
    });
  }

  // open habit effect
  public void openHabitEffectActivity() {
    Intent intent = new Intent(this, HabitEffectActivity.class);
    startActivity(intent);
  }

  public void openLoginActivity() {
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
  }

  public void openJournalActivity() {
    Intent intent = new Intent(this, ViewJournalActivity.class);
    startActivity(intent);
  }

  public void openHabitActivity(){
    Intent intent = new Intent(this, ViewHabitActivity.class);
    startActivity(intent);
  }

  public void openlogs()
  {
    Intent intent = new Intent(this, Log_Habits_Hobbies_Time_Activity.class);
    startActivity(intent);
  }

  public void openCalendarActivity() {
    Intent intent = new Intent(this, CalendarActivity.class);
    startActivity(intent);
  }

}
