package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

@SuppressLint("ViewConstructor")
class DietView @SuppressLint("SetTextI18n") constructor(
    private var context: Context,
    private var food: Food
): LinearLayout(context) {
    private var foodName: EditText
    private var calories: EditText
    private var macros: MutableList<MacroView> = mutableListOf()
    private var speechRecognizer: SpeechRecognizer
    private var speechButtonClicked: Int = 0


    init {
        this.orientation = VERTICAL
        setPadding(30, 30, 30, 30)

        val borderAddFood = GradientDrawable()
        borderAddFood.shape = GradientDrawable.RECTANGLE
        borderAddFood.setColor(Color.parseColor("#4A7E4B"))
        borderAddFood.setStroke(10, Color.parseColor("#244325"))
        borderAddFood.cornerRadius = 16f

        val foodLayout = LinearLayout(context)
        val foodView = LabelView(context, "Food: ", this.food.getName())
        foodName = foodView.getValueView()
        foodLayout.addView(foodView)
        val speechButton1 = Button(context)
        speechButton1.text = "Speech to Text"
        speechButton1.layoutParams = LayoutParams(
            400,
            120,
        )
        speechButton1.background = borderAddFood
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer.setRecognitionListener(SpeechRecognizerListener())
        speechButton1.setOnClickListener {
            val grantedPermission: Int =
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
            if (grantedPermission == PackageManager.PERMISSION_GRANTED) {
                speechButtonClicked = 0
                if (SpeechRecognizer.isRecognitionAvailable(context)) {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString())
                    Toast.makeText(context, "Speak now", Toast.LENGTH_SHORT).show()
                    speechRecognizer.startListening(intent)
                } else {
                    Toast.makeText(context, "Speech recognition not available on this device", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Need to grant microphone permission", Toast.LENGTH_SHORT).show()

                ActivityCompat.requestPermissions(
                    context as DietActivity,
                    arrayOf(android.Manifest.permission.RECORD_AUDIO),
                    200
                )
            }
        }
        foodLayout.addView(speechButton1)
        this.addView(foodLayout)

        val caloriesLayout = LinearLayout(context)
        val caloriesView = LabelView(context, "Calories: ", this.food.getCalories().toString())
        calories = caloriesView.getValueView()
        val speechButton2 = Button(context)
        speechButton2.text = "Speech to Text"
        speechButton2.layoutParams = LayoutParams(
            400,
            120,
        )
        speechButton2.background = borderAddFood
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer.setRecognitionListener(SpeechRecognizerListener())
        speechButton2.setOnClickListener {
            val grantedPermission: Int =
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
            if (grantedPermission == PackageManager.PERMISSION_GRANTED) {
                speechButtonClicked = 1
                if (SpeechRecognizer.isRecognitionAvailable(context)) {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString())
                    Toast.makeText(context, "Speak now", Toast.LENGTH_SHORT).show()
                    speechRecognizer.startListening(intent)
                } else {
                    Toast.makeText(context, "Speech recognition not available on this device", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(context, "Need to grant microphone permission", Toast.LENGTH_SHORT).show()

                ActivityCompat.requestPermissions(
                    context as DietActivity,
                    arrayOf(android.Manifest.permission.RECORD_AUDIO),
                    200
                )
            }
        }
        caloriesLayout.addView(caloriesView)
        caloriesLayout.addView(speechButton2)
        this.addView(caloriesLayout)

        val tch = TextChangeHandler()
        caloriesView.setHandler(tch)

        val macroLayout = LinearLayout(context)
        macroLayout.orientation = HORIZONTAL

        val macrosName = TextView(context)
        macrosName.text = "Macros: "
        macrosName.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        macrosName.textSize = 20f
        macroLayout.setPadding(20, 20, 20, 20)
        macroLayout.addView(macrosName)

        val addMacro = Button(context)
        addMacro.text = "Add Macro"
        addMacro.background = borderAddFood
        addMacro.layoutParams = LayoutParams(
            275,
            140
        )
        macroLayout.addView(addMacro)

        addMacro.setOnClickListener {
            val macroView = MacroView(context, "", 0)
            this.addView(macroView)
            macros.add(macroView)
        }
        this.addView(macroLayout)

        for ((macroName, macroAmount) in this.food.getMacros().entries) {
            val macroView = MacroView(context, macroName, macroAmount)
            this.addView(macroView)
            macros.add(macroView)
        }

        this.setPadding(20, 20, 20, 20)
    }

    fun getFoodName(): EditText {
        return foodName
    }

    fun getCalories(): EditText {
        return calories
    }

    fun getMacros(): MutableList<MacroView> {
        return macros
    }

    inner class TextChangeHandler: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            (context as DietActivity).setTotalCaloriesView()
        }
    }

    inner class SpeechRecognizerListener: RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "Audio error"
                SpeechRecognizer.ERROR_CLIENT -> "Client error"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions error"
                SpeechRecognizer.ERROR_NETWORK -> "Network error"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout error"
                SpeechRecognizer.ERROR_NO_MATCH -> "No match error"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy error"
                SpeechRecognizer.ERROR_SERVER -> "Server error"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech timeout error"
                else -> "Didn't understand, please try again."
            }
            Log.d("SpeechRecognizer", message)
        }
        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
        override fun onResults(results: Bundle?) {
            val resultArray = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val recognizedText: String = resultArray?.get(0)!!

            if (speechButtonClicked == 0) {
                foodName.setText(recognizedText)
            } else if (speechButtonClicked == 1) {
                calories.setText(recognizedText)
            }
        }
    }
}