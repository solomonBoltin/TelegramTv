package com.solomonboltin.telegramtv3.ui.connection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.solomonboltin.telegramtv3.vms.ClientVM
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConnectionUI() {
    val clientVm: ClientVM = koinViewModel()
    val authState by clientVm.authState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)
    ) {
        when (authState) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> {
                Text("Connecting to telegram")
            }
            is TdApi.AuthorizationStateWaitEncryptionKey -> {
                Text("Waiting for encryption key")
            }
            is TdApi.AuthorizationStateWaitPhoneNumber -> Text("Wait phone number")
            is TdApi.AuthorizationStateWaitOtherDeviceConfirmation -> QrScanUI()
            is TdApi.AuthorizationStateClosed -> {
                Text(text = "Connection closed")
            }
            else -> Text("Unknown Unauthorized state: $authState")
        }
    }
}
