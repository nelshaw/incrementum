package com.example.incrementum;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.HashSet;

public class CalendarActivity extends AppCompatActivity {

    MaterialCalendarView calendarView;
    HashSet<CalendarDay> dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        dates = new HashSet<>();
        dates.add(CalendarDay.today());

        Log.d("date", CalendarDay.today().toString());

//        int redColorValue = Color.BLUE;
//        calendarView.addDecorator(new CalendarDecorator(redColorValue, dates));

        calendarView.addDecorator(new CalendarDecorator(this, Color.GREEN, dates));

//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
//                Log.d("Select Date: ", date);
//            }
//        });

    }
}
