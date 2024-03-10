package com.solomonboltin.telegramtv.ui.dash.data

import com.solomonboltin.telegramtv.data.scrappers.interfaces.MovieScrapper

data class DashData(val movieLists: MutableList<MovieList>, var selectedListIndex: Int = 0,
    ) {


    data class MovieList(
        val title: String,
        val movies: MutableList<MovieScrapper>,
        val chatId: Long = 0L,
        var selectedMovieIndex: Int = 0,
    ) {

        // next movie in the list
        fun selectNextMovie() {
            this.selectedMovieIndex = minOf(selectedMovieIndex + 1, movies.size - 1)
        }

        fun selectPreviousMovie() {
            this.selectedMovieIndex = maxOf(0, selectedMovieIndex - 1)
        }
    }

    val selectedMovie: MovieScrapper?
        get() {
            return movieLists.getOrNull(0)?.movies?.getOrNull(0)
        }

    fun addMovie(chatId: Long, movie: MovieScrapper): DashData {
        for (i in movieLists.indices) {
            if (movieLists[i].chatId == chatId) {
                movieLists[i].movies.add(movie)
                return this
            }
        }
        movieLists.add(MovieList("Chat: $chatId", mutableListOf(movie), chatId))
        return this
    }


    fun getMovieList(chatId: Long): MovieList? {
        return movieLists.find { it.chatId == chatId }
    }


    fun copy(): DashData {
        return DashData(movieLists.map { MovieList(it.title, it.movies.toMutableList(), it.chatId, it.selectedMovieIndex)}
            .toMutableList(), selectedListIndex)
    }
}