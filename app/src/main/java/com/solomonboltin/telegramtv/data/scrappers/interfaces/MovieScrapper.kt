package com.solomonboltin.telegramtv.data.scrappers.interfaces

import androidx.compose.ui.graphics.ImageBitmap
import com.google.android.exoplayer2.MediaItem
import com.solomonboltin.telegramtv.data.models.Movie
import com.solomonboltin.telegramtv.data.models.MovieDa
import com.solomonboltin.telegramtv.media.TelegramVideoSource
import com.solomonboltin.telegramtv.ui.movie.newMediaItem
import org.drinkless.td.libcore.telegram.TdApi
import java.util.UUID

interface MovieInfoScrapper {
    val title: String?
    val year: String?
    val description: String?
    val rating: String?
    val tags: List<String>

    val hasInfo get() = title != null
}

interface MovieImagesScrapper {
    fun getPoster1(): String?
    fun getPoster2(): ImageBitmap?

    val hasImages get() = getPoster1() != null

}

interface MovieFilesScrapper {

    fun getDefaultVideo(): TdApi.MessageVideo?
    fun getDefaultMediaItem(): MediaItem? =
        newMediaItem(TelegramVideoSource.UriFactory.create(1, getDefaultVideo()!!.video.video!!))

    val hasFiles get() = getDefaultVideo() != null
}


interface MovieScrapper {
    val info: MovieInfoScrapper
    val images: MovieImagesScrapper
    val files: MovieFilesScrapper
    val chatId: Long

    val isValidMovie get() = info.hasInfo && images.hasImages && files.hasFiles

    fun toMovieDa(): MovieDa {
        return MovieDa(
            id = UUID.randomUUID().toString(),
            scrapedMovie = this,
            title = info.title ?: "",
            year = info.year ?: "",
            description = info.description ?: "",
            rating = info.rating ?: "",
            tags = info.tags,
            poster1 = images.getPoster1(),
            file = files.getDefaultMediaItem()
        )
    }

    fun toMovie(): Movie {
        return Movie(
            id = UUID.randomUUID().toString(),
            title = info.title ?: "",
            fileId = files.getDefaultVideo()?.video?.video?.id ?: 0
        )
    }

}
