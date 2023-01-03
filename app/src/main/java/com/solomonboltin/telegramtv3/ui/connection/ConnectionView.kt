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
import com.solomonboltin.telegramtv3.vms.AppVm
import org.drinkless.td.libcore.telegram.TdApi

@Composable
fun ConnectionView(viewModel: AppVm) {

    val authState by viewModel.authState.collectAsState()
    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)
    ) {
        when (authState) {
            is TdApi.AuthorizationStateWaitOtherDeviceConfirmation -> RequestQrScan(viewModel)
            else -> Text(authState.toString())


        }
    }
}
