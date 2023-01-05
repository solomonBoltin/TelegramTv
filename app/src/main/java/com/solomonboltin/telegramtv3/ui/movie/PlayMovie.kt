package com.solomonboltin.telegramtv3.ui.movie

import androidx.compose.runtime.Composable
import com.solomonboltin.telegramtv3.models.Movie
import com.solomonboltin.telegramtv3.vms.ClientVM
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.androidx.compose.koinViewModel


@Composable
fun PlayMovieUI(movie: Movie) {
    val clientVM = koinViewModel<ClientVM>()


}