package com.arvifox.arvi.simplemisc.tmdb

class MovieRepo(private val api: Apifactory.TmdbApi) : BaseRepo() {

    suspend fun getPopularMovies(): MutableList<TmdbMovie>? {
        val movieResponse = safeApiCall(
            call = { api.getPopularMovieAsync().await() },
            errorMessage = "Error Fetching Popular Movies"
        )
        return movieResponse?.results?.toMutableList()
    }
}