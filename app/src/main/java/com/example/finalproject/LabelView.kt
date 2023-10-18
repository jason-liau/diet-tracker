package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

@SuppressLint("ViewConstructor")
class LabelView(context: Context, name: String, value: String) : LinearLayout(context) {
    private var nameView: TextView
    private var valueView: EditText

    init {
        orientation = HORIZONTAL
        nameView = TextView(context)
        nameView.text = name
        nameView.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        nameView.textSize = 20f
        this.addView(nameView)
        valueView = EditText(context)
        valueView.setText(value)
        valueView.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        valueView.textSize = 20f
        this.addView(valueView)
        this.setPadding(20, 20, 20, 20)
    }

    fun getValueView(): EditText {
        return valueView
    }

    fun setHandler(handler: TextWatcher) {
        valueView.addTextChangedListener(handler)
    }
}