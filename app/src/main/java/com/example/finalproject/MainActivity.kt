package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener

class MainActivity : AppCompatActivity(), CalendarView.OnDateChangeListener {
    private lateinit var calendar: CalendarView
    private lateinit var summary: android.widget.Button
    private lateinit var text: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        setContentView(R.layout.activity_main)

        calendar = findViewById(R.id.calendar)
        summary = findViewById(R.id.summary)
        text = findViewById(R.id.text)

        summary.setOnClickListener {
            val intent = Intent(this, SummaryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.increasing_size, 0)
        }

        val initializer = AdInitializer()
        MobileAds.initialize(this, initializer)

        val adView = AdView(this)
        val adSize = AdSize(AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT)
        adView.setAdSize(adSize)
        val adUnitId: String = getString(R.string.ad_unit_id)
        adView.adUnitId = adUnitId

        val builder: AdRequest.Builder = AdRequest.Builder()
        builder.addKeyword("diet")
        builder.addKeyword("food")
        builder.addKeyword("fitness")
        val request: AdRequest = builder.build()

        val adLayout: LinearLayout = findViewById(R.id.ad_view)
        adLayout.addView(adView)

        try {
            adView.loadAd(request)
        } catch(e: Exception) {
            Log.w("MainActivity", "Ad failed to load")
        }

        calendar.setOnDateChangeListener(this)
    }

    override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, day: Int) {
        val date = "$day/${month+1}/$year"
        Log.w("MainActivity", date)

        val intent = Intent(this, DietActivity::class.java)
        intent.putExtra("date", date)
        startActivity(intent)
        overridePendingTransition(R.anim.increasing_size, 0)
    }

    class AdInitializer : OnInitializationCompleteListener {
        override fun onInitializationComplete(status: InitializationStatus) {
            Log.w( "MainActivty", "Ad initialization complete" )
        }
    }
}