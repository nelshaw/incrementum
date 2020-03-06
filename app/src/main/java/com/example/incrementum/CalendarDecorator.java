package com.example.incrementum;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class CalendarDecorator implements DayViewDecorator {

    private Drawable highlightDrawable;
    private Context context;
    private int color;
    private HashSet<CalendarDay> dates;

    public CalendarDecorator( Context context, int color, Collection<CalendarDay> dates) {
        this.context = context;
        this.color = color;
        this.dates = new HashSet<>(dates);
        highlightDrawable = this.context.getResources().getDrawable(R.drawable.circlebackground);
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        highlightDrawable.mutate().setTint(color);
        view.setBackgroundDrawable(highlightDrawable);
    }

}
