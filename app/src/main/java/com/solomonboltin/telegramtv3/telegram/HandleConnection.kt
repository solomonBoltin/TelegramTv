package com.solomonboltin.telegramtv3.telegram

import android.content.Context
import com.solomonboltin.telegramtv3.models.ClientStatus
import com.solomonboltin.telegramtv3.vms.AppVm

import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

//import com.solomonboltin.telegramtv3.views.login.MainView
//
//fun handelConnection(context: Context, viewModel: MainViewModel) {
//
//}
class ClientHandler(val context: Context, val viewModel: AppVm) {
    lateinit var client: Client

    init {
        println("ClientHandler init")
        client = Client.create(
            { obj ->
                println("Got client response: $obj")
                // Handle incoming updates and responses
                when (obj!!.constructor) {
                    TdApi.UpdateAuthorizationState.CONSTRUCTOR -> {
                        val authorizationState =
                            (obj as TdApi.UpdateAuthorizationState).authorizationState
                        println("Blolo: $authorizationState")
                        viewModel.setAuthState(authorizationState)

                        when (authorizationState.constructor) {

                            TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                                // Set TDLib parameters
                                client.send(TdApi.SetTdlibParameters(getTdLibParams(context))) {
                                    println("set tdlib parameters: $it")
                                }
                            }
                            TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> {
                                // Check the database encryption key for correctness
                                client.send(TdApi.CheckDatabaseEncryptionKey()) { obj ->
                                    println("check database encryption key: $obj")
                                }
                            }
                            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> {
                                // Show the phone number entry view
                                client.send(
                                    TdApi.RequestQrCodeAuthentication()
                                ) {
                                    println("SetAuthenticationPhoneNumber result: $it")
                                }
                            }
                            TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR -> {
                                val auth =
                                    obj.authorizationState as TdApi.AuthorizationStateWaitOtherDeviceConfirmation
                                // Show the QR code confirmation view
                                println("Show QR code confirmation view ${auth.link}")
                                viewModel.connectionView.updateClientStatus(ClientStatus.WaitingForQrCode(auth.link))

                            }
                            //
                            TdApi.AuthorizationStateReady.CONSTRUCTOR -> {
                                // println("AuthorizationStateReady")
                                // print getme
                                client.send(TdApi.GetMe()) {
                                    println("GetMe result: $it")
                                }
                            }
                        }
                    }

                    TdApi.Error.CONSTRUCTOR -> {
                        // Handle errors
                        val error = obj as TdApi.Error
                        if (error.message == "PHONE_NUMBER_INVALID") {
                            println("Invalid phone number")
                        } else if (error.message == "PHONE_CODE_INVALID") {
                            println("Invalid phone code")
                        } else if (error.message == "PHONE_CODE_EXPIRED") {
                            println("Code expired")
                        } else if (error.message == "PASSWORD_HASH_INVALID") {
                            println("Invalid password")
                        } else {
                            println("Error: ${error.message}")
                        }
                    }
                }
            },
            { println("Got update exception: $it") },
            { println("Got exception: $it") }
        )
    }

}


fun getClient(context: Context, appVm: AppVm): Client {
    return ClientHandler(context, appVm).client
}