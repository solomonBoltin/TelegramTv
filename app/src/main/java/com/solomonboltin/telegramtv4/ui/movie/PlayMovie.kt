package com.solomonboltin.telegramtv4.ui.movie

import android.net.Uri
import android.util.Log
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
import com.solomonboltin.telegramtv4.BackPressHandler
import com.solomonboltin.telegramtv4.models.Movie
import com.solomonboltin.telegramtv4.telegram.TDataSource
import com.solomonboltin.telegramtv4.vms.FilesVM
import org.drinkless.td.libcore.telegram.TdApi.File
import org.koin.androidx.compose.koinViewModel


@Composable
fun PlayMovieUI(movie: Movie) {
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
            val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName))
            val source = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(Uri.parse(video)))
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
fun MyContent(movie: Movie){
    val filesVM = koinViewModel<FilesVM>()
    var file: File? = null
    var path = ""

    filesVM.seekFileOffset(movie.file.id)

    file = filesVM.getFile(movie.file.id, condition = {
        it.local.path.isNullOrBlank()
    })
    Log.d("MyContent", "file: $file")
    // Declaring ExoPlayer
    // Fetching the Local Context
    val mContext = LocalContext.current

    val mExoPlayer = remember(mContext) {
        ExoPlayer.Builder(mContext).build().apply {
            addAnalyticsListener(EventLogger(null))
            addListener(object : Player.Listener{
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

//                val dataSourceFactory = DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, mContext.packageName))
//                source = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(ur))
//                val media = MediaItem.fromUri(Uri.parse(ur))
//                setMediaSource(MediaSource.Factory(TDataSource.Factory(filesVM)).createMediaSource(
//                    newMediaItem(TDataSource.UriFactory.create(1, movie.file))!!))
//                setMediaItem(media)

//                val media = newMediaItem(TDataSource.UriFactory.create(1, movie.file))!!
//                val source = ProgressiveMediaSource.Factory(TDataSource.Factory(filesVM))
//
//                val mediaItem = source.createMediaSource(
//                        newMediaItem(TDataSource.UriFactory.create(1, movie.file.id))!!
//                    ).mediaItem
            setMediaSource(ProgressiveMediaSource.Factory(TDataSource.Factory(filesVM)).createMediaSource(
                newMediaItem(TDataSource.UriFactory.create(1, movie.file))!!))
            playWhenReady = true
            prepare()
        }
    }


    BackPressHandler(onBackPressed = {

        mExoPlayer.release()
        filesVM.stopPlayingMovie() }
    )

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {


        // Declaring a string value
        // that stores raw video url
        val mVideoUrl = "https://cdn.videvo.net/videvo_files/video/free/2020-05/large_watermarked/3d_ocean_1590675653_preview.mp4"
        val ur = "https://media.geeksforgeeks.org/wp-content/uploads/20220314114159/fv.mp4"
//        filesVM.seekFileOffset(movie.file.id)
        println("EGL_emulation")
        println(movie.file.local.path)
//        val file = RandomAccessFile(path, "r")
//        println("EGL_emulation: file = $file")
//        println("EGL_emulation: file.length() = ${file.length()}")
//        println("EGL_emulation: file.fd = ${file.fd}")


        // Implementing ExoPlayer
        AndroidView(factory = { context ->
            PlayerView(context).apply {
                player = mExoPlayer
            }
        })
    }
}

fun newMediaItem(uri: Uri?): MediaItem? {
    return MediaItem.Builder().setUri(uri).build()
}