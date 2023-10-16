package com.example.chatapplication

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.DatePicker


class CustomDatePicker: DatePicker{

    constructor(context: Context?):super(context)
    constructor(context: Context?, attrs: AttributeSet?):super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        if (ev!!.actionMasked === MotionEvent.ACTION_DOWN) {
            val p = parent
            p?.requestDisallowInterceptTouchEvent(true)
        }

        return false
    }

}