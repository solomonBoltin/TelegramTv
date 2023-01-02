package com.solomonboltin.telegramtv3.vms

import android.content.Context
import androidx.lifecycle.ViewModel
import com.solomonboltin.telegramtv3.telegram.ClientHandler
import com.solomonboltin.telegramtv3.telegram.getClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.td.libcore.telegram.TdApi

class AppVm(context: Context) : ViewModel() {
    // Create the TDLib client
    // reactive state
    val client = getClient(context, this)


    private var _authState = MutableStateFlow<TdApi.AuthorizationState?>(null)
    val authState: StateFlow<TdApi.AuthorizationState?> = _authState.asStateFlow()


    val connectionView = ConnectionVm()

    fun setAuthState(authState: TdApi.AuthorizationState) {
        print("authState: $authState")
        _authState.update {
            authState
        }
    }

}
