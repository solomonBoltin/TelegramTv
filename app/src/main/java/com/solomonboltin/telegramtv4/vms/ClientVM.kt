package com.solomonboltin.telegramtv4.vms

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.solomonboltin.telegramtv4.configuration.getTdLibParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.log


class ClientVM : ViewModel() {
    private val context: Context by inject(Context::class.java)
    lateinit var client: Client

    private var fileUpdatesHandler: (TdApi.File) -> Unit = {
        Log.i("ClientVm", "No handler for file updates")
    }

    fun sendBlocked(ob: TdApi.Function) : TdApi.Object? {
        var response: TdApi.Object? = null
        client.send(ob, {
            response = it
        }, {
            Log.i("ClientVM", "Error: $it")
        })

        repeat(5 * 100) {
            Thread.sleep(10)
            if (response != null) {
                return response
            }
        }
        Log.i("ClientVM", "Error: $response")
        return response
    }

    fun setFileUpdatesHandler(handler: (TdApi.File) -> Unit) {
        fileUpdatesHandler = handler
    }


    private fun handleUpdates(update: TdApi.Object) {
        when (update) {
            is TdApi.UpdateAuthorizationState -> {
                handleAuthStateUpdate(update.authorizationState)
            }
            is TdApi.UpdateUser -> {
                handleUserUpdate(update.user)
            }
            is TdApi.UpdateFile -> {
                handleFileUpdates(update.file)
            }


        }
    }

    private fun handleFileUpdates(file: TdApi.File){
        fileUpdatesHandler(file)
    }

    private fun handleAuthStateUpdate(authState: TdApi.AuthorizationState) {
        setAuthState(authState)
        when (authState) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> {
                client.send(TdApi.SetTdlibParameters(getTdLibParams(context))) {}
            }
            is TdApi.AuthorizationStateWaitEncryptionKey -> {
                client.send(TdApi.CheckDatabaseEncryptionKey()) {}
            }
            is TdApi.AuthorizationStateWaitPhoneNumber -> {
                client.send(TdApi.RequestQrCodeAuthentication()) {}
            }
            is TdApi.AuthorizationStateClosed -> {
                restartClient()
            }
        }
    }

    private fun handleUserUpdate(user: TdApi.User) {
        setUser(user)
    }

    private fun createClient(): Client {
        return Client.create(
            { update ->
                handleUpdates(update)
            },
            { error ->
                Log.i("ClientHandler", "error: $error")
            },
            { ex ->
                Log.i("ClientException", ex.message.toString())
            }
        )
    }

    private fun restartClient() {
        client = createClient()
    }

    // Authentication state
    private var _authState = MutableStateFlow<TdApi.AuthorizationState?>(null)
    val authState: StateFlow<TdApi.AuthorizationState?> = _authState.asStateFlow()
    private fun setAuthState(authState: TdApi.AuthorizationState) {
        print("authState: $authState")
        _authState.update {
            authState
        }
    }

    // User state
    private var _user = MutableStateFlow<TdApi.User?>(null)
    val user: StateFlow<TdApi.User?> = _user.asStateFlow()
    fun setUser(user: TdApi.User) {
        _user.update { user }
    }


    init {
        restartClient()
    }

}


