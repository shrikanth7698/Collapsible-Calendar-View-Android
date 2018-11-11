package com.shrikanthravi.collapsiblecalendarview.widget;

/**
 * Created by shrikanthravi on 07/03/18.
 */


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.shrikanthravi.collapsiblecalendarview.R;
import com.shrikanthravi.collapsiblecalendarview.data.CalendarAdapter;
import com.shrikanthravi.collapsiblecalendarview.data.Event;
import com.shrikanthravi.collapsiblecalendarview.listener.OnSwipeTouchListener;
import com.shrikanthravi.collapsiblecalendarview.view.ExpandIconView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import androidx.annotation.NonNull;

public class CollapsibleCalendar extends UICalendar {

    private CalendarAdapter  mAdapter;
    private CalendarListener mListener;

    private boolean expanded = false;

    private int mInitHeight = 0;

    private Handler mHandler            = new Handler();
    private boolean mIsWaitingForUpdate = false;

    private int mCurrentWeekIndex;

    public CollapsibleCalendar(Context context) {
        super(context);
    }

    public CollapsibleCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CollapsibleCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);


        int size = getEventDotSize();
        CalendarAdapter adapter = new CalendarAdapter(context);
        adapter.setEventDotSize(getEventDotSize());
        setAdapter(adapter);


        // bind events
        mLayoutRoot.setOnTouchListener(getSwipeTouchListener());
        mBtnPrevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevMonth();
            }
        });

        mBtnNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonth();
            }
        });

        mBtnPrevWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevWeek();
            }
        });

        mBtnNextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextWeek();
            }
        });

        expandIconView.setState(ExpandIconView.MORE, true);


        expandIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expanded) {
                    collapse(400);
                } else {
                    expand(400);
                }
            }
        });

        this.post(new Runnable() {
            @Override
            public void run() {
                collapseTo(mCurrentWeekIndex);
            }
        });


    }

    private OnSwipeTouchListener getSwipeTouchListener() {
        return new OnSwipeTouchListener(getContext()) {
            public void onSwipeTop() {
                collapse(400);

            }

            public void onSwipeLeft() {
                if (getState() == STATE_COLLAPSED) {
                    nextWeek();
                } else if (getState() == STATE_EXPANDED) {
                    nextMonth();
                }
            }

            public void onSwipeRight() {
                if (getState() == STATE_COLLAPSED) {
                    prevWeek();
                } else if (getState() == STATE_EXPANDED) {
                    prevMonth();
                }
            }

            public void onSwipeBottom() {
                expand(400);
            }

        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mInitHeight = mTableBody.getMeasuredHeight();

        if (mIsWaitingForUpdate) {
            redraw();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    collapseTo(mCurrentWeekIndex);
                }
            });
            mIsWaitingForUpdate = false;
            if (mListener != null) {
                mListener.onDataUpdate();
            }
        }
    }

    @Override
    protected void redraw() {
        // redraw all views of week
        TableRow rowWeek = (TableRow) mTableHead.getChildAt(0);
        if (rowWeek != null) {
            for (int i = 0; i < rowWeek.getChildCount(); i++) {
                ((TextView) rowWeek.getChildAt(i)).setTextColor(getTextColor());
            }
        }
        // redraw all views of day
        if (mAdapter != null) {
            for (int i = 0; i < mAdapter.getCount(); i++) {
                LocalDate day = mAdapter.getItem(i);
                View view = mAdapter.getView(i);
                TextView txtDay = view.findViewById(R.id.txt_day);
                txtDay.setBackgroundColor(Color.TRANSPARENT);
                txtDay.setTextColor(getTextColor());

                // set today's item
                if (isToady(day)) {
                    txtDay.setBackgroundDrawable(getTodayItemBackgroundDrawable());
                    txtDay.setTextColor(getTodayItemTextColor());
                }

                // set the selected item
                if (isSelectedDay(day)) {
                    txtDay.setBackgroundDrawable(getSelectedItemBackgroundDrawable());
                    txtDay.setTextColor(getSelectedItemTextColor());
                }
            }
        }
    }

    @Override
    protected void reload() {
        if (mAdapter != null) {
            mAdapter.setEventDotSize(getEventDotSize());
            mAdapter.refresh();

            // reset UI
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM YYYY");
            String formattedDate = dateFormat.format(mAdapter.getCalendar());
            mTxtTitle.setText(formattedDate);
            mTableHead.removeAllViews();
            mTableBody.removeAllViews();

            TableRow rowCurrent;

            // set day of week
            int[] dayOfWeekIds = {
                    R.string.sunday,
                    R.string.monday,
                    R.string.tuesday,
                    R.string.wednesday,
                    R.string.thursday,
                    R.string.friday,
                    R.string.saturday
            };
            rowCurrent = new TableRow(mContext);
            rowCurrent.setLayoutParams(new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            for (int i = 0; i < 7; i++) {
                View view = mInflater.inflate(R.layout.layout_day_of_week, null);
                TextView txtDayOfWeek = (TextView) view.findViewById(R.id.txt_day_of_week);
                txtDayOfWeek.setText(dayOfWeekIds[(i + getFirstDayOfWeek().getValue()) % 7]);
                view.setLayoutParams(new TableRow.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1));
                rowCurrent.addView(view);
            }
            mTableHead.addView(rowCurrent);

            // set day view
            for (int i = 0; i < mAdapter.getCount(); i++) {
                final int position = i;

                if (position % 7 == 0) {
                    rowCurrent = new TableRow(mContext);
                    rowCurrent.setLayoutParams(new TableLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    mTableBody.addView(rowCurrent);
                }
                final View view = mAdapter.getView(position);
                view.setLayoutParams(new TableRow.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1));
                view.setOnTouchListener(getSwipeTouchListener());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClicked(v, mAdapter.getItem(position));
                    }
                });
                rowCurrent.addView(view);
            }

            redraw();
            mIsWaitingForUpdate = true;
        }
    }

    private int getSuitableRowIndex() {
        if (getSelectedItemPosition() != -1) {
            View view = mAdapter.getView(getSelectedItemPosition());
            TableRow row = (TableRow) view.getParent();

            return mTableBody.indexOfChild(row);
        } else if (getTodayItemPosition() != -1) {
            View view = mAdapter.getView(getTodayItemPosition());
            TableRow row = (TableRow) view.getParent();

            return mTableBody.indexOfChild(row);
        } else {
            return 0;
        }
    }

    public void onItemClicked(View view, LocalDate day) {
        select(day);

        LocalDate cal = mAdapter.getCalendar();

        int newYear = day.getYear();
        int newMonth = day.getMonthValue() - 1;
        int oldYear = cal.getYear();
        int oldMonth = cal.getMonthValue();
        if (newMonth != oldMonth) {
            LocalDate d = day.withDayOfMonth(1);
            mAdapter.setDate(d);

            if (newYear > oldYear || newMonth > oldMonth) {
                mCurrentWeekIndex = 0;
            }
            if (newYear < oldYear || newMonth < oldMonth) {
                mCurrentWeekIndex = -1;
            }
            if (mListener != null) {
                mListener.onMonthChange();
            }
            reload();
        }

        if (mListener != null) {
            mListener.onItemClick(view);
        }
    }

    // public methods
    public void setAdapter(CalendarAdapter adapter) {
        mAdapter = adapter;
        adapter.setFirstDayOfWeek(getFirstDayOfWeek());

        reload();

        // init week
        mCurrentWeekIndex = getSuitableRowIndex();
    }

    public void addEventTag(LocalDate date) {
        mAdapter.addEvent(new Event(date, getEventColor()));

        reload();
    }

    public void addEventTag(LocalDate date, int color) {
        mAdapter.addEvent(new Event(date, color));

        reload();
    }

    public void prevMonth() {
        mAdapter.previousMonth();
        reload();
        if (mListener != null) {
            mListener.onMonthChange();
        }
    }

    public void nextMonth() {
        mAdapter.nextMonth();
        reload();
        if (mListener != null) {
            mListener.onMonthChange();
        }
    }

    public void prevWeek() {
        if (mCurrentWeekIndex - 1 < 0) {
            mCurrentWeekIndex = -1;
            prevMonth();
        } else {
            mCurrentWeekIndex--;
            collapseTo(mCurrentWeekIndex);
        }
    }

    public void nextWeek() {
        if (mCurrentWeekIndex + 1 >= mTableBody.getChildCount()) {
            mCurrentWeekIndex = 0;
            nextMonth();
        } else {
            mCurrentWeekIndex++;
            collapseTo(mCurrentWeekIndex);
        }
    }

    public LocalDate getSelectedDay() {
        if (getSelectedItem() == null) {
            return LocalDate.now();
        }
        return getSelectedItem();
    }

    public boolean isSelectedDay(@NonNull LocalDate day) {
        return day.equals(getSelectedItem());
    }

    public boolean isToady(@NonNull LocalDate day) {
        return LocalDate.now().equals(day);
    }

    public int getSelectedItemPosition() {
        int position = -1;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            LocalDate day = mAdapter.getItem(i);

            if (isSelectedDay(day)) {
                position = i;
                break;
            }
        }
        return position;
    }

    public int getTodayItemPosition() {
        int position = -1;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            LocalDate day = mAdapter.getItem(i);

            if (isToady(day)) {
                position = i;
                break;
            }
        }
        return position;
    }

    public void collapse(int duration) {

        if (getState() == STATE_EXPANDED) {
            setState(STATE_PROCESSING);

            mLayoutBtnGroupMonth.setVisibility(GONE);
            mLayoutBtnGroupWeek.setVisibility(VISIBLE);
            mBtnPrevWeek.setClickable(false);
            mBtnNextWeek.setClickable(false);

            int index = getSuitableRowIndex();
            mCurrentWeekIndex = index;

            final int currentHeight = mInitHeight;
            final int targetHeight = mTableBody.getChildAt(index).getMeasuredHeight();
            int tempHeight = 0;
            for (int i = 0; i < index; i++) {
                tempHeight += mTableBody.getChildAt(i).getMeasuredHeight();
            }
            final int topHeight = tempHeight;

            Animation anim = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {

                    mScrollViewBody.getLayoutParams().height = (interpolatedTime == 1)
                            ? targetHeight
                            : currentHeight - (int) ((currentHeight - targetHeight) * interpolatedTime);
                    mScrollViewBody.requestLayout();

                    if (mScrollViewBody.getMeasuredHeight() < topHeight + targetHeight) {
                        int position = topHeight + targetHeight - mScrollViewBody.getMeasuredHeight();
                        mScrollViewBody.smoothScrollTo(0, position);
                    }

                    if (interpolatedTime == 1) {
                        setState(STATE_COLLAPSED);

                        mBtnPrevWeek.setClickable(true);
                        mBtnNextWeek.setClickable(true);
                    }
                }
            };
            anim.setDuration(duration);
            startAnimation(anim);
        }

        expandIconView.setState(ExpandIconView.MORE, true);
    }

    private void collapseTo(int index) {
        if (getState() == STATE_COLLAPSED) {
            if (index == -1) {
                index = mTableBody.getChildCount() - 1;
            }
            mCurrentWeekIndex = index;

            final int targetHeight = mTableBody.getChildAt(index).getMeasuredHeight();
            int tempHeight = 0;
            for (int i = 0; i < index; i++) {
                tempHeight += mTableBody.getChildAt(i).getMeasuredHeight();
            }
            final int topHeight = tempHeight;

            mScrollViewBody.getLayoutParams().height = targetHeight;
            mScrollViewBody.requestLayout();

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mScrollViewBody.smoothScrollTo(0, topHeight);
                }
            });


            if (mListener != null) {
                mListener.onWeekChange(mCurrentWeekIndex);
            }
        }
    }

    public void expand(int duration) {
        if (getState() == STATE_COLLAPSED) {
            setState(STATE_PROCESSING);

            mLayoutBtnGroupMonth.setVisibility(VISIBLE);
            mLayoutBtnGroupWeek.setVisibility(GONE);
            mBtnPrevMonth.setClickable(false);
            mBtnNextMonth.setClickable(false);

            final int currentHeight = mScrollViewBody.getMeasuredHeight();
            final int targetHeight = mInitHeight;

            Animation anim = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {

                    mScrollViewBody.getLayoutParams().height = (interpolatedTime == 1)
                            ? LinearLayout.LayoutParams.WRAP_CONTENT
                            : currentHeight - (int) ((currentHeight - targetHeight) * interpolatedTime);
                    mScrollViewBody.requestLayout();

                    if (interpolatedTime == 1) {
                        setState(STATE_EXPANDED);

                        mBtnPrevMonth.setClickable(true);
                        mBtnNextMonth.setClickable(true);
                    }
                }
            };
            anim.setDuration(duration);
            startAnimation(anim);
        }

        expandIconView.setState(ExpandIconView.LESS, true);
    }

    @Override
    public void setState(int state) {
        super.setState(state);
        if (state == STATE_COLLAPSED) {
            expanded = false;
        }
        if (state == STATE_EXPANDED) {
            expanded = true;
        }
    }

    public void select(LocalDate day) {
        setSelectedItem(day);

        redraw();

        if (mListener != null) {
            mListener.onDaySelect();
        }
    }

    public void setStateWithUpdateUI(int state) {
        setState(state);

        if (getState() != state) {
            mIsWaitingForUpdate = true;
            requestLayout();
        }
    }

    // callback
    public void setCalendarListener(CalendarListener listener) {
        mListener = listener;
    }

    public interface CalendarListener {

        // triggered when a day is selected programmatically or clicked by user.
        void onDaySelect();

        // triggered only when the views of day on calendar are clicked by user.
        void onItemClick(View v);

        // triggered when the data of calendar are updated by changing month or adding events.
        void onDataUpdate();

        // triggered when the month are changed.
        void onMonthChange();

        // triggered when the week position are changed.
        void onWeekChange(int position);
    }


}

