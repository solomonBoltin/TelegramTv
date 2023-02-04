package com.solomonboltin.telegramtv4.models

// movie data model with title, year, file, message: TdApi.Message

import org.drinkless.td.libcore.telegram.TdApi

data class Movie(
    val title: String,
    val year: String,
    val file: TdApi.File,
    val message: TdApi.Message
)