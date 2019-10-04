package com.hyperexternal.collapsiblecalendarview

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hyperexternal.collapsiblecalendarview.data.Day
import com.hyperexternal.collapsiblecalendarview.drawables.CircleDrawable
import com.hyperexternal.collapsiblecalendarview.view.OnSwipeTouchListener
import com.hyperexternal.collapsiblecalendarview.widget.CollapsibleCalendar
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        var TAG = "MainActivity";
    }

    lateinit var collapsibleCalendar: CollapsibleCalendar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.elevation = 0f
        window.statusBarColor = resources.getColor(R.color.google_red)
        var relativeLayout = findViewById<ScrollView>(R.id.scrollView)
        var textView = findViewById<TextView>(R.id.tv_date)

        collapsibleCalendar = findViewById(R.id.collapsibleCalendarView)
        relativeLayout.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {
            override fun onSwipeRight() {
                collapsibleCalendar.nextDay()
            }

            override fun onSwipeLeft() {
                collapsibleCalendar.prevDay()
            }

            override fun onSwipeTop() {
                if (collapsibleCalendar.expanded) {
                    collapsibleCalendar.collapse(400)
                }
            }

            override fun onSwipeBottom() {
                if (!collapsibleCalendar.expanded) {
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
        collapsibleCalendar.params = CollapsibleCalendar.Params(-60,
                180,
                true,
                Color.BLUE,
                4,
                CircleDrawable(context = this).setParams(CircleDrawable.Params(Color.parseColor("#50C2FF"), 24)).getCircle(),
                CircleDrawable(context = this).setParams(CircleDrawable.Params(Color.parseColor("#50C2FF".toTransparentColor(24)), 24)).getCircle()
        )
        collapsibleCalendar.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onTodayClick() {

            }

            override fun onFilterClick() {
                Toast.makeText(this@MainActivity, "onFilterClicked", Toast.LENGTH_LONG).show()
            }

            override fun onDayChanged() {

            }

            override fun onClickListener() {

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
