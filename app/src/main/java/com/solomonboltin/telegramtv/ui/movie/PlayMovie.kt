package com.solomonboltin.telegramtv.ui.movie

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.metadata.Metadata
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util
import com.solomonboltin.telegramtv.BackPressHandler
import com.solomonboltin.telegramtv.data.models.MovieDa
import com.solomonboltin.telegramtv.media.TelegramVideoSource
import com.solomonboltin.telegramtv.vms.FilesVM
import kotlinx.coroutines.delay
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


//
//    filesVM.seekFileOffset(movie.file.id)
//
//    file = filesVM.getFile(movie.file.id, condition = {
//        it.local.path.isNullOrBlank()
//    })
    // Declaring ExoPlayer
    // Fetching the Local Context


    val mContext = LocalContext.current


    val mExoPlayer = remember(mContext) {
        ExoPlayer.Builder(mContext).build().apply {
            addAnalyticsListener(EventLogger(null))
            addListener(object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    println("EGL_emulation: onMediaItemTransition. mediaItem = ${mediaItem!!.mediaMetadata.toBundle()}, reason = $reason")
                }

                override fun onMetadata(metadata: Metadata) {
                    super.onMetadata(metadata)
                    println("EGL_emulation: onMetadata = $metadata")
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    super.onMediaMetadataChanged(mediaMetadata)
                    Log.i("EGL_emulation", "onMediaMetadataChanged: mediaMetadata = $mediaMetadata")
                }
            })

            playWhenReady = true
            prepare()

        }
    }

    val mediaSourceFactory = ProgressiveMediaSource.Factory(TelegramVideoSource.Factory(filesVM))
    val mediaSource = mediaSourceFactory.createMediaSource(movieDa.file!!)


    BackPressHandler(onBackPressed = {
        mExoPlayer.release()
        filesVM.stopPlayingMovie()
    }
    )

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Implementing ExoPlayer
        AndroidView(factory = { context ->
            PlayerView(context).apply {
                player = mExoPlayer
            }
        })
    }


    // preload media file
    val fileId = movieDa.scrapedMovie.files.getDefaultVideo()!!.video.video.id
    filesVM.seekFileOffset(fileId)
    println("TGBF Seeking file $fileId")

    LaunchedEffect(key1 = Unit) {
        delay(10000)
        mExoPlayer.setMediaSource(mediaSource)
    }
    // set media file


}

fun newMediaItem(uri: Uri?): MediaItem? {
    return MediaItem.Builder().setUri(uri).build()
}