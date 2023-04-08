package com.example.kutta.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Rating
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import com.example.kutta.MainActivity
import com.example.kutta.R

class RatingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        val ratingBar = findViewById<RatingBar>(R.id.rb_ratingBar)

        ratingBar.rating = 2.5f
        ratingBar.stepSize = 0.5f

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            Toast.makeText(this, "Rating: $rating", Toast.LENGTH_SHORT).show()
        }

        val rating = findViewById<Button>(R.id.rating)
        rating.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

    }
}
