package com.solomonboltin.telegramtv4.models

// movie data model with title, year, file, message: TdApi.Message

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import com.google.android.exoplayer2.MediaItem
import com.solomonboltin.telegramtv4.struck.DashData
import org.drinkless.td.libcore.telegram.TdApi

data class Movie(
    val title: String,
    val year: String,
    val file: MediaItem? = null,
    val message: TdApi.Message = TdApi.Message(),
    val description: String = "",
    val rating: String  = "",
    val poster1: ImageBitmap? = null,
    val tags: List<String> = emptyList(),
)



