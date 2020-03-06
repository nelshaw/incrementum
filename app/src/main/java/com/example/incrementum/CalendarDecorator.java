package com.example.incrementum;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class CalendarDecorator implements DayViewDecorator {
//
//
//    public CalendarDecorator(int color, Collection<CalendarDay> dates) {
////        this.color = color;
////        this.dates = new HashSet<>(dates);
//        highlightDrawable = new ColorDrawable(color);
//        date = CalendarDay.today();
//    }
//
//    @Override
//    public boolean shouldDecorate(CalendarDay day) {
//        //return dates.contains(day);
//        return date != null && day.equals(date);
//    }
//
//    @Override
//    public void decorate(DayViewFacade view) {
////        view.addSpan(new DotSpan(10, color));
//        //view.addSpan(new CircularBorderDrawable(10, color));
//        //view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.selector));
//        view.addSpan(highlightDrawable);
//        view.addSpan(new ForegroundColorSpan(Color.RED));
//    }

    private Drawable highlightDrawable;
    private Context context;
    private final int color;
    private final HashSet<CalendarDay> dates;

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
        highlightDrawable.setTint(color);
        view.setBackgroundDrawable(highlightDrawable);
    }
}
