package com.example.kinopoisk

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("/3/movie/popular?api_key=cd47138b0a84478731a482e5b4c68df4&language=en-US&page=1")
    fun listOfMovies(): Call<MovieResults?>?
}