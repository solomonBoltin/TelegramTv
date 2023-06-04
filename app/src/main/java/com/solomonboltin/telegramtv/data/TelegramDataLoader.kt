package com.solomonboltin.telegramtv.data

import com.solomonboltin.telegramtv.data.scrappers.amsalem.AmsalemChatScrapper
import com.solomonboltin.telegramtv.data.scrappers.interfaces.ChatScrapper
import com.solomonboltin.telegramtv.data.scrappers.default.DefaultChatScrapper
import com.solomonboltin.telegramtv.vms.ChatM
import com.solomonboltin.telegramtv.vms.ClientVM
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

open class TelegramDataLoader(private val clientVM: ClientVM) {

    fun chatScrappersFlow() = clientVM.chatsFlow()
        .map { chat -> getChatScrapper(chat) }
        .map { chatScrapper ->
            if (chatScrapper.isValidChat()) chatScrapper
            else null
        }
        .filterNotNull()


    private fun getChatScrapper(chat: ChatM): ChatScrapper {
        val amsalemChatScrapper = AmsalemChatScrapper(clientVM, chat)
        if (amsalemChatScrapper.isValidChat()) {
            return amsalemChatScrapper
        }


        return DefaultChatScrapper(clientVM, chat)
    }
}
