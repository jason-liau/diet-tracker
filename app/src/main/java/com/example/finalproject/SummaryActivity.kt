package com.example.finalproject

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*


class SummaryActivity: AppCompatActivity() {
    private lateinit var dietTracker: DietTracker

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        dietTracker = DietTracker(this)

        val weekMap: HashMap<String, Int> = HashMap<String, Int>()
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        val foodMap = dietTracker.getMap()

        for ((dateKey, foodList) in foodMap) {
            val date = LocalDate.parse(dateKey, formatter)
            val year = date.year
            val week = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())
            val weekKey = "$year Week $week: "
            if (weekKey !in weekMap) {
                weekMap[weekKey] = 0
            }
            var sumCalories = 0
            for (food: Food in foodList) {
                sumCalories += food.getCalories()
            }
            weekMap[weekKey] = weekMap[weekKey]!! + sumCalories
        }

        val goBack = Button(this)
        val backLayout = LinearLayout(this)
        backLayout.addView(goBack)
        backLayout.setPadding(0, 30, 0, 0)
        goBack.text = "Back"
        goBack.layoutParams = LinearLayout.LayoutParams(
            200,
            140,
        )
        val border = GradientDrawable()
        border.shape = GradientDrawable.RECTANGLE
        border.setColor(Color.parseColor("#4A7E4B"))
        border.cornerRadius = 16f
        border.setStroke(10, Color.parseColor("#244325"))
        goBack.background = border
        goBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_from_right, 0)
        }

        var layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(backLayout)

        for (week in weekMap.keys.sorted()) {
            val numCalories = weekMap[week]
            layout.addView(NotEditableLabelView(this, week, numCalories.toString()))
        }

        var scrollView = ScrollView(this)
        scrollView.setPadding(30, 30, 30, 30)
        scrollView.setBackgroundColor((Color.parseColor("#C1E1C1")))

        scrollView.addView(layout)
        setContentView(scrollView)
    }
}