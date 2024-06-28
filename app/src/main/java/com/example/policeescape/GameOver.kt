package com.example.policeescape

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOver : AppCompatActivity() {
    private lateinit var tvPoints: TextView
    private lateinit var tvHighest: TextView
    private lateinit var ivNewHighest: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        tvPoints = findViewById(R.id.tvPoints)
        tvHighest = findViewById(R.id.tvHighest)
        ivNewHighest = findViewById(R.id.ivNewHighest)
        sharedPreferences = getSharedPreferences("my_pref", MODE_PRIVATE)

        val points = intent.getIntExtra("points", 0)

        tvPoints.text = points.toString()

        // Get highest score
        var highest = sharedPreferences.getInt("highest", 0)

        if (points > highest) {
            // Show the new highest score
            ivNewHighest.visibility = View.VISIBLE

            // Update the highest score
            highest = points

            // Save the new highest score
            val editor = sharedPreferences.edit()
            editor.putInt("highest", highest)
            editor.apply()
        }

        // Update highest score
        tvHighest.text = highest.toString()
    }

    // Restart the game
    fun restart(view: View) {
        val intent = Intent(this@GameOver, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Exit the game
    fun exit(view: View) {
        finish()
    }
}
