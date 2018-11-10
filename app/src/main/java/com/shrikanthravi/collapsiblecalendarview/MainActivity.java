package com.shrikanthravi.collapsiblecalendarview;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import org.threeten.bp.LocalDate;

import java.util.GregorianCalendar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        getWindow().setStatusBarColor(getResources().getColor(R.color.google_red));

        CollapsibleCalendar collapsibleCalendar = findViewById(R.id.collapsibleCalendarView);
        LocalDate today = LocalDate.now();
        collapsibleCalendar.addEventTag(today);
        LocalDate tomorrow = today.plusDays(1);
        collapsibleCalendar.addEventTag(tomorrow, Color.BLUE);

        Log.d("Testing date ", collapsibleCalendar.getSelectedDay().toString());
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
