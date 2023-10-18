package com.example.finalproject

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.util.*

class DietActivity: AppCompatActivity() {
    private lateinit var dietTracker: DietTracker
    private lateinit var totalCaloriesView: NotEditableLabelView
    private lateinit var linearLayout: LinearLayout
    private lateinit var scrollView: ScrollView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        dietTracker = DietTracker(this)

        val date: String? = intent.getStringExtra("date")

        scrollView = ScrollView(this)
        scrollView.setPadding(30, 30, 30, 30)

        linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL

        val tracker: MutableList<Food> = dietTracker.getTracker(date!!)

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
            val newTracker: MutableList<Food> = mutableListOf()
            for (i in 0 until linearLayout.childCount) {
                if (linearLayout.getChildAt(i) is LinearLayout) {
                    val ll: LinearLayout = linearLayout.getChildAt(i) as LinearLayout
                    if (ll.getChildAt(0) is DietView) {
                        val dietView = ll.getChildAt(0) as DietView
                        val foodName = dietView.getFoodName().text
                        val calories = dietView.getCalories().text

                        val cal: String = calories.toString().replace(",", "")
                        if (isNumber(cal)) {
                            val food = Food(foodName.toString(), cal.toInt())
                            val macros = dietView.getMacros()

                            for (macro in macros) {
                                val mac: String = macro.getMacroAmountView().text.toString().replace(",", "")
                                if (isNumber(mac)) {
                                    food.addMacro(
                                        macro.getMacroNameView().text.toString(),
                                        mac.toInt()
                                    )
                                }
                            }

                            newTracker.add(food)
                        }
                    }
                }
            }

            dietTracker.putTracker(date, newTracker)
            dietTracker.setPreferences()

            finish()
            overridePendingTransition(R.anim.slide_in_from_right, 0)
        }

        val initializer : MainActivity.AdInitializer = MainActivity.AdInitializer()
        MobileAds.initialize(this, initializer)

        val adView = AdView(this)
        val adSize = AdSize(AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT)
        adView.setAdSize(adSize)
        val adUnitId  = getString(R.string.ad_unit_id)
        adView.adUnitId = adUnitId

        val builder : AdRequest.Builder = AdRequest.Builder()
        builder.addKeyword("diet")
        builder.addKeyword("food")
        builder.addKeyword("fitness")
        val request : AdRequest = builder.build()

        linearLayout.addView(adView)

        try{
            adView.loadAd(request)
        } catch (e : Exception){
            Log.w("MainActivity", "Ad failed to load")
        }

        linearLayout.addView(backLayout)

        var totalCalories = 0

        for (food: Food in tracker) {
            totalCalories += food.getCalories()
        }

        totalCaloriesView = NotEditableLabelView(this, "Total Calories: ", totalCalories.toString())
        totalCaloriesView.gravity = Gravity.CENTER
        linearLayout.addView(totalCaloriesView)

        val addFood = Button(this)
        addFood.text = "Add Food"
        addFood.gravity = Gravity.CENTER
        addFood.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )

        val borderButtons = GradientDrawable()
        borderButtons.shape = GradientDrawable.RECTANGLE
        borderButtons.setColor(Color.parseColor("#C15C37"))
        borderButtons.setStroke(10, Color.parseColor("#244325"))
        borderButtons.cornerRadius = 16f
        addFood.background=borderButtons
        addFood.setOnClickListener {
            val dietLayout = LinearLayout(this)
            dietLayout.orientation = LinearLayout.VERTICAL
            val dietView = DietView(this, Food("", 0))
            dietLayout.addView(dietView)
            val dietButton = Button(this)
            dietButton.text = "Delete Food"
            dietButton.background = border
            dietButton.layoutParams = LinearLayout.LayoutParams(
                300,
                140,
            )
            dietButton.background = borderButtons
            dietButton.setOnClickListener {
                val toRemove: LinearLayout = dietLayout
                val dietView: DietView = toRemove.getChildAt(0) as DietView
                val calories: String = dietView.getCalories().text.toString().replace(",", "")
                if (isNumber(calories)) {
                    totalCaloriesView.setValueView(
                        (totalCaloriesView.getValueView().text.toString()
                            .toInt() - calories.toInt()).toString()
                    )
                }
                linearLayout.removeView(toRemove)
            }
            dietLayout.addView(dietButton)
            linearLayout.addView(dietLayout)
        }

        linearLayout.addView(addFood)

        for (food: Food in tracker) {
            val dietLayout = LinearLayout(this)
            dietLayout.orientation = LinearLayout.VERTICAL
            val dietView = DietView(this, food)
            dietLayout.addView(dietView)
            val dietButton = Button(this)
            dietButton.text = "Delete Food"
            dietButton.background = borderButtons
            dietButton.layoutParams = LinearLayout.LayoutParams(
                300,
                140
            )
            dietButton.setOnClickListener {
                val toRemove: LinearLayout = dietLayout
                val dietView: DietView = toRemove.getChildAt(0) as DietView
                val calories: String = dietView.getCalories().text.toString().replace(",", "")
                if (isNumber(calories)) {
                    totalCaloriesView.setValueView(
                        (totalCaloriesView.getValueView().text.toString()
                            .toInt() - calories.toInt()).toString()
                    )
                }
                linearLayout.removeView(toRemove)
            }

            dietLayout.addView(dietButton)

            linearLayout.addView(dietLayout)
        }

        scrollView.addView(linearLayout)
        scrollView.setBackgroundColor((Color.parseColor("#C1E1C1")))

        setContentView(scrollView)
    }

    private fun isNumber(s: String): Boolean {
        return when(s.toIntOrNull()) {
            null -> false
            else -> true
        }
    }

    fun setTotalCaloriesView() {
        val newTracker: MutableList<Food> = mutableListOf()
        for (i in 0 until linearLayout.childCount) {
            if (linearLayout.getChildAt(i) is LinearLayout) {
                val ll: LinearLayout = linearLayout.getChildAt(i) as LinearLayout
                if (ll.getChildAt(0) is DietView) {
                    val dietView = ll.getChildAt(0) as DietView

                    val foodName = dietView.getFoodName().text
                    val calories = dietView.getCalories().text

                    val cal: String = calories.toString().replace(",", "")

                    if (isNumber(cal)) {
                        val food = Food(foodName.toString(), cal.toInt())

                        val macros = dietView.getMacros()

                        for (macro in macros) {
                            val mac: String = macro.getMacroAmountView().text.toString().replace(",", "")
                            if (isNumber(mac)) {
                                food.addMacro(
                                    macro.getMacroNameView().text.toString(),
                                    mac.toInt()
                                )
                            }
                        }

                        newTracker.add(food)
                    }
                }
            }
        }

        var totalCalories = 0

        for (food: Food in newTracker) {
            totalCalories += food.getCalories()
        }

        totalCaloriesView.setValueView(totalCalories.toString())
    }
}