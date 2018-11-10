package com.shrikanthravi.collapsiblecalendarview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.shrikanthravi.collapsiblecalendarview.R;
import com.shrikanthravi.collapsiblecalendarview.view.ExpandIconView;
import com.shrikanthravi.collapsiblecalendarview.view.LockScrollView;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;


public abstract class UICalendar extends LinearLayout {

    // State
    public static final int STATE_EXPANDED   = 0;
    public static final int STATE_COLLAPSED  = 1;
    public static final int STATE_PROCESSING = 2;
    public static final int EVENT_DOT_BIG = 0;
    public static final int EVENT_DOT_SMALL = 1;

    protected Context mContext;
    protected LayoutInflater mInflater;

    // UI
    protected LinearLayout mLayoutRoot;
    protected TextView mTxtTitle;
    protected TableLayout mTableHead;
    protected LockScrollView mScrollViewBody;
    protected TableLayout mTableBody;
    protected RelativeLayout mLayoutBtnGroupMonth;
    protected RelativeLayout mLayoutBtnGroupWeek;
    protected ImageView mBtnPrevMonth;
    protected ImageView mBtnNextMonth;
    protected ImageView mBtnPrevWeek;
    protected ImageView mBtnNextWeek;
    protected ExpandIconView expandIconView;

    // Attributes
    private boolean   mShowWeek       = true;
    private DayOfWeek mFirstDayOfWeek = DayOfWeek.SUNDAY;
    private int       mState          = STATE_COLLAPSED;

    private int mTextColor = Color.BLACK;
    private int mPrimaryColor = Color.WHITE;

    private int mTodayItemTextColor = Color.BLACK;
    private Drawable mTodayItemBackgroundDrawable =
            getResources().getDrawable(R.drawable.circle_black_stroke_background);
    private int mSelectedItemTextColor = Color.WHITE;
    private Drawable mSelectedItemBackgroundDrawable =
            getResources().getDrawable(R.drawable.circle_black_solid_background);

    private Drawable mButtonLeftDrawable =
            getResources().getDrawable(R.drawable.left_icon);
    private Drawable mButtonRightDrawable =
            getResources().getDrawable(R.drawable.right_icon);

    private LocalDate mSelectedItem = null;

    private int mButtonLeftDrawableTintColor=Color.BLACK;
    private int mButtonRightDrawableTintColor=Color.BLACK;

    private int mExpandIconColor = Color.BLACK;
    private int mEventColor=Color.BLACK;

    private int mEventDotSize=EVENT_DOT_BIG;

    public UICalendar(Context context) {
        this(context, null);
    }

