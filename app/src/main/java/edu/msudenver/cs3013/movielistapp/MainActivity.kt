package edu.msudenver.cs3013.movielistapp


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private var movieList = ArrayList<Movie>()
    private lateinit var myPlace: File

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    val title = it.getStringExtra("title") ?: ""
                    val year = it.getStringExtra("year") ?: ""
                    val genre = it.getStringExtra("genre") ?: ""
                    val rating = it.getStringExtra("rating") ?: ""
                    val newMovie = Movie(title, year, genre, rating)
                    movieList.add(newMovie)
                    movieAdapter.notifyDataSetChanged()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        myPlace = File(filesDir, "MOVIELIST.csv")
        readFile()
        movieAdapter = MovieAdapter(movieList)
        recyclerView.adapter = movieAdapter
        val itemTouchHelper = ItemTouchHelper(movieAdapter.swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun readFile() {
        movieList.clear()
        if (!myPlace.exists()) {
            return
        }
        try {
            val inputStream = FileInputStream(myPlace)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val tokens = line?.split(",")
                if (tokens != null && tokens.size == 4) {
                    val movie = Movie(tokens[0], tokens[1], tokens[2], tokens[3])
                    movieList.add(movie)
                }
            }
            reader.close()
        } catch (e: IOException) {
            Log.e("MOVIELIST", "Error reading file", e)
        }
    }

    private fun writeFile() {
        try {
            val outputStream = FileOutputStream(myPlace)
            val writer = BufferedWriter(OutputStreamWriter(outputStream))
            for (movie in movieList) {
                val line = "${movie.title},${movie.year},${movie.genre},${movie.rating}"
                writer.write(line)
                writer.newLine()
            }
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            Log.e("MOVIELIST", "Error writing file", e)
        }
    }

    fun saveList(v: View) {
        writeFile()
    }

    fun startSecond(v: View) {
        val intent = Intent(this, AddMovieActivity::class.java)
        startForResult.launch(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ratingSort -> {
                movieList.sortByDescending { it.rating }
                movieAdapter.notifyDataSetChanged()
                return true
            }
            R.id.yearSort -> {
                movieList.sortByDescending { it.year }
                movieAdapter.notifyDataSetChanged()
                return true
            }
            R.id.genreSort -> {
                movieList.sortBy { it.genre }
                movieAdapter.notifyDataSetChanged()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
