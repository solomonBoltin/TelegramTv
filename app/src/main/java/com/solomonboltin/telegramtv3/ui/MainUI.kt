package com.solomonboltin.telegramtv3.ui

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.solomonboltin.telegramtv3.vms.ClientVM
import com.solomonboltin.telegramtv3.ui.connection.ConnectionUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.androidx.compose.koinViewModel


@Composable
fun MainUI() {
    Log.i("MainUI", "Starting main ui")

    val clientVM = koinViewModel<ClientVM>()
    val clientState by clientVM.authState.collectAsState()

    val user by clientVM.user.collectAsState()

    when (clientState) {
        is TdApi.AuthorizationStateReady -> {
            Text(text = "Hello ${user?.firstName}")
            clientVM.client.send(TdApi.GetMe()) {
                Log.i("MainUI", "GetMe response: $it")
                clientVM.setUser(it as TdApi.User)
            }
        }
        else -> { ConnectionUI() }
    }

}