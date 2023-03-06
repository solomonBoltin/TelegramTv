package com.solomonboltin.telegramtv4.ui.dash.data

import com.solomonboltin.telegramtv4.tvb.scrappers.interfaces.ScrappedMovie

data class DashData(val movieLists: MutableList<MovieList>) {

    data class MovieList(val title: String, val movies: MutableList<ScrappedMovie>) {

        // next movie in the list
        fun nextMovie() {
            movies.add(movies.removeAt(0))
        }

        fun previousMovie() {
            movies.add(0, movies.removeAt(movies.size - 1))
        }
    }

    val selectedMovie: ScrappedMovie?
        get() {
            return movieLists.getOrNull(0)?.movies?.getOrNull(0)
        }

    fun addMovie(category: String, movie: ScrappedMovie): DashData {
        for (i in movieLists.indices) {
            if (movieLists[i].title == category) {
                movieLists[i].movies.add(movie)
                return this
            }
        }
        movieLists.add(MovieList(category, mutableListOf(movie)))
        return this
    }

    fun nextMovie(): DashData {
        movieLists.getOrNull(0)?.nextMovie()
        return this
    }

    // return copy of DashData. if there is first row and first movie it will append the last movie to the beginning of the list and remove the last movie
    fun previousMovie(): DashData {
        movieLists.getOrNull(0)?.previousMovie()
        return this
    }

    fun nextList(): DashData {
        movieLists.add(movieLists.removeAt(0))
        return this
    }

    fun previousList(): DashData {
        movieLists.add(0, movieLists.removeAt(movieLists.size - 1))
        return this
    }

    fun copy(): DashData {
        return DashData(movieLists.map { MovieList(it.title, it.movies.toMutableList()) }
            .toMutableList())
    }
}