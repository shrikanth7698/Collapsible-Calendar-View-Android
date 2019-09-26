package com.shrikanthravi.collapsiblecalendarview

import android.content.Context
import android.util.TypedValue

/**
 * division of responsibility between projects
 */
fun Context.dipToPixels(dipValue: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, resources.displayMetrics)
fun Context.dipToPixels(dipValue: Int) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue.toFloat(), resources.displayMetrics)