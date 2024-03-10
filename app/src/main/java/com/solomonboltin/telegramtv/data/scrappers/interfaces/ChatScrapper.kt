package com.solomonboltin.telegramtv.data.scrappers.interfaces

import kotlinx.coroutines.flow.Flow
import org.drinkless.td.libcore.telegram.TdApi

interface ChatScrapper {
    val chatId: Long
    fun isValidChat(): Boolean
    fun moviesFlow(): Flow<MovieScrapper>
    fun messagesFlow(): Flow<TdApi.Message>

    fun scrapMovie(message: TdApi.Message): MovieScrapper?
}