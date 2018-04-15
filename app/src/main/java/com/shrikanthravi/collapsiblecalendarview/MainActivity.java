package com.shrikanthravi.collapsiblecalendarview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;
import com.shrikanthravi.collapsiblecalendarview.widget.UICalendar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        getWindow().setStatusBarColor(getResources().getColor(R.color.google_red));

        CollapsibleCalendar collapsibleCalendar = findViewById(R.id.collapsibleCalendarView);

        System.out.println("Testing date "+collapsibleCalendar.getSelectedDay().getDay()+"/"+collapsibleCalendar.getSelectedDay().getMonth()+"/"+collapsibleCalendar.getSelectedDay().getYear());
        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {

            }

            @Override
            public void onItemClick(View v) {

            }

            @Override
            public void onDataUpdate() {

            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int position) {

            }
        });


    }
}
