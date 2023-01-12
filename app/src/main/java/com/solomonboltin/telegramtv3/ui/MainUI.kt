package com.solomonboltin.telegramtv3.ui

import android.util.Log
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.relay.compose.RelayContainer
import com.solomonboltin.telegramtv3.R
import com.solomonboltin.telegramtv3.connect.Connect
import com.solomonboltin.telegramtv3.vms.ClientVM
import com.solomonboltin.telegramtv3.ui.connection.ConnectionUI
import com.solomonboltin.telegramtv3.ui.movie.MoviesDashUI
import com.solomonboltin.telegramtv3.ui.movie.MyContent
import com.solomonboltin.telegramtv3.ui.movie.PlayMovieUI
import com.solomonboltin.telegramtv3.vms.FilesVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    val user by clientVM.user.collectAsState()
    MaterialTheme {
        RelayContainer {
            when (clientState) {
                is TdApi.AuthorizationStateReady -> {
                    if(playingMovie == null){
                        MoviesDashUI()
                    }
                    else{
//                        PlayMovieUI(movie = playingMovie!!)
                        println("Paling MyContent ")
                        MyContent(playingMovie!!)
                    }

                }
                else -> {
                    ConnectionUI()
                }
            }

        }
    }


}