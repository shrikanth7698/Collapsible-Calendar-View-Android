package com.shrikanthravi.collapsiblecalendarview.data;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shrikanthravi.collapsiblecalendarview.R;
import com.shrikanthravi.collapsiblecalendarview.widget.UICalendar;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import static org.threeten.bp.Month.DECEMBER;
import static org.threeten.bp.Month.JANUARY;

/**
 * Created by shrikanthravi on 06/03/18.
 */

public class CalendarAdapter {
    private DayOfWeek      mFirstDayOfWeek = DayOfWeek.MONDAY;
    private LocalDate      mCal;
    private LayoutInflater mInflater;
    private int            mEventDotSize   = UICalendar.EVENT_DOT_BIG;

    private List<LocalDate> mItemList  = new ArrayList<>();
    private List<View>      mViewList  = new ArrayList<>();
    private List<Event>     mEventList = new ArrayList<>();

    public CalendarAdapter(Context context) {
        this.mCal = LocalDate.now().withDayOfMonth(1);
        mInflater = LayoutInflater.from(context);

        refresh();
    }

    // public methods
    public int getCount() {
        return mItemList.size();
    }

    public LocalDate getItem(int position) {
        return mItemList.get(position);
    }

    public View getView(final int position) {
        return mViewList.get(position);
    }

    public void nextMonth() {
        mCal = mCal.plusMonths(1);
    }

    public void previousMonth() {
        mCal = mCal.minusMonths(1);
    }

    public void setDate(LocalDate date) {
        mCal = date;
    }

    public void setFirstDayOfWeek(DayOfWeek firstDayOfWeek) {
        mFirstDayOfWeek = firstDayOfWeek;
    }

    public void setEventDotSize(int eventDotSize) {
        mEventDotSize = eventDotSize;
    }

    public LocalDate getCalendar() {
        return mCal;
    }

    public void addEvent(Event event) {
        mEventList.add(event);
    }

    public void refresh() {
        // clear data
        mItemList.clear();
        mViewList.clear();

        // set calendar
        int year = mCal.getYear();
        int month = mCal.getMonthValue();

        mCal = LocalDate.of(year, month, 1);

        int lastDayOfMonth = mCal.lengthOfMonth();
        DayOfWeek firstDayOfWeek = mCal.getDayOfWeek();

        // generate day list
        int offset = 0 - (firstDayOfWeek.getValue() - mFirstDayOfWeek.getValue());
        if (offset > 0) offset += -7;
        int length = (int) Math.ceil((float) (lastDayOfMonth - offset) / 7) * 7;
        for (int i = offset; i < length + offset; i++) {
            int numYear;
            int numMonth;
            int numDay;

            if (i <= 0) { // prev month
                if (month == JANUARY.getValue()) {
                    numYear = year - 1;
                    numMonth = DECEMBER.getValue();
                } else {
                    numYear = year;
                    numMonth = month - 1;
                }
                LocalDate tempCal = LocalDate.of(numYear, numMonth, 1);
                numDay = tempCal.lengthOfMonth() + i;
            } else if (i > lastDayOfMonth) { // next month
                if (month == DECEMBER.getValue()) {
                    numYear = year + 1;
                    numMonth = JANUARY.getValue();
                } else {
                    numYear = year;
                    numMonth = month;
                }
                numDay = i - lastDayOfMonth;
            } else {
                numYear = year;
                numMonth = month;
                numDay = i;
            }

            LocalDate day = LocalDate.of(numYear, numMonth, numDay);
            View view;
            if (mEventDotSize == UICalendar.EVENT_DOT_SMALL) {
                view = mInflater.inflate(R.layout.day_layout_small, null);
            } else {
                view = mInflater.inflate(R.layout.day_layout, null);
            }

            TextView txtDay = view.findViewById(R.id.txt_day);
            ImageView imgEventTag = view.findViewById(R.id.img_event_tag);

            txtDay.setText(String.valueOf(day.getDayOfMonth()));
            if (day.getMonth() != mCal.getMonth()) {
                txtDay.setAlpha(0.3f);
            }

            for (int j = 0; j < mEventList.size(); j++) {
                Event event = mEventList.get(j);
                if (day.equals(event.getDate())) {
                    imgEventTag.setVisibility(View.VISIBLE);
                    imgEventTag.setColorFilter(event.getColor(), PorterDuff.Mode.SRC_ATOP);
                }
            }

            mItemList.add(day);
            mViewList.add(view);
        }
    }
}
