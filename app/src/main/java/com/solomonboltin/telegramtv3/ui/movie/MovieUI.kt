package com.solomonboltin.telegramtv3.ui.movie

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.solomonboltin.telegramtv3.models.Movie
import com.solomonboltin.telegramtv3.vms.FilesVM
import com.solomonboltin.telegramtv3.vms.MovieMachineVM
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.androidx.compose.koinViewModel
import java.io.RandomAccessFile

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieUI(movie: Movie = Movie("The Matrix", "2000", TdApi.File(), TdApi.Message())) {
    val filesVM = koinViewModel<FilesVM>()
    val file by filesVM.fileFlow(movie.file.id).collectAsState(initial = movie.file)

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
        }

    }


}