    public UICalendar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UICalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.UICalendar, defStyleAttr, 0);
        setAttributes(attributes);
        attributes.recycle();
    }

    protected abstract void redraw();
    protected abstract void reload();

    protected void init(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

        // load rootView from xml
        View rootView = mInflater.inflate(R.layout.widget_collapsible_calendarview, this, true);

        // init UI
        mLayoutRoot          = rootView.findViewById(R.id.layout_root);
        mTxtTitle            = rootView.findViewById(R.id.txt_title);

        mTableHead           = rootView.findViewById(R.id.table_head);
        mScrollViewBody      = rootView.findViewById(R.id.scroll_view_body);
        mTableBody           = rootView.findViewById(R.id.table_body);
        mLayoutBtnGroupMonth = rootView.findViewById(R.id.layout_btn_group_month);
        mLayoutBtnGroupWeek  = rootView.findViewById(R.id.layout_btn_group_week);
        mBtnPrevMonth        = rootView.findViewById(R.id.btn_prev_month);
        mBtnNextMonth        = rootView.findViewById(R.id.btn_next_month);
        mBtnPrevWeek         = rootView.findViewById(R.id.btn_prev_week);
        mBtnNextWeek         = rootView.findViewById(R.id.btn_next_week);
        expandIconView       = rootView.findViewById(R.id.expandIcon);
    }

    protected void setAttributes(TypedArray attrs) {
        // set attributes by the values from XML
        //setStyle(attrs.getInt(R.styleable.UICalendar_style, mStyle));
        setShowWeek(attrs.getBoolean(R.styleable.UICalendar_showWeek, mShowWeek));
        int dayOfWeekValue = attrs.getInt(R.styleable.UICalendar_firstDayOfWeek, mFirstDayOfWeek.getValue());
        setFirstDayOfWeek(DayOfWeek.of(dayOfWeekValue));
        setState(attrs.getInt(R.styleable.UICalendar_state, mState));

        setTextColor(attrs.getColor(R.styleable.UICalendar_textColor, mTextColor));
        setPrimaryColor(attrs.getColor(R.styleable.UICalendar_primaryColor, mPrimaryColor));

        setEventColor(attrs.getColor(R.styleable.UICalendar_eventColor, mEventColor));
        setEventDotSize(attrs.getInt(R.styleable.UICalendar_eventDotSize, mEventDotSize));

        setTodayItemTextColor(attrs.getColor(
                R.styleable.UICalendar_todayItem_textColor, mTodayItemTextColor));
        Drawable todayItemBackgroundDrawable =
                attrs.getDrawable(R.styleable.UICalendar_todayItem_background);
        if (todayItemBackgroundDrawable != null) {
            setTodayItemBackgroundDrawable(todayItemBackgroundDrawable);
        } else {
            setTodayItemBackgroundDrawable(mTodayItemBackgroundDrawable);
        }

        setSelectedItemTextColor(attrs.getColor(
                R.styleable.UICalendar_selectedItem_textColor, mSelectedItemTextColor));
        Drawable selectedItemBackgroundDrawable =
                attrs.getDrawable(R.styleable.UICalendar_selectedItem_background);
        if (selectedItemBackgroundDrawable != null) {
            setSelectedItemBackgroundDrawable(selectedItemBackgroundDrawable);
        } else {
            setSelectedItemBackgroundDrawable(mSelectedItemBackgroundDrawable);
        }

        Drawable buttonLeftDrawable =
                attrs.getDrawable(R.styleable.UICalendar_buttonLeft_drawable);
        if (buttonLeftDrawable != null) {
            setButtonLeftDrawable(buttonLeftDrawable);
        } else {
            setButtonLeftDrawable(mButtonLeftDrawable);
        }

        Drawable buttonRightDrawable =
                attrs.getDrawable(R.styleable.UICalendar_buttonRight_drawable);
        if (buttonRightDrawable != null) {
            setButtonRightDrawable(buttonRightDrawable);
        } else {
            setButtonRightDrawable(mButtonRightDrawable);
        }

        setButtonLeftDrawableTintColor(attrs.getColor(R.styleable.UICalendar_buttonLeft_drawableTintColor,mButtonLeftDrawableTintColor));
        setButtonRightDrawableTintColor(attrs.getColor(R.styleable.UICalendar_buttonRight_drawableTintColor,mButtonRightDrawableTintColor));
        setExpandIconColor(attrs.getColor(R.styleable.UICalendar_expandIconColor,mExpandIconColor));
        mSelectedItem   = null;
    }

    public void setButtonLeftDrawableTintColor(int color){
        this.mButtonLeftDrawableTintColor = color;
        mBtnPrevMonth.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        mBtnPrevWeek.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        redraw();
    }

    public void setButtonRightDrawableTintColor(int color){

        this.mButtonRightDrawableTintColor = color;
        mBtnNextMonth.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        mBtnNextWeek.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        redraw();
    }

    public void setExpandIconColor(int color){
        this.mExpandIconColor = color;
        expandIconView.setColor(color);
    }

    public boolean isShowWeek() {
        return mShowWeek;
    }

    public void setShowWeek(boolean showWeek) {
        this.mShowWeek = showWeek;

        if (showWeek) {
            mTableHead.setVisibility(VISIBLE);
        } else {
            mTableHead.setVisibility(GONE);
        }
    }

    public DayOfWeek getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    public void setFirstDayOfWeek(DayOfWeek firstDayOfWeek) {
        this.mFirstDayOfWeek = firstDayOfWeek;
        reload();
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        this.mState = state;

        if (mState == STATE_EXPANDED) {
            mLayoutBtnGroupMonth.setVisibility(VISIBLE);
            mLayoutBtnGroupWeek.setVisibility(GONE);
        }
        if (mState == STATE_COLLAPSED) {
            mLayoutBtnGroupMonth.setVisibility(GONE);
            mLayoutBtnGroupWeek.setVisibility(VISIBLE);
        }
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        redraw();

        mTxtTitle.setTextColor(mTextColor);
    }

    public int getPrimaryColor() {
        return mPrimaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.mPrimaryColor = primaryColor;
        redraw();

        mLayoutRoot.setBackgroundColor(mPrimaryColor);
    }

    private void setEventColor(int eventColor) {
        this.mEventColor = eventColor;
        redraw();

    }

    private void setEventDotSize(int eventDotSize) {
        this.mEventDotSize = eventDotSize;
        redraw();

    }

    public int getEventDotSize() {
        return mEventDotSize;

    }
    public int getEventColor() {
        return mEventColor;
    }

    public int getTodayItemTextColor() {
        return mTodayItemTextColor;
    }

    public void setTodayItemTextColor(int todayItemTextColor) {
        this.mTodayItemTextColor = todayItemTextColor;
        redraw();
    }

    public Drawable getTodayItemBackgroundDrawable() {
        return mTodayItemBackgroundDrawable;
    }

    public void setTodayItemBackgroundDrawable(Drawable todayItemBackgroundDrawable) {
        this.mTodayItemBackgroundDrawable = todayItemBackgroundDrawable;
        redraw();
    }

    public int getSelectedItemTextColor() {
        return mSelectedItemTextColor;
    }

    public void setSelectedItemTextColor(int selectedItemTextColor) {
        this.mSelectedItemTextColor = selectedItemTextColor;
        redraw();
    }

    public Drawable getSelectedItemBackgroundDrawable() {
        return mSelectedItemBackgroundDrawable;
    }

    public void setSelectedItemBackgroundDrawable(Drawable selectedItemBackground) {
        this.mSelectedItemBackgroundDrawable = selectedItemBackground;
        redraw();
    }

    public void setButtonLeftDrawable(Drawable buttonLeftDrawable) {
        this.mButtonLeftDrawable = buttonLeftDrawable;
        mBtnPrevMonth.setImageDrawable(buttonLeftDrawable);
        mBtnPrevWeek.setImageDrawable(buttonLeftDrawable);
    }

    public void setButtonRightDrawable(Drawable buttonRightDrawable) {
        this.mButtonRightDrawable = buttonRightDrawable;
        mBtnNextMonth.setImageDrawable(buttonRightDrawable);
        mBtnNextWeek.setImageDrawable(buttonRightDrawable);
    }

    public LocalDate getSelectedItem() {
        return mSelectedItem;
    }

    public void setSelectedItem(LocalDate selectedDate) {
        this.mSelectedItem = selectedDate;
    }




}
