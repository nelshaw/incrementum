package com.example.incrementum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class ProfileActivity extends AppCompatActivity {

  TextView _emailText;
  TextView _userText;
  String userName;
  String email;
  Button refresh;
  ImageView profilePicture;
  Button addNewProfilePicture;

  private static final int PERMISSION_REQUEST = 0;
  private static final int RESULT_LOAD_IMAGE = 1;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    String[] permissionsNeeded = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    != PackageManager.PERMISSION_GRANTED){
     // requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
      requestPermissions(permissionsNeeded, PERMISSION_REQUEST);
    }

    _emailText = findViewById(R.id.emailText);
    _emailText.setText(email);
    profilePicture = (ImageView)  findViewById(R.id.profilePic);
    addNewProfilePicture = (Button)  findViewById(R.id.addProfilePicture);

//    addNewProfilePicture.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(i, RESULT_LOAD_IMAGE);
//      }
//    });



    refresh = findViewById(R.id.refresh);
    refresh.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
      }
    });

    _emailText = findViewById(R.id.emailText);
    _userText = findViewById(R.id.usernameText);
    _userText.setText("");
    Intent intent = getIntent();
    email = intent.getStringExtra("email");
    Toast.makeText(getBaseContext(), email, Toast.LENGTH_LONG).show();
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


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode){
      case PERMISSION_REQUEST:
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
          Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
        }
        else{
          Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();

        }
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    switch (requestCode){
      case RESULT_LOAD_IMAGE:
        if (resultCode == RESULT_OK){
          Uri selectedImage = data.getData();
          String[] filePathColumn = {MediaStore.Images.Media.DATA};
          Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null);
          cursor.moveToFirst();
          int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
          String picturePath = cursor.getString(columnIndex);
          cursor.close();
          profilePicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
  }

  public void getData() {
    RemoteFindIterable<Document> results;

    final StitchAppClient client =
      Stitch.getAppClient("incrementum-xjkms");
    final RemoteMongoClient mongoClient =
      client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
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
      } catch (JSONException e) {
        Log.d("JSON exception:", e.toString());
      }
    });
  }
}
