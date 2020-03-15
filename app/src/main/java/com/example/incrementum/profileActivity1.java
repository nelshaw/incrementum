package com.example.incrementum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;




import java.util.ArrayList;


import butterknife.InjectView;

public class profileActivity1 extends AppCompatActivity {

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
        Toast.makeText(getBaseContext(),email, Toast.LENGTH_LONG).show();
        _emailText.setText(email);
        getData();
        if(_userText.length()==0)
        {
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
                switch (menuItem.getItemId()){

                    case R.id.profile_nav:
//                        finish();
//                        startActivity(new Intent(getApplicationContext()
//                                ,CalendarActivity.class));
//                        overridePendingTransition(0,0);
                        return true;

                    case R.id.habits_nav:
                        finish();
                        startActivity(new Intent(getApplicationContext()
                                ,ViewHabitActivity.class));
                        overridePendingTransition(0,0);
                        return true;
//
//                    case R.id.profile_nav:
////            startActivity(new Intent(getApplicationContext()
////                    ,MainActivity.class));
////            overridePendingTransition(0,0);
//                        finish();
//                        return true;
                }
                return false;
            }
        });





















    }


public void getData(){
    RemoteFindIterable<Document> results;

        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");
        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Users");
        results = coll.find(Filters.eq("email",email))
                .projection(
                        new Document());
    results.forEach(item ->{
        try{
            JSONObject obj = new JSONObject(item.toJson());
            Log.d("RESULTS",obj.toString());
            userName = obj.getString("username");
            Log.d("USERNAMEEEEEEEEEEEEEE",userName);
            _userText.setText(userName);
        }
        catch(JSONException e){
            Log.d("JSON exception:",e.toString());
        }
    });
}
}
