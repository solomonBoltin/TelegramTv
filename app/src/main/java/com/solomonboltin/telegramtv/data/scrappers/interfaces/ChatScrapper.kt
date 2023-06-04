package com.solomonboltin.telegramtv.data.scrappers.interfaces

import kotlinx.coroutines.flow.Flow

interface ChatScrapper {
    fun isValidChat(): Boolean
    fun moviesFlow(): Flow<MovieScrapper>
}