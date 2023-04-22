package com.solomonboltin.telegramtv4.tvb.models

// movie data model with title, year, file, message: TdApi.Message

import androidx.compose.ui.graphics.ImageBitmap
import com.google.android.exoplayer2.MediaItem
import org.drinkless.td.libcore.telegram.TdApi

data class Movie(
    val title: String,
    val year: String,
    val file: MediaItem? = null,
    val message: TdApi.Message = TdApi.Message(),
    val description: String = "",
    val rating: String  = "",
    val poster1: String? = null,
    val tags: List<String> = emptyList(),
)



