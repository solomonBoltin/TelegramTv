package com.solomonboltin.telegramtv.ui.movie

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.tv.material3.Text
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.solomonboltin.telegramtv.BackPressHandler
import com.solomonboltin.telegramtv.data.models.MovieDa
import com.solomonboltin.telegramtv.media.TelegramVideoSource
import com.solomonboltin.telegramtv.vms.FilesVM
import com.solomonboltin.telegramtv.vms.PlayerVM
import org.koin.androidx.compose.getKoin
import org.koin.androidx.compose.koinViewModel


@Composable
fun PlayMovieUI(movieDa: MovieDa) {
    val filesVM = koinViewModel<FilesVM>()
    val context = LocalContext.current
//
//    filesVM.seekFileOffset(movie.file.id)
//    val exoPlayer = remember {
//        ExoPlayer.Builder(context)
//            .build()
//            .apply {
//                val source =  ProgressiveMediaSource.Factory(Ur)
//                    .createMediaSource(
//                        MediaItem.fromUri(movie.file.local.path)
//                    )
//
//                val source1 = ProgressiveMediaSource.Factory()
//                setMediaSource(source)
//                prepare()
//            }
//    }
    val video = "https://media.geeksforgeeks.org/wp-content/uploads/20220314114159/fv.mp4"

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            val dataSourceFactory =
                DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName))
            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(video)))
//            prepare(source)
            setMediaSource(source)
            prepare()
        }
    }

//    exoPlayer.playWhenReady = true
//    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
//    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

    // Implementing ExoPlayer
    AndroidView(factory = { context ->
        PlayerView(context).apply {
            player = exoPlayer
        }
    })
//    DisposableEffect(
//        AndroidView(factory = {
//            PlayerView(context).apply {
//                useController = false
//                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
//
//                player = exoPlayer
//                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
//            }
//        })
//    ) {
//        onDispose { exoPlayer.release() }
//    }


}

@Composable
fun MyContent(movieDa: MovieDa) {
    val filesVM = koinViewModel<FilesVM>()
    val playerVM = koinViewModel<PlayerVM>()

    val playingState by playerVM.playingState.collectAsState()
    val playingFileState by playerVM.playingFile.collectAsState()

//
//    filesVM.seekFileOffset(movie.file.id)
//
//    file = filesVM.getFile(movie.file.id, condition = {
//        it.local.path.isNullOrBlank()
//    })
    // Declaring ExoPlayer
    // Fetching the Local Context

    val exoPlayer: ExoPlayer = getKoin().get()
    val mContext = LocalContext.current


    val mediaSourceFactory = ProgressiveMediaSource.Factory(TelegramVideoSource.Factory(filesVM))
    val mediaSource = mediaSourceFactory.createMediaSource(movieDa.file!!)


    BackPressHandler(onBackPressed = {
        playerVM.unsetMovie()
    })


    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Implementing ExoPlayer
        Text("PlayingState: $playingState, PlayingFileState: ${playingFileState?.local?.downloadedSize} / ${playingFileState?.size}")

        AndroidView(factory = { context ->
            StyledPlayerView(context)
                .apply { player = exoPlayer }
        })
    }


    // preload media file
//    val fileId = movieDa.scrapedMovie.files.getDefaultVideo()!!.video.video.id
//    filesVM.seekFileOffset(fileId)
//    println("TGBF Seeking file $fileId")
//
//    LaunchedEffect(key1 = Unit) {
//        delay(10000)
//        exoPlayer.setMediaSource(mediaSource)
//    }
//    // set media file


}

fun newMediaItem(uri: Uri?): MediaItem? {
    return MediaItem.Builder().setUri(uri).build()
}