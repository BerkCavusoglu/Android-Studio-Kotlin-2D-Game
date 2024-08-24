package com.example.oyun2d

import android.content.Context
import android.content.Intent  // Intent'i import ettik
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultScreenActivity : AppCompatActivity() {

    private lateinit var total: TextView
    private lateinit var totalScoreTextView: TextView
    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_screen)

        total = findViewById(R.id.total)
        totalScoreTextView = findViewById(R.id.totalScore)
        startButton = findViewById(R.id.Start)

        val score = intent.getIntExtra("score", 0)
        totalScoreTextView.text = score.toString()

        val sp = getSharedPreferences("Sonuc", Context.MODE_PRIVATE)
        val savedTotalScore = sp.getInt("totalScore", 0)

        if (score > savedTotalScore) {
            val editor = sp.edit()
            editor.putInt("totalScore", score)
            editor.apply() // `commit` yerine `apply` kullanımı daha yaygındır.
            totalScoreTextView.text = score.toString()
        } else {
            totalScoreTextView.text = savedTotalScore.toString()
        }

        startButton.setOnClickListener {
            val intent = Intent(this@ResultScreenActivity, MainActivity::class.java)  // Intent'in tanımlanması
            startActivity(intent)
            finish()
        }
    }
}
