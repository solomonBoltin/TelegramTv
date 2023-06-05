package com.solomonboltin.telegramtv.ui.dash

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
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
import coil.compose.AsyncImage
import com.google.relay.compose.BoxScopeInstanceImpl.align
import com.solomonboltin.telegramtv.R
import com.solomonboltin.telegramtv.data.models.MovieDa
import com.solomonboltin.telegramtv.ui.dash.data.DashData
import com.solomonboltin.telegramtv.vms.MovieDashVM
import org.koin.androidx.compose.koinViewModel


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
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.White),
        contentPadding = PaddingValues(2.dp),  //avoid the little icon
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),

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
                text = movieDa.year, style = MaterialTheme.typography.labelMedium, color = Color.White
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
fun MovieInList(movieDa: MovieDa, width: Int = 100, height: Int = 120, selected: Boolean = false) {
    val default = painterResource(R.drawable.movie4)
//    val image = if (movie.poster1 != null) BitmapPainter(movie.poster1) else default
    val selectedModifier = Modifier.border(BorderStroke(3.dp, Color.White))
    Box(
        modifier = if (selected) selectedModifier else Modifier,
    ) {
        // load image using coil compose
        AsyncImage(
            model = movieDa.poster1,
            contentDescription = "content description",
            modifier = Modifier
                .width(width.dp)
                .height(height.dp),
            contentScale = ContentScale.Crop,
        )

    }
//    "", contentScale = ContentScale.Fit, modifier = Modifier.height(150.dp)


}

@Composable
fun MoviesListUI(movieList: DashData.MovieList, selected: Boolean = false) {

    //white bold text for movie list title
    androidx.compose.material.Text(
        text = movieList.title, color = Color.White, style = MaterialTheme.typography.titleMedium
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.Black))
            .scrollable(state = ScrollableState { 0f }, orientation = Orientation.Vertical)
    ) {
        movieList.movies.forEachIndexed { index, movie ->
            MovieInList(movieDa = movie.toMovieDa(), selected = (index == 0 && selected))
        }
    }
}

@Composable
fun MovieDashUI(dashData: DashData) {
    // dead ui for viewing dash data

    val selectedList = dashData.movieLists.getOrNull(0)
    val selectedMovie = selectedList?.movies?.getOrNull(0)

    if (selectedMovie != null) {
        // image width half of screen
        // image will fade gradually to the right
        AsyncImage(
            model = dashData.selectedMovie?.toMovieDa()?.poster1,
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
                }
            ,
            contentScale = ContentScale.Crop,

        )
    }
    Column {
        if (selectedMovie != null) {
            MovieInfoBar(movieDa = selectedMovie.toMovieDa())
        }

        dashData.movieLists.forEachIndexed { index, movieList ->
            MoviesListUI(movieList = movieList, selected = (index == 0))
        }
    }
}

@Composable
fun MovieDashLiveUI() {
    val movieDashVM = koinViewModel<MovieDashVM>()
    val dashData by movieDashVM.dashData.collectAsState()


    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Box(
            Modifier
                .background(Color.Black.copy(alpha = 0.8f))
                .fillMaxSize()
                .onKeyEvent {
                    println(it)
                    true
                }
        ) {
            MovieDashUI(dashData = dashData)
        }
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

