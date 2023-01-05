package com.solomonboltin.telegramtv3.ui.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.solomonboltin.telegramtv3.models.Movie
import com.solomonboltin.telegramtv3.vms.MovieMachineVM
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun MovieUI(movie: Movie = Movie("The Matrix", "2000", TdApi.File(), TdApi.Message())) {
    val movieMachineVM = koinViewModel<MovieMachineVM>()

    Card(
        onClick = { /* Do something */ },
        modifier = Modifier.size(width = 180.dp, height = 100.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Text(movie.title, Modifier.align(Alignment.Center))
        }
    }



}