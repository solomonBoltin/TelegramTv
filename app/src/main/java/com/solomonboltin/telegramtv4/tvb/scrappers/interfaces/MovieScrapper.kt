package com.solomonboltin.telegramtv4.tvb.scrappers.interfaces

import androidx.compose.ui.graphics.ImageBitmap
import com.google.android.exoplayer2.MediaItem
import com.solomonboltin.telegramtv4.tvb.models.Movie
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
    fun getDefaultFile(): MediaItem?

    val hasFiles get() = getDefaultFile() != null
}


interface ScrappedMovie {
    val info: ScrappedMovieInfo
    val images: ScrapedMovieImages
    val files: ScrapedMovieFiles

    val isValidMovie get() = info.hasInfo && images.hasImages && files.hasFiles

    fun toMovie(): Movie {
        return Movie(
            title = info.title,
            year = info.year,
            description = info.description,
            rating = info.rating,
            tags = info.tags,
            poster1 = images.getPoster1(),
            file = files.getDefaultFile()
        )
    }

}

interface MovieScrapperA {
    val log: Logger

    val movies: List<ScrappedMovie>

    fun scrapMovies(max: Int = 500, onNewMovie: (ScrappedMovie) -> Unit)

}
