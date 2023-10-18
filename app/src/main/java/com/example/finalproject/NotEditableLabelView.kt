package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView

@SuppressLint("ViewConstructor")
class NotEditableLabelView(context: Context, name: String, value: String) : LinearLayout(context) {
    private var nameView: TextView
    private var valueView: TextView

    init {
        orientation = HORIZONTAL
        nameView = TextView(context)
        nameView.text = name
        nameView.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        nameView.textSize = 30f
        this.addView(nameView)
        valueView = TextView(context)
        valueView.text = value
        valueView.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        valueView.textSize = 30f
        this.addView(valueView)
        this.setPadding(20, 20, 20, 20)
    }

    fun setValueView(s: String) {
        valueView.text = s
    }

    fun getValueView(): TextView {
        return valueView
    }
}