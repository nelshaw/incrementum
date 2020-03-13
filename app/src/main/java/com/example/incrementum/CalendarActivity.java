package com.example.incrementum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.client.model.Updates;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import static com.mongodb.client.model.Filters.eq;

public class CalendarActivity extends AppCompatActivity {

    MaterialCalendarView calendarView;
    HashSet<CalendarDay> didNotDoHabitDates;
    HashSet<CalendarDay> didDoHabitDates;

    ImageButton didNotButton;
    ImageButton didButton;

    String User_id = "5e587cbed6292c4d1074b5d8";
    String Habit_id = "12345";

    static Date dateSelected;

    //connect to database
    final StitchAppClient client =
            Stitch.getAppClient("incrementum-xjkms");

    final RemoteMongoClient mongoClient =
            client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

    //connect to collection
    final RemoteMongoCollection<Document> coll =
            mongoClient.getDatabase("Incrementum").getCollection("Calendar");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        didNotButton = findViewById(R.id.didNotButton);
        didButton = findViewById(R.id.didButton);

        didNotDoHabitDates = new HashSet<>();
        didDoHabitDates = new HashSet<>();

        getDateValues();

        //sleep 1 sec to wait for getDateValues() to fill all hashsets from mongodb
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        calendarView.addDecorator(new CalendarDecorator(this, Color.GREEN, didNotDoHabitDates));
        calendarView.addDecorator(new CalendarDecorator(this, Color.RED, didDoHabitDates));
        calendarView.setSelectedDate(CalendarDay.today());

        didNotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarDay date = calendarView.getSelectedDate();
                String selectedDate = formatDate(date);

                Document query = new Document()
                        .append("User_id", User_id)
                        .append("Habit_id", Habit_id);

                Document dayStatus = new Document()
                        .append("Date", selectedDate)
                        .append("Status", false);

                final Task<RemoteUpdateResult> update = coll.updateOne(query, Updates.addToSet("Days", dayStatus));

                update.addOnCompleteListener(new OnCompleteListener<RemoteUpdateResult>() {
                    @Override
                    public void onComplete(@NonNull Task<RemoteUpdateResult> task) {
                        if (task.isSuccessful()){
                            Log.d("STITCH", String.format("success inserting: %s",
                                    task.getResult().getUpsertedId()));
                        }
                        else{
                            Log.d("STITCH", "Unsuccessful");
                        }
                    }
                });
                finish();
                startActivity(getIntent());
            }
        });

        didButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarDay date = calendarView.getSelectedDate();
                String selectedDate = formatDate(date);

                Document dayStatus = new Document()
                        .append("Date", selectedDate)
                        .append("Status", true);

                final Task<RemoteUpdateResult> update = coll.updateOne(eq("User_id", User_id), Updates.addToSet("Days", dayStatus));

                update.addOnCompleteListener(new OnCompleteListener<RemoteUpdateResult>() {
                    @Override
                    public void onComplete(@NonNull Task<RemoteUpdateResult> task) {
                        if (task.isSuccessful()){
                            Log.d("STITCH", String.format("success inserting: %s",
                                    task.getResult().getUpsertedId()));
                        }
                        else{
                            Log.d("STITCH", "Unsuccessful");
                        }
                    }
                });

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

              try {
                dateSelected = format.parse(selectedDate);
              } catch (ParseException e) {
                e.printStackTrace();
              }

              openAddHabitOccurrenceActivity();
              Log.d("CALENDAR", "date added " + dateSelected);
            }
        });

    }

    //function to format date
    public String formatDate(CalendarDay date){
        String day = date.getDay() + "";
        String month = (date.getMonth() + 1) + "";
        if(day.length() != 2)
            day = "0" + day;
        if(month.length() != 2)
            month = "0" + month;

        return date.getYear() + "-" + month + "-" + day;
    }

    //function to convert string to date
    public Date StringToDate(String s){

        Date result = null;
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            result  = dateFormat.parse(s);
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        return result ;
    }

    public void getDateValues(){

        Document filterDoc = new Document();

        //find all documents
        RemoteFindIterable<Document> results = coll.find(filterDoc)
                .projection(
                        new Document()
                                .append("_id", 0)
                                .append("Habit_id", 0)
                                .append("User_id", 0));

        //for each document in the collection
        results.forEach(item -> {
            try {
                //convert document to json
                JSONObject obj = new JSONObject(item.toJson());

                //find days array
                JSONArray days = obj.getJSONArray("Days");

                //for indices in days array
                for (int i = 0; i < days.length(); i++)
                {
                    //find date and status
                    JSONObject object = new JSONObject(days.get(i).toString());
                    String date = object.getString("Date");
                    boolean stat = object.getBoolean("Status");

                    //convert date into a CalendarDay
                    CalendarDay day = CalendarDay.from(StringToDate(date));

                    //based on status, add to hashset to make red or green highlight circles
                    if(stat){
                        didDoHabitDates.add(day);
                    }
                    else{
                        didNotDoHabitDates.add(day);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    //function to open add habit occurance
    public void openAddHabitOccurrenceActivity() {
        Intent intent = new Intent(this, AddHabitOccurrenceActivity.class);
        startActivity(intent);
    }
}
