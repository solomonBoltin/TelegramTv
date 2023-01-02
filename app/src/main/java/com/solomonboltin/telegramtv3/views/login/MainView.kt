package com.solomonboltin.telegramtv3.views.login

import android.content.Context
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solomonboltin.telegramtv3.telegram.ClientHandler
import com.solomonboltin.telegramtv3.telegram.getTdLibParams
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

enum class AppState {
    STARTING,
    CONNECTION,
    SAY_HELLO,
}

// sealed interface State with the following subclasses:
// 1. State.Starting (no parameters)
// 2. State.Connection (val connectionViewModel: ConnectionViewModel)
// 3. State.SayHello (val userInfo: String)

//sealed interface AppState {
//    class Starting : AppState
//    class Connection : AppState
//    class SayHello() : AppState
//}



class TgView(val context: Context) : ViewModel() {
    // Create the TDLib client
    // reactive state
    val client =     ClientHandler(context, this).client

    private var _authState = MutableStateFlow<TdApi.AuthorizationState?>(null)
    val authState: StateFlow<TdApi.AuthorizationState?> = _authState.asStateFlow()




//    val state = mutableStateOf<AppState>(AppState.STARTING)
    var state: MutableStateFlow<AppState> = MutableStateFlow(AppState.STARTING)
    val connectionView = connectionView(this)

    fun setAuthState(authState: TdApi.AuthorizationState) {
        print("authState: $authState")
        _authState.update {
            authState
        }
    }

}

// compose view that named sayHello displayes text "Hello World"
//
@Preview
@Composable
fun sayStarting() {
    Text("Starting ... ")
}

@Composable
fun sayHello(mainViewModel: TgView) {
    Text(text = "Hello World")
}

// composable fun MainView(viewModel: MainViewModel) that displays requestQrScan if state is ConnectionState.CONNECTION
// composable fun MainView(viewModel: MainViewModel) that displays sayHello if state is ConnectionState.SAY_HELLO

@Composable
fun MainView(viewModel: TgView) {
    print("MainView")
    Log.i("MainView", "MainView")

    val state by viewModel.authState.collectAsState()
    

    when (state) {

        is TdApi.AuthorizationState -> {
            ConnectionView(viewModel = viewModel)
        }
        else -> {
            Text(text = "Not Auth: $state")
        }
    }

}