package com.solomonboltin.telegramtv4.ui

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.grid.TvGridCells
import com.google.relay.compose.BoxScopeInstance.boxAlign
import com.google.relay.compose.BoxScopeInstanceImpl.align
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RowScopeInstanceImpl.align
import com.solomonboltin.telegramtv4.frame18.Frame18
import com.solomonboltin.telegramtv4.movieview.MovieView
import com.solomonboltin.telegramtv4.struck.MovieDashVM
import com.solomonboltin.telegramtv4.tv.Tv
import com.solomonboltin.telegramtv4.vms.ClientVM
import com.solomonboltin.telegramtv4.ui.connection.ConnectionUI
import com.solomonboltin.telegramtv4.ui.movie.MoviesDashUI
import com.solomonboltin.telegramtv4.ui.movie.MyContent
import com.solomonboltin.telegramtv4.vms.FilesVM
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.androidx.compose.koinViewModel

@Preview()
@Composable
fun MainUI() {
    Log.i("MainUI", "Starting main ui")

    val clientVM = koinViewModel<ClientVM>()
    val clientState by clientVM.authState.collectAsState()

    val filesVM = koinViewModel<FilesVM>()
    val playingMovie by filesVM.playingMovie.collectAsState()

    val movieDashVM = koinViewModel<MovieDashVM>()


    val user by clientVM.user.collectAsState()
//    val itemsList = (0..5).toList()
//
//    LazyRow(){
//        items(itemsList){
//            Box(modifier = Modifier.background(Color.Yellow).size(100.dp).border(BorderStroke(3.dp, Color.Black)).padding(start = 10.dp)){
//            }
//        }
//    }

    when (clientState) {
        is TdApi.AuthorizationStateReady -> {
            if (playingMovie == null) {
                movieDashVM.start()
                MoviesDashUI()
            } else {
                println("Paling MyContent ")
                MyContent(playingMovie!!)
            }

        }
        else -> {
            ConnectionUI()
        }
    }
//    MaterialTheme{
//        RelayContainer{
//            Text(text = "Hello world" )
//            Column {
//                MovieView(
//                    Modifier
//                        .widthIn(0.dp, 250.dp)
//                        .heightIn(0.dp, 420.dp))
//                Icon(Icons.Outlined.ThumbUp, contentDescription = "null")
//            }
//        }
//    }
//
//    Tv(                Modifier
//        .fillMaxSize()
//        .widthIn(0.dp, 890.dp)
////        .boxAlign(Alignment.Center, DpOffset.Unspecified)
////            .horizontalScroll(ScrollState(0))
////            .verticalScroll(ScrollState(0))
//    )

}


@Composable
fun MovieActionBar() {
    Column {
        Modifier
    }
}

//    MaterialTheme {
//        RelayContainer {
//
//
////            when (clientState) {
////                is TdApi.AuthorizationStateReady -> {
////                    if(playingMovie == null){
////                        MoviesDashUI()
////                    }
////                    else{
//////                        PlayMovieUI(movie = playingMovie!!)
////                        println("Paling MyContent ")
////                        MyContent(playingMovie!!)
////                    }
////
////                }
////                else -> {
////                    ConnectionUI()
////                }
////            }
//
//        }
//    }


