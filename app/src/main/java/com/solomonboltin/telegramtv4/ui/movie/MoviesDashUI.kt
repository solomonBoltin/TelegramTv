package com.solomonboltin.telegramtv4.ui.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.solomonboltin.telegramtv4.struck.DashUi
import com.solomonboltin.telegramtv4.struck.MovieDashUI
import com.solomonboltin.telegramtv4.struck.MovieDashVM
import com.solomonboltin.telegramtv4.vms.ClientVM
import org.koin.androidx.compose.koinViewModel


@Composable
fun MoviesDashUI() {
    val movieDashVM = koinViewModel<MovieDashVM>()
    val dashData by movieDashVM.dashData.collectAsState()
    println("Reloading MoviesDashUI")


    Box(Modifier.background(color = Color.DarkGray).fillMaxSize()){
        MovieDashUI(dashData = dashData)
        Text(text = dashData.movieLists.toString())
    }  


//    if (moviesState.isNotEmpty()) {
//        Column(modifier = Modifier
//            .padding(16.dp)
//            .verticalScroll(rememberScrollState())) {
//            moviesState.forEach { movie -> MovieUI(movie) }
//        }
//    }
//    else{
//        Text(text = "No movies found")
//    }

}