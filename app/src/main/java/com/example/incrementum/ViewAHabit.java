package com.example.incrementum;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.providers.userpassword.UserPasswordAuthProviderClient;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mongodb.client.model.Filters;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class ViewAHabit extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewHabitActivity.class);
        startActivity(intent);
    }

    TextView name;
    TextView triggers;
    TextView times;
    String habitName;
    String id;
    TextView descriptionText;
    String description;
    int length;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_a_habit);
        UserInfo user = (UserInfo) getApplication();
        id = user.getHabitId();


        Button delete = findViewById(R.id.delete);
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ViewHabitActivity.class);
                startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(ViewAHabit.this);
                dialog.setMessage("Most people require sufficient time to realize whether or not a habit is harmful or not. It is recommended you go over how you initially felt, and think about deleting the habit. \nAre you sure you want to delete this habit?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(length==0)
                                {

                                    try {
                                        delete(id);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                    Toast.makeText(getBaseContext(),"Cannot delete habit because minimum tracking length has not been met.", Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                dialog.show();


            }
        });

        name = findViewById(R.id.name);
        triggers = findViewById(R.id.triggersListView);
        times = findViewById(R.id.timesListView);
        descriptionText = findViewById(R.id.description);
        DatabaseLoad load = new DatabaseLoad();
        load.execute();
    }
    private class DatabaseLoad extends AsyncTask<Void,Void,Void> {
        RemoteFindIterable <Document>  results;
        @Override
        protected void onPreExecute() {
            final StitchAppClient client =
                    Stitch.getAppClient("incrementum-xjkms");
            final RemoteMongoClient mongoClient =
                    client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
            final RemoteMongoCollection<Document> coll =
                    mongoClient.getDatabase("Incrementum").getCollection("Habits");
            results = coll.find(Filters.eq("_id",new ObjectId(id)))
                    .projection(
                            new Document());
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            results.forEach(item ->{
                try{
                    JSONObject obj = new JSONObject(item.toJson());
                    Log.d("HABIT", obj.toString());
                    habitName = obj.getString("name");
                    JSONArray triggerlist = new JSONArray();
                    triggerlist = obj.getJSONArray("Triggers");
                    JSONArray timesList = new JSONArray();
                    timesList = obj.getJSONArray("Times");
                    name.setText(habitName);
                    description = obj.getString("description");
                    descriptionText.setText(description);
                    length = Integer.parseInt(obj.getString("length"));
                    ArrayList<String> tempTriggers = new ArrayList<String>();
                    for(int i =0;i<triggerlist.length();i++)
                    {
                        tempTriggers.add(triggerlist.get(i).toString());
                        Log.d("*********************",triggerlist.get(i).toString());
                    }
                    for(String s:tempTriggers)
                    {
                        triggers.append(s+"\n");
                    }
                    ArrayList<String> tempTimes= new ArrayList<String>();
                    for(int i =0;i<timesList.length();i++)
                    {
                        tempTimes.add(timesList.get(i).toString());
                        Log.d("*********************",timesList.get(i).toString());
                    }
                    for(String s:tempTimes)
                    {
                        times.append(s+"\n");
                    }
                }
                catch(JSONException e){
                    Log.d("JSON exception:",e.toString());
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            runOnUiThread(() -> {   Log.d("POST","************************************");

                name.setText(habitName);
                descriptionText.setText(description);
            });
            super.onPostExecute(aVoid);
        }
    }

    public void delete(String id) throws InterruptedException {


        final RemoteMongoCollection<Document> coll =
                DatabaseHelper.mongoClient.getDatabase("Incrementum").getCollection("Habits");

        final RemoteMongoCollection<Document> collCalendar =
                DatabaseHelper.mongoClient.getDatabase("Incrementum").getCollection("Calendar");


        String str = new ObjectId(id).toString();
        coll.deleteOne(new Document("_id",new ObjectId(id)));
        collCalendar.deleteOne(Filters.eq("Habit_id", str));
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
        Thread.sleep(100);
        Intent intent = new Intent(this, ViewHabitActivity.class);
        startActivity(intent);

    }







}
