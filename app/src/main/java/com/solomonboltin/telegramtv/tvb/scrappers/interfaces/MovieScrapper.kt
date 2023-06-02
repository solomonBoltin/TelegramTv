package com.solomonboltin.telegramtv.tvb.scrappers.interfaces

import androidx.compose.ui.graphics.ImageBitmap
import com.google.android.exoplayer2.MediaItem
import com.solomonboltin.telegramtv.tvb.models.Movie
import com.solomonboltin.telegramtv.tvb.scrappers.telegram.media.TelegramVideoSource
import com.solomonboltin.telegramtv.ui.movie.newMediaItem
import org.drinkless.td.libcore.telegram.TdApi
import org.slf4j.Logger

interface ScrappedMovieInfo {
    val title: String
    val year: String
    val description: String
    val rating: String
    val tags: List<String>

    val hasInfo get() = title.isNotBlank()
}

interface ScrapedMovieImages {
    fun getPoster1(): String?
    fun getPoster2(): ImageBitmap?

    val hasImages get() = getPoster1() != null

}

interface ScrapedMovieFiles {

    fun getDefaultVideo(): TdApi.MessageVideo?
    fun getDefaultMediaItem(): MediaItem? =
        newMediaItem(TelegramVideoSource.UriFactory.create(1, getDefaultVideo()!!.video.video!!))

    val hasFiles get() = getDefaultVideo() != null
}


interface ScrappedMovie {
    val info: ScrappedMovieInfo
    val images: ScrapedMovieImages
    val files: ScrapedMovieFiles

    val isValidMovie get() = info.hasInfo && images.hasImages && files.hasFiles

    fun toMovie(): Movie {
        return Movie(
            scrapedMovie = this,
            title = info.title,
            year = info.year,
            description = info.description,
            rating = info.rating,
            tags = info.tags,
            poster1 = images.getPoster1(),
            file = files.getDefaultMediaItem()
        )
    }

}

interface MovieScrapperA {
    val log: Logger

    val movies: List<ScrappedMovie>

    fun scrapMovies(max: Int = 500, onNewMovie: (ScrappedMovie) -> Unit)

}
