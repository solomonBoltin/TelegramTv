package com.solomonboltin.telegramtv3.views.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.drinkless.td.libcore.telegram.TdApi

// jetpack compose viewModel that contains clientStatus of type ClientStatus
// ClientStatus is a sealed class that contains the following subclasses:
// 1. ClientStatus.Unknown
// 2. ClientStatus.WaitingForQrCode (contains linkString)
// 3. ClientStatus.Connected(userDetails: String)

// viewModel should be reactive to clientStatus and update itself when clientStatus changes

// viewModel should have a function that updates clientStatus


sealed interface ClientStatus {
    object Unknown : ClientStatus
    data class WaitingForQrCode(val linkString: String) : ClientStatus
    data class Connected(val userDetails: String) : ClientStatus

}

class connectionView(val tgView: TgView) : ViewModel() {
    val clientStatus = mutableStateOf<ClientStatus>(ClientStatus.Unknown)
    fun updateClientStatus(clientStatus: ClientStatus) {
        this.clientStatus.value = clientStatus
    }


}

// compose fun for ConnectionView that takes ConnectionViewModel as a parameter
// ConnectionView should be reactive to ConnectionViewModel and update itself when clientStatus changes
// Connection view should fit to entire tv screen and have a background color of gray
// in middle of screen there should be a text that says "Unknown client status" if clientStatus is ClientStatus.Unknown
// display RequestQrScan view if clientStatus is ClientStatus.WaitingForQrCode
// display hello : userInfo if clientStatus is ClientStatus.Connected


@Composable
fun ConnectionView(viewModel: TgView) {
    val authState = viewModel.authState.collectAsState()
    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)
    ) {
        when (authState.value) {
            is TdApi.AuthorizationStateWaitOtherDeviceConfirmation -> RequestQrScan(viewModel)
            else -> Text(authState.toString())


        }
    }
}
