package com.example.incrementum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mongodb.client.model.Filters;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.incrementum.DatabaseHelper.mongoClient;

public class ProfileActivity extends AppCompatActivity {

  TextView _emailText;
  TextView _userText;
  String userName;
  String email;
  Button refresh;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    _emailText = findViewById(R.id.emailText);
    _emailText.setText(email);

    final Button logout = findViewById(R.id.logout_b);

    final Button analButton = findViewById(R.id.analbutton);

    analButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        userName = _userText.getText().toString();
        openAnalysis();
        sendData(userName);
        finish();
      }
    });

    logout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        UserInfo user2 = (UserInfo) getApplication();
        user2.clear();
        startActivity(new Intent(getApplicationContext()
                , LoginActivity.class));
      }
    });

    _emailText = findViewById(R.id.emailText);
    _userText = findViewById(R.id.usernameText);
    _userText.setText("");

    UserInfo user = (UserInfo) getApplication();
    email = user.getEmail();

    _emailText.setText(email);
    getData();
    if (_userText.length() == 0) {
      getData();
    }

    //Initalize and Assign Value
    BottomNavigationView bottomNavigationView2 = findViewById(R.id.bottom_navigation2);

    //Set home selected
    bottomNavigationView2.setSelectedItemId(R.id.profile_nav);

    //Perform ItemSelectedList
    bottomNavigationView2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

          case R.id.profile_nav:
//                        finish();
//                        startActivity(new Intent(getApplicationContext()
//                                ,CalendarActivity.class));
//                        overridePendingTransition(0,0);
            return true;

          case R.id.habit_nav:
            finish();
            startActivity(new Intent(getApplicationContext()
              , ViewHabitActivity.class));
            overridePendingTransition(0, 0);
            return true;

        }
        return false;
      }
    });

  }

  public void openAnalysis() {
    Intent intent = new Intent(this, only_hobbies.class);
    startActivity(intent);
  }

  public void sendData(String username)
  {
    UserInfo user = (UserInfo) getApplication();
    user.setUserName(username);
  }

  public void getData() {
    RemoteFindIterable<Document> results;
    // Connect to MongoDB Client
    final RemoteMongoCollection<Document> coll =
      mongoClient.getDatabase("Incrementum").getCollection("Users");
    results = coll.find(Filters.eq("email", email))
      .projection(
        new Document());
    results.forEach(item -> {
      try {
        JSONObject obj = new JSONObject(item.toJson());
        Log.d("RESULTS", obj.toString());
        userName = obj.getString("username");
        Log.d("USERNAMEEEEEEEEEEEEEE", userName);
        _userText.setText(userName);
        sendData(userName);
      } catch (JSONException e) {
        Log.d("JSON exception:", e.toString());
      }
    });
  }
}
