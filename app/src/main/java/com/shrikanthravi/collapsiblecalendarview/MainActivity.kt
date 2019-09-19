package com.shrikanthravi.collapsiblecalendarview

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.view.OnSwipeTouchListener

import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import com.shrikanthravi.collapsiblecalendarview.widget.UICalendar

import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar


class MainActivity : AppCompatActivity(){
    companion object{
        var TAG = "MainActivity";
    }

    lateinit var collapsibleCalendar:CollapsibleCalendar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.elevation = 0f
        window.statusBarColor = resources.getColor(R.color.google_red)
        var relativeLayout = findViewById<ScrollView>(R.id.scrollView)
        var textView = findViewById<TextView>(R.id.tv_date)

        collapsibleCalendar = findViewById(R.id.collapsibleCalendarView)
        relativeLayout.setOnTouchListener(object:OnSwipeTouchListener(this@MainActivity){
            override fun onSwipeRight() {
                collapsibleCalendar.nextDay()
            }

            override fun onSwipeLeft() {
                collapsibleCalendar.prevDay()
            }

            override fun onSwipeTop() {
                if(collapsibleCalendar.expanded){
                    collapsibleCalendar.collapse(400)
                }
            }

            override fun onSwipeBottom() {
                if(!collapsibleCalendar.expanded){
                    collapsibleCalendar.expand(400)
                }
            }
        });
        //To hide or show expand icon
        collapsibleCalendar.setExpandIconVisible(true)
        val today = GregorianCalendar()
        collapsibleCalendar.addEventTag(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        today.add(Calendar.DATE, 1)
        collapsibleCalendar.selectedDay = Day(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        collapsibleCalendar.addEventTag(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), Color.BLUE)
        collapsibleCalendar.params = CollapsibleCalendar.Params(-100, 100)
        collapsibleCalendar.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onDayChanged() {

            }

            override fun onClickListener() {
                if(collapsibleCalendar.expanded){
                    collapsibleCalendar.collapse(400)
                }
                else{
                    collapsibleCalendar.expand(400)
                }
            }

            override fun onDaySelect() {
                textView.text = collapsibleCalendar.selectedDay?.toUnixTime().toString()
            }

            override fun onItemClick(v: View) {

            }

            override fun onDataUpdate() {

            }

            override fun onMonthChange() {

            }

            override fun onWeekChange(position: Int) {

            }
        })


    }
}
