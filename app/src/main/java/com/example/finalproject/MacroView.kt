package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout

@SuppressLint("ViewConstructor")
class MacroView(context: Context, macroName: String, macroAmount: Int) : LinearLayout(context) {
    private var macroNameView: EditText
    private var macroAmountView: EditText

    init {
        orientation = HORIZONTAL
        macroNameView = EditText(context)
        macroNameView.setText(macroName)
        macroNameView.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        this.addView(macroNameView)
        macroAmountView = EditText(context)
        macroAmountView.setText(macroAmount.toString())
        macroAmountView.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        this.addView(macroAmountView)
        this.setPadding(20, 0, 20, 10)
    }

    fun getMacroNameView(): EditText {
        return macroNameView
    }

    fun getMacroAmountView(): EditText {
        return macroAmountView
    }
}