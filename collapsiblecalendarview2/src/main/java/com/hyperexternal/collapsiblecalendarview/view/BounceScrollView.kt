package com.hyperexternal.collapsiblecalendarview.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.core.widget.NestedScrollView

class BounceScrollView : NestedScrollView {
    private var contentView: View? = null
    private var startX: Float = 0.toFloat()
    private val originalRect = Rect()
    private var canPullDown = false
    private var canPullUp = false
    var isMoved = false


    private var downX: Int = 0
    private var downY: Int = 0
    private val mTouchSlop = 0


    private val isCanPullDown: Boolean
        get() = scrollY == 0 || contentView!!.height < height + scrollY


    private val isCanPullUp: Boolean
        get() = contentView!!.height <= height + scrollY

    constructor(context: Context) : super(context) {
        setFadingEdgeLength(0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setFadingEdgeLength(0)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 0) {
            contentView = getChildAt(0)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (contentView == null)
            return
        originalRect.set(contentView!!.left, contentView!!.top, contentView!!.right, contentView!!.bottom)
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (contentView == null) {
            return super.dispatchTouchEvent(ev)
        }
        val action = ev.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                canPullDown = isCanPullDown
                canPullUp = isCanPullUp
                startX = ev.x
            }
            MotionEvent.ACTION_UP->{
                if(isMoved){
                    boundBack()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if(!isMoved)
                {
                    return super.dispatchTouchEvent(ev)
                }
                if (!canPullDown && !canPullUp) {
                    startX = ev.y
                    canPullDown = isCanPullDown
                    canPullUp = isCanPullUp

                } else {
                    val nowX = ev.x
                    val deltaY = (nowX - startX).toInt()
                    val shouldMove = (canPullDown && deltaY > 0
                            || canPullUp && deltaY < 0
                            || canPullUp && canPullDown)
                    if (shouldMove) {
                        val offset = (deltaY * MOVE_FACTOR).toInt()

                        contentView!!.layout(originalRect.left + offset, originalRect.top, originalRect.right + offset, originalRect.bottom)
                    }
                }
            }
            else -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                Log.e(TAG, "--qydq->ACTION_DOWN")
                downX = ev.rawX.toInt()
                downY = ev.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val moveY = ev.rawY.toInt()
                val moveX = ev.rawX.toInt()
                if (Math.abs(moveY - downY) > Math.abs(moveX - downX)) {
                    Log.e(TAG, "--qydq->ACTION_MOVED")
                    return true
                }
                if (moveX - downX > Math.abs(moveY - downY)) {
                    Log.e(TAG, "--qydq->ACTION_RIGHT")
                    return false
                }
                if (moveX - downX < Math.abs(moveY - downY)) {
                    Log.e(TAG, "--qydq->ACTION_LEFT")
                    return false
                }
            }
            else -> {
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun boundBack() {
        if (!isMoved)
            return
        val anim = TranslateAnimation(0f, 0f, contentView!!.top.toFloat(), originalRect.top.toFloat())
        anim.duration = ANIM_TIME.toLong()
        contentView!!.startAnimation(anim)
        contentView!!.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom)
        canPullDown = false
        canPullUp = false
        isMoved = false
    }

    override fun fling(velocityY: Int) {
        super.fling(velocityY / 2)
    }

    companion object {

        private val TAG = "BounceScrollView "
        private val MOVE_FACTOR = 0.6f

        private val ANIM_TIME = 350
    }

}