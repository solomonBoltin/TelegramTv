package com.solomonboltin.telegramtv.vms

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solomonboltin.telegramtv.configuration.getTdLibParams
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.LoggerFactory

class ChatM(val clientVM: ClientVM, val chat: TdApi.Chat) {
    fun messagesFlow(): Flow<TdApi.Message> {
        println("ScrappingFlows messagesFlow chat: ${chat.title}")
        return flow {
            println("ScrappingFlows messagesFlow flow")

            var lastMessageId = 0L
            while (true) {
                println("ScrappingFlows messagesFlow flow loop")

                getMessages(chat.id, lastMessageId)

                    .collect { message ->
                        println("NewFlows messagesFlow  getMessage collect")
                        emit(message)

                        lastMessageId = message.id
                    }

                println("ScrappingFlows sleeping: $lastMessageId")
                delay(1000)
            }
        }
    }

    fun getMessages(chatId: Long, fromMessageId: Long = 0): Flow<TdApi.Message> =

        callbackFlow {
            println("ScrappingFlows getMessages $chatId $fromMessageId")

            clientVM.client.send(
                TdApi.GetChatHistory(
                    chatId,
                    fromMessageId,
                    0,
                    100,
                    false
                )
            ) { res ->
                if (res is TdApi.Error) {
                    println("ScrappingFlows getMessages error ${res.message}")
                    close()
                }
                println("ScrappingFlows getMessages res ${(res as TdApi.Messages).totalCount } )}")


                (res as TdApi.Messages).messages.forEach {
                    println("ScrappingFlows getMessages forEach ${it.chatId}")

                    trySend(it)
                }
                println("ScrappingFlows getMessages canceling")
                close()
            }
            println("ScrappingFlows getMessages awaitClose")

            awaitClose { }

            println("ScrappingFlows getMessages done")
        }

}

data class Chat(val id: Long, val title: String)


class ClientVM : ViewModel() {
    val log = LoggerFactory.getLogger(ClientVM::class.java)

    lateinit var client: Client
    private val context: Context by inject(Context::class.java)


    private val chatPositionFlow = MutableSharedFlow<Long>()
    private var defaultFileUpdatesHandler: (TdApi.File) -> Unit = {
        Log.i("ClientVm", "No handler for file updates")
    }


    fun sendBlocked(ob: TdApi.Function): TdApi.Object? {
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
        defaultFileUpdatesHandler = handler
    }

    fun requestLoadingChats() {
        sendBlocked(TdApi.LoadChats(null, 800))
    }

    private fun handleUpdates(update: TdApi.Object) {
        when (update) {
            is TdApi.UpdateAuthorizationState -> {
                handleAuthStateUpdate(update.authorizationState)
            }

            is TdApi.UpdateConnectionState -> {
                updateConnectionState(update.state)
            }

            is TdApi.UpdateUser -> {
                handleUserUpdate(update.user)
            }

            is TdApi.UpdateFile -> {
                handleFileUpdates(update.file)
            }

            is TdApi.UpdateChatPosition -> {
                viewModelScope.launch {
//                    println("ChatPositionUpdate $update")

                    chatPositionFlow.emit(update.chatId)
                }
//                println("ChatPositionUpdate $update")

            }

            is TdApi.UpdateChatTitle -> {
                chats[update.chatId] = Chat(update.chatId, update.title)
            }

            is TdApi.UpdateUser -> {
//                Log.i("UpdateClientVMUser", "Unhandled update: $update")

            }

            is TdApi.Messages -> {
//                Log.i("UpdateClientVMUser", "Unhandled update: $update")

            }

            is TdApi.UpdateNewMessage -> {
//                Log.i("UpdateClientVMUser", "Unhandled update: $update")

            }

            else -> {
//                Log.i("UpdateClientVM", "Unhandled update: $update")
            }


        }
    }

    private fun handleFileUpdates(file: TdApi.File) {
        defaultFileUpdatesHandler(file)
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
    private fun setUser(user: TdApi.User) {
        _user.update { user }
    }


    // Chats state
    val chats: MutableMap<Long, Chat> = mutableMapOf()

    fun getChat(chatId: Long): Flow<TdApi.Chat> = callbackFlow {
        client.send(TdApi.GetChat(chatId)) {
            trySend(it as TdApi.Chat)
        }

        awaitClose { }
    }

    fun chatsFlow() = chatPositionFlow.flatMapConcat {
        log.info("ChatFromFlow: getting chat $it")
        getChat(it).take(1).map { chat ->
            ChatM(this, chat)
        }
    }
//        .filter { chat ->
//            chat.type is TdApi.ChatTypeSupergroup && (chat.type as TdApi.ChatTypeSupergroup).isChannel
//        }


    private var _connectionState = MutableStateFlow<TdApi.ConnectionState?>(null)
    val connectionState: StateFlow<TdApi.ConnectionState?> = _connectionState.asStateFlow()

    fun updateConnectionState(connectionStateUpdate: TdApi.ConnectionState) {
        _connectionState.update { connectionStateUpdate }
    }


    val chatsFlow = MutableSharedFlow<Chat>()


    fun updateChat(chat: Chat) {

        viewModelScope.launch {
            chatsFlow.emit(chat)
        }
    }

    fun searchChats(text: String): Flow<Chat> {
        return chatsFlow.filter { chat ->
            chat.title.contains(text)
        }
    }

    // chatsVm ->
    //  getChat -> chat
    //  start() -- loads all chats
    //  channels() -> list<chats>
    //  searchChatByTitle()
    //


    init {
        restartClient()
    }

}


