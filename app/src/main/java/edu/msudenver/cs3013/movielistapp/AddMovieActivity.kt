package edu.msudenver.cs3013.movielistapp


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddMovieActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var yearEditText: EditText
    private lateinit var genreEditText: EditText
    private lateinit var ratingEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        titleEditText = findViewById(R.id.titleEditText)
        yearEditText = findViewById(R.id.yearEditText)
        genreEditText = findViewById(R.id.genreEditText)
        ratingEditText = findViewById(R.id.ratingEditText)
    }

    fun backToFirst(v: View) {
        val title = titleEditText.text.toString()
        val year = yearEditText.text.toString()
        val genre = genreEditText.text.toString()
        val rating = ratingEditText.text.toString()

        setMovieInfo(title, year, genre, rating)
    }

    private fun setMovieInfo(title: String, year: String, genre: String, rating: String) {
        val intent = Intent()
        intent.putExtra("title", title)
        intent.putExtra("year", year)
        intent.putExtra("genre", genre)
        intent.putExtra("rating", rating)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
