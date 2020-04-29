package com.example.kinopoisk

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinopoisk.PopularMoviesListAdapter.ViewHolderPopularMovies
import java.util.*

class PopularMoviesListAdapter(private val mContext: Context, private val mMovieTitle: ArrayList<String?>, private val mMoviePoster: ArrayList<String>, private val mMovieYear: ArrayList<String>, private val mMovieRating: ArrayList<String>, private val mMovieRatersNumber: ArrayList<String>) : RecyclerView.Adapter<ViewHolderPopularMovies>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPopularMovies {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.kinopoisk_hd_movie_item, parent, false)
        return ViewHolderPopularMovies(view)
    }

    override fun onBindViewHolder(holder: ViewHolderPopularMovies, position: Int) {
        Glide.with(mContext).asBitmap().load(mMoviePoster[position]).into(holder.moviePoster)
        holder.movieTitle.text = mMovieTitle[position]
        holder.movieDate.text = mMovieYear[position]
        holder.movieRating.text = mMovieRating[position]
        holder.movieRatersNumber.text = mMovieRatersNumber[position]
    }

    override fun getItemCount(): Int {
        return mMoviePoster.size
    }

    inner class ViewHolderPopularMovies(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var moviePoster: ImageView
        var movieTitle: TextView
        var movieDate: TextView
        var movieRating: TextView
        var movieRatersNumber: TextView

        init {
            moviePoster = itemView.findViewById(R.id.movie_poster)
            movieTitle = itemView.findViewById(R.id.movie_title)
            movieDate = itemView.findViewById(R.id.year_of_release)
            movieRating = itemView.findViewById(R.id.rating_of_movie)
            movieRatersNumber = itemView.findViewById(R.id.number_of_raters)
        }
    }

}