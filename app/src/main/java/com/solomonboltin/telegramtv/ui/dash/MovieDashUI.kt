@file:OptIn(ExperimentalTvMaterial3Api::class)

package com.solomonboltin.telegramtv.ui.dash

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyListState
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.rememberTvLazyListState
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.google.relay.compose.BoxScopeInstanceImpl.align
import com.solomonboltin.telegramtv.R
import com.solomonboltin.telegramtv.data.models.MovieDa
import com.solomonboltin.telegramtv.vms.MovieDashVM
import com.solomonboltin.telegramtv.vms.PlayerVM
import org.koin.androidx.compose.koinViewModel
import org.slf4j.LoggerFactory


private val log = LoggerFactory.getLogger(MovieDashVM::class.java)


@Preview(widthDp = 120, heightDp = 15)
@Composable
fun MovieControlBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        MovieActionBarIcon(Icons.Outlined.PlayArrow)
        // space between
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .width(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            MovieActionBarIcon(Icons.Outlined.Add)
            MovieActionBarIcon(Icons.Outlined.ThumbUp)
            MovieActionBarIcon(Icons.Outlined.ThumbDown)
        }

    }
}


@Composable
fun MovieActionBarIcon(icon: ImageVector) {
    OutlinedButton(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxHeight(),  //avoid the oval shape
//        shape = CircleShape,
//        border = BorderStroke(1.dp, Color.White),
        contentPadding = PaddingValues(2.dp),  //avoid the little icon
//        colors = ButtonDefaults.ContentPadding
    ) {
        Icon(icon, contentDescription = "content description", tint = Color.White)
    }
}


@Composable
fun MovieInfoBar(movieDa: MovieDa) {
    Column(
        modifier = Modifier
            .width(450.dp)
            .height(200.dp)
            .padding(10.dp)

    ) {
        androidx.compose.material.Text(
            text = movieDa.title, style = MaterialTheme.typography.titleLarge, color = Color.White
        )
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            androidx.compose.material.Text(
                text = movieDa.year,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            androidx.compose.material.Text(
                text = movieDa.rating,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                movieDa.tags.forEach { tag ->
                    androidx.compose.material.Text(
                        text = tag,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontStyle = FontStyle.Italic
                    )
                }

            }
        }
        androidx.compose.material.Text(
            text = movieDa.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}


@Composable
fun MovieCardUI(movieDa: MovieDa, modifier: Modifier = Modifier) {
    // dashVm view model
    val dashVM = koinViewModel<MovieDashVM>()
    val playerVM = koinViewModel<PlayerVM>()

    val interactionSource = MutableInteractionSource()
    val isFocused by interactionSource.collectIsFocusedAsState()

    if (isFocused) {
        log.info("NewUI focused movie: ${movieDa.title}")
        dashVM.setFocusedMovie(movieDa)
    }
    val focusRequester = remember { FocusRequester() }


    Card(
        onClick = {
            log.info("NewUI Clicked movie: ${movieDa.title}")
            playerVM.setMovie(movieDa)
        },
        interactionSource = interactionSource,
        modifier = modifier
            .focusRequester(focusRequester)
    ) {
        val width: Int = 100
        val height: Int = 120
        AsyncImage(
            model = movieDa.poster1,
            contentDescription = "content description",
            modifier = Modifier
                .width(width.dp)
                .height(height.dp),
            contentScale = ContentScale.Crop,
        )
    }

    LaunchedEffect (Unit) {
        if (dashVM.focusedMovie.value == null) {
            log.info("NewUI no focused movie: when displaying ${movieDa.title} requesting focus")
            focusRequester.requestFocus()
        }
    }
//    "", contentScale = ContentScale.Fit, modifier = Modifier.height(150.dp)


}

@Composable
fun MoviesListUI(movieListId: Long, modifier: Modifier = Modifier) {

    // dashVm view model
    val dashVM = koinViewModel<MovieDashVM>()
    log.info("NewFlows getting movieList: $movieListId")

    val finalMovieList by dashVM.getMovieListFlow(movieListId)?.collectAsState() ?: return
    log.info("NewFlows rendering: ${finalMovieList.movieDas.map { it.title }}")


    //
    Text(
        text = finalMovieList.chatId.toString(),
        color = Color.White,
        style = MaterialTheme.typography.titleMedium
    )
    // interactionSource = interactionSource,
    val interactionSource = MutableInteractionSource()
    val isFocused by interactionSource.collectIsFocusedAsState()
    if (isFocused) {
        log.info("NewUI focused movieList: ${finalMovieList.chatId}")

    }

    TvLazyRow(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = modifier
    ) {
        items(finalMovieList.movieDas.size) { index ->
            MovieCardUI(movieDa = finalMovieList.movieDas[index])
        }
    }
}


@Composable
fun FocusedMovieImage() {
    val dashVM = koinViewModel<MovieDashVM>()
    val focusedMovie by dashVM.focusedMovie.collectAsState()

    log.info("NewUI displaying focused movie bar: ${focusedMovie?.title}")

    if (focusedMovie != null) {
        AsyncImage(
            model = focusedMovie!!.poster1,
            contentDescription = "content description",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.Center)
                .graphicsLayer { alpha = 0.99F }
                .drawWithContent {
                    val colors = listOf(Color.Transparent, Color.Black)
                    drawContent()
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = colors,
                            startX = size.width, // Reverse the gradient phase
                            endX = 0f
                        ),
                        blendMode = BlendMode.DstIn
                    )
                },
            contentScale = ContentScale.Crop,
        )
    }
    // todo image will fade gradually to the right and to bottom
}

@Composable
fun FocusedMovieInfo() {
    val dashVM = koinViewModel<MovieDashVM>()
    val focusedMovie by dashVM.focusedMovie.collectAsState()

    if (focusedMovie != null) {
        MovieInfoBar(movieDa = focusedMovie!!)
    }
}

@Composable
fun MovieDashUI() {
    val dashVM = koinViewModel<MovieDashVM>()
    val movieLists by dashVM.movieListsState.collectAsState()

    log.info("NewUi updated movieLists: $movieLists")

    Box {
        FocusedMovieImage()
        Column {
            FocusedMovieInfo()
            TvLazyColumn(
                state = rememberTvLazyListState(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                items(movieLists.size) { index ->
                    MoviesListUI(movieListId = movieLists[index])
                }
            }
        }
    }


}

@Composable
fun MovieDashLiveUI() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

//    val mainFocusRequester = remember { FocusRequester() }

        Box(
            Modifier
                .background(Color.Black.copy(alpha = 0.8f))
                .fillMaxSize()
//                .focusRequester(mainFocusRequester)
//                .onFocusEvent { fEvent ->
//                    log.info("NewUI MovieDashLiveUI onFocusEvent: $fEvent")
//                }
        ) {
            MovieDashUI()
        }
    }

    log.info("NewUI rendering MovieDashLiveUI")
    LaunchedEffect(Unit) {
        log.info("NewUI requesting focus")
//        mainFocusRequester.requestFocus()
    }
}


//@Composable
//fun SelectedMovieBackgroundImage(dashData: DashData) {
//    val image = dashData.selectedMovie?.toMovieDa()?.poster1
//    // image size should be half of the screen size
//    val imageSize =
//    if (image != null) {
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            drawIntoCanvas { canvas ->
//
//                val gradientWidth: Dp = 100.dp // Width of the fading gradient
//                val gradientColor = Color.Transparent // Transparent color for the gradient
//
//                // Calculate the coordinates of the gradient rectangle
//                val gradientBounds = Rect(
//                    size.width / 2 - imageSize.first / 2,
//                    size.height / 2 - imageSize.second / 2,
//                    size.width / 2 + imageSize.first / 2,
//                    size.height / 2 + imageSize.second / 2
//                )
//
//                // Draw the image
//                drawImage(
//                    // Replace with your image drawable or resource
//                    // Example: ImageBitmap.imageResource(R.drawable.your_image)
//                )
//
//                // Apply the fading gradient on the edges
//                withTransform({
//                    clipRect(gradientBounds)
//                }) {
//                    val gradient = Brush.verticalGradient(
//                        colors = listOf(
//                            gradientColor,
//                            Color.Transparent,
//                            Color.Transparent,
//                            gradientColor
//                        ),
//                        startY = gradientBounds.top.toFloat(),
//                        endY = gradientBounds.bottom.toFloat()
//                    )
//                    drawRect(gradient, alpha = 1f)
//                }
//
//}}}}

