package com.hyperexternal.collapsiblecalendarview.drawable

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import com.hyperexternal.collapsiblecalendarview.dipToPixels


class CircleDrawable(val context: Context) {
    var params: Params? = null

    /**
     * If more than one params in future we will use Params class to set this function
     */
    data class Params(val backgroundColor: Int, val size: Int)

    fun setParams(params: Params): CircleDrawable {
        this.params = params
        return this
    }

    fun getCircle(): Drawable {
        var shape = GradientDrawable()
        shape.setShape(GradientDrawable.OVAL)
        shape.setCornerRadii(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f))
        params?.let {
            shape.setColor(it.backgroundColor)
            shape.setSize(context.dipToPixels(it.size.toFloat()).toInt(), context.dipToPixels(it.size).toInt())
        }
        return shape
    }
}