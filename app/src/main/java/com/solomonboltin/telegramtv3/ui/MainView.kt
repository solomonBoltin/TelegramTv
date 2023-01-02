package com.solomonboltin.telegramtv3.ui

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.solomonboltin.telegramtv3.ui.connection.ConnectionView
import com.solomonboltin.telegramtv3.vms.AppVm
import org.drinkless.td.libcore.telegram.TdApi


@Composable
fun MainView(appVm: AppVm) {
    print("MainView")
    Log.i("MainView", "MainView")

    val state by appVm.authState.collectAsState()


    when (state) {

        is TdApi.AuthorizationState -> {
            ConnectionView(viewModel = appVm)
        }
        else -> {
            Text(text = "Not Auth: $state")
        }
    }

}