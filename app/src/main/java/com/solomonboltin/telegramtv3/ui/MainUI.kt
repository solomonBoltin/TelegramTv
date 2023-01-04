package com.solomonboltin.telegramtv3.ui

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.solomonboltin.telegramtv3.vms.ClientVM
import com.solomonboltin.telegramtv3.ui.connection.ConnectionUI
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.androidx.compose.koinViewModel


@Composable
fun MainUI() {
    Log.i("MainUI", "Starting main ui")

    val clientVM = koinViewModel<ClientVM>()
    val clientState by clientVM.authState.collectAsState()

    when (clientState) {
        is TdApi.AuthorizationStateReady -> {
            Text(text = "Connected $clientState")
        }
        else -> { ConnectionUI() }
    }

}