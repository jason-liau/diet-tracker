package com.example.finalproject

class Tracker {
    private val foods: HashMap<String, MutableList<Food>> = HashMap()

    fun putTracker(date: String, tracker: MutableList<Food>) {
        foods[date] = tracker
    }

    fun getTracker(date: String): MutableList<Food> {
        if (!foods.containsKey(date)) {
            foods[date] = mutableListOf()
        }

        return foods[date]!!
    }

    fun getMap(): HashMap<String, MutableList<Food>> {
        return foods
    }
}