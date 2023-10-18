package com.example.finalproject

class Food(private var name: String, private var calories: Int) {
    private var macros: HashMap<String, Int> = HashMap()

    fun addMacro(name: String, amount: Int) {
        macros[name] = amount
    }

    fun getName(): String {
        return name
    }

    fun getCalories(): Int {
        return calories
    }

    fun getMacros(): HashMap<String, Int> {
        return macros
    }
}