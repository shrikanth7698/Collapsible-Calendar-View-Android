package com.shrikanthravi.collapsiblecalendarview.data;

import org.threeten.bp.LocalDate;

/**
 * Created by shrikanthravi on 06/03/18.
 */

public class Event {
    private final LocalDate date;

    private int mColor;

    public Event(LocalDate date) {
        this.date = date;
    }

    public Event(LocalDate date, int color) {
        this.date = date;
        this.mColor = color;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getColor() {
        return mColor;
    }
}
