package com.shrikanthravi.collapsiblecalendarview.data;

/**
 * Created by shrikanthravi on 06/03/18.
 */

public class Event {
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mColor;

    public Event(int year, int month, int day){
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
    }

    public Event(int year, int month, int day, int color){
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
        this.mColor=color;
    }

    public int getMonth(){
        return mMonth;
    }

    public int getYear(){
        return mYear;
    }

    public int getDay(){
        return mDay;
    }

    public int getColor() {
        return mColor;
    }
}
