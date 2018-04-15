package com.shrikanthravi.collapsiblecalendarview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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


    }
}
