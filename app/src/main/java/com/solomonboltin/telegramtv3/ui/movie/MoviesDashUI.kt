package com.solomonboltin.telegramtv3.ui.movie

import android.util.Log
import android.widget.GridLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.solomonboltin.telegramtv3.models.Movie
import com.solomonboltin.telegramtv3.vms.ClientVM
import com.solomonboltin.telegramtv3.vms.MovieMachineVM
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesDashUI() {
    val clientVM = koinViewModel<ClientVM>()
    val authState by clientVM.authState.collectAsState()

    val movieMachineVM = koinViewModel<MovieMachineVM>()
    val moviesState: List<Movie> by movieMachineVM.movies.collectAsState()
    // composable grid with representation for each movie
    // in the list of movies

    movieMachineVM.searchMessages("").onEach {
        Log.i("MSearchResult", it.content.toString())
    }

    Text(text ="MoviesDashUI $authState")
    Button(onClick = { movieMachineVM.searchMovies(""){} }) {
        Text(text = "Search Movies")
    }

    if (moviesState.isNotEmpty()) {
        Column(modifier = Modifier.padding(16.dp)) {
            moviesState.forEach { movie ->
                MovieUI(movie)
            }
        }
    }
    else{
        Text(text = "No movies found")
    }

}