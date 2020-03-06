package com.example.incrementum;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
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

public class CalendarActivity extends AppCompatActivity {

    MaterialCalendarView calendarView;
    HashSet<CalendarDay> didNotDoHabitDates;
    HashSet<CalendarDay> didDoHabitDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);

        didNotDoHabitDates = new HashSet<>();
        didDoHabitDates = new HashSet<>();

        getDateValues();

        //sleep 1 sec to wait for getDateValues() to fill all hashsets from mongodb
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        calendarView.addDecorator(new CalendarDecorator(this, Color.GREEN, didNotDoHabitDates));
        calendarView.addDecorator(new CalendarDecorator(this, Color.RED, didDoHabitDates));



//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
//                Log.d("Select Date: ", date);
//            }
//        });

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

        //connect to database
        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        //connect to collection
        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Calendar");

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
                    Boolean stat = object.getBoolean("Status");

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
}
