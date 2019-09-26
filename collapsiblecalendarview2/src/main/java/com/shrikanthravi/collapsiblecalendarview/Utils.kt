package com.shrikanthravi.collapsiblecalendarview

import android.content.Context
import android.util.TypedValue
import java.util.*
import java.util.concurrent.TimeUnit

fun Context.dipToPixels(dipValue: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, resources.displayMetrics)
fun Context.dipToPixels(dipValue: Int) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue.toFloat(), resources.displayMetrics)

fun Calendar.daysBetween(endDate: Calendar): Long {
    val end = endDate.timeInMillis
    val start = this.timeInMillis
    return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start))
}
