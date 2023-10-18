package com.example.finalproject

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DietTracker(context: Context) {
    private val context: Context
    private val trackers: Tracker

    init {
        this.context = context
        val pref: SharedPreferences =
            context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)

        val jsonString: String? = pref.getString("DietTracker", "")

        trackers = if (jsonString != null && jsonString != "") {
            Gson().fromJson(jsonString, object : TypeToken<Tracker>() {}.type)
        } else {
            Tracker()
        }
    }

    fun putTracker(date: String, tracker: MutableList<Food>) {
        trackers.putTracker(date, tracker)
    }

    fun getTracker(date: String): MutableList<Food> {
        return trackers.getTracker(date)
    }

    fun setPreferences() {
        val pref: SharedPreferences =
            context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = pref.edit()

        val jsonString: String = Gson().toJson(trackers)

        editor.putString("DietTracker", jsonString)
        editor.commit()
    }

    fun getMap(): HashMap<String, MutableList<Food>> {
        return trackers.getMap()
    }
}