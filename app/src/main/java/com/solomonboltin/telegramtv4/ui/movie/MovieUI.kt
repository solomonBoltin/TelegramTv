package com.solomonboltin.telegramtv4.ui.movie

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.solomonboltin.telegramtv4.models.Movie
import com.solomonboltin.telegramtv4.vms.FilesVM
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.MessageDocument
import org.drinkless.td.libcore.telegram.TdApi.MessageVideo
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieUI(movie: Movie = Movie("The Matrix", "2000", TdApi.File(), TdApi.Message())) {
    val filesVM = koinViewModel<FilesVM>()
    val file = movie.file
    Log.i("MovieUI", "reloading view")
    val paddingModifier = Modifier.padding(10.dp)
    Card(
        onClick = {
            filesVM.setMovie(movie)
        },
        elevation = 10.dp,
        modifier = paddingModifier
    ) {
        Column(modifier = paddingModifier) {
            Text(text = "size: ${file.size}")
            Text(text = "offset: ${file.local.downloadOffset}")
            val message = movie.message.content
            when(message){
                is MessageVideo -> {
                    Text(text = "duration: ${message.video.duration}")
                    Text(text = "caption: ${message.caption.text}")
                }
                is MessageDocument -> {
                    Text(text = "type: document")
                    Text(text = "caption: ${message.caption.text}")
                }
            }


        }

    }


}