package com.example.incrementum;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class CalendarDecorator implements DayViewDecorator {

    private Drawable highlightDrawable;
    private Context context;

    public CalendarDecorator( Context context) {
        this.context = context;
        highlightDrawable = this.context.getResources().getDrawable(R.drawable.circlebackground);
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(CalendarDay.today());
    }

    @Override
    public void decorate(DayViewFacade view) {
        highlightDrawable.setTint(Color.RED);
        view.setBackgroundDrawable(highlightDrawable);
    }
}
