package com.arvifox.arvi.simplemisc.tmdb

data class TmdbMovie(
        val id: Int,
        val vote_average: Double,
        val title: String,
        val overview: String,
        val adult: Boolean
)