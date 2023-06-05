package com.solomonboltin.telegramtv.data.models

// movie data model with title, year, file, message: TdApi.Message

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import com.google.android.exoplayer2.MediaItem
import com.solomonboltin.telegramtv.data.scrappers.interfaces.MovieScrapper
import org.drinkless.td.libcore.telegram.TdApi


data class MovieDa(
    var id: String,
    val scrapedMovie: MovieScrapper,
    val title: String,
    val year: String,

    val file: MediaItem? = null,

    val message: TdApi.Message = TdApi.Message(),
    val description: String = "",
    val rating: String  = "",
    val poster1: String? = null,

    val tags: List<String> = emptyList(),
){
    val fileId: Int
        get() = scrapedMovie.files.getDefaultVideo()?.video?.video?.id ?: 0
}


@Entity
data class Movie(
    @PrimaryKey val id: String, val title: String, val fileId: Int){

}



@Dao
interface MovieDao {

    @get:Query("SELECT * FROM movie")
    val allMovieDas: List<Movie>

    @Insert
    fun insert(movie: Movie)
}


@Database(entities = [Movie::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}




