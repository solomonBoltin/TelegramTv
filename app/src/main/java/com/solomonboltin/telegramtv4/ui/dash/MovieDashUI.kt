package com.solomonboltin.telegramtv4.ui.dash

import android.media.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.solomonboltin.telegramtv4.R
import com.solomonboltin.telegramtv4.tvb.models.Movie
import com.solomonboltin.telegramtv4.ui.dash.data.DashData
import com.solomonboltin.telegramtv4.vms.MovieDashVM
import org.koin.androidx.compose.koinViewModel
import java.io.File
import kotlin.math.log


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
fun MovieInfoBar(movie: Movie) {
    Column(
        modifier = Modifier
            .width(450.dp)
            .height(200.dp)
            .padding(10.dp)

    ) {
        androidx.compose.material.Text(
            text = movie.title, style = MaterialTheme.typography.titleLarge, color = Color.White
        )
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            androidx.compose.material.Text(
                text = movie.year, style = MaterialTheme.typography.labelMedium, color = Color.White
            )
            androidx.compose.material.Text(
                text = movie.rating,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                movie.tags.forEach { tag ->
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
            text = movie.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}


@Composable
fun MovieInList(movie: Movie, width: Int = 60, height: Int = 140, selected: Boolean = false) {
    val default = painterResource(R.drawable.movie4)
//    val image = if (movie.poster1 != null) BitmapPainter(movie.poster1) else default
    val selectedModifier = Modifier.border(BorderStroke(3.dp, Color.White))
    Box(
        modifier = if (selected) selectedModifier else Modifier,
    ) {
        // load image using coil compose
        AsyncImage(
            model = movie.poster1,
            contentDescription = "content description",
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
            MovieInList(movie = movie.toMovie(), selected = (index == 0 && selected))
        }
    }
}


@Composable
fun MovieDashUI(dashData: DashData) {
    // dead ui for viewing dash data

    val selectedList = dashData.movieLists.getOrNull(0)
    val selectedMovie = selectedList?.movies?.getOrNull(0)

    Column() {
        if (selectedMovie != null) MovieInfoBar(movie = selectedMovie.toMovie())
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

