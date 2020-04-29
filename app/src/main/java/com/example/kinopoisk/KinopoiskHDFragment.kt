package com.example.kinopoisk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoisk.MovieResults.ResultsBean
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class KinopoiskHDFragment : Fragment() {
    var listOfMovies: List<ResultsBean?>? = null

    //for recycler view Adapter
    private val mMovieTitle = ArrayList<String?>()
    private val mMoviePoster = ArrayList<String>()
    private val mMovieYear = ArrayList<String>()
    private val mMovieRating = ArrayList<String>()
    private val mMovieRatersNumber = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.kinopoisk_fragment, container, false)
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val myInterface = retrofit.create(ApiInterface::class.java)
        val call = myInterface.listOfMovies()
        call!!.enqueue(object : Callback<MovieResults?> {
            override fun onResponse(call: Call<MovieResults?>, response: Response<MovieResults?>) {
                if (!response.isSuccessful) {
                    return
                }
                val results = response.body()
                listOfMovies = results?.results
                for (movie in listOfMovies!!) {
                    mMovieTitle.add(movie?.title)
                    mMoviePoster.add("https://image.tmdb.org/t/p/w185_and_h278_bestv2" + movie?.poster_path)
                    mMovieYear.add(movie?.release_date!!.substring(0, 4))
                    mMovieRating.add(java.lang.Double.toString(movie?.vote_average))
                    mMovieRatersNumber.add(Integer.toString(movie?.vote_count))
                }
                val recyclerView: RecyclerView = view.findViewById(R.id.popular_movies_recycler_view)
                val adapter = PopularMoviesListAdapter(view.context, mMovieTitle, mMoviePoster, mMovieYear, mMovieRating, mMovieRatersNumber)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(view.context)
            }

            override fun onFailure(call: Call<MovieResults?>, t: Throwable) {
                t.printStackTrace()
            }
        })
        return view
    }

    companion object {
        //for retrofit
        var BASE_URL = "https://api.themoviedb.org"
    }
}