package com.solomonboltin.telegramtv4.ui.movie

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.solomonboltin.telegramtv4.models.Movie
import com.solomonboltin.telegramtv4.struck.DashData
import org.drinkless.td.libcore.telegram.TdApi


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
    Column() {
        Text(text = movie.title, style = MaterialTheme.typography.titleLarge, color = Color.White)
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = movie.year,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Text(
                text = movie.rating,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                movie.tags.forEach { tag ->
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontStyle = FontStyle.Italic
                    )
                }

            }
        }
        Text(
            text = movie.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}


@Composable
fun MovieInList(movie: Movie, width: Int = 60, height: Int = 140, selected: Boolean = false) {
    val default = painterResource(com.solomonboltin.telegramtv4.R.drawable.movie4)
    val image = if (movie.poster1 != null) BitmapPainter(movie.poster1) else default
    val selectedModifier = Modifier.border(BorderStroke(3.dp, Color.White))
    Box(
        modifier = if (selected) selectedModifier else Modifier,
    ) {
        Image(
            image, "",
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(150.dp)


        )
    }


}

@Composable
fun MoviesListUI(movieList: DashData.MovieList, selected: Boolean = false) {

    //white bold text for movie list title
    Text(text = movieList.title, color = Color.White, style = MaterialTheme.typography.titleMedium)

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


//@Preview
//@Composable
//fun previewMovieDashUI() {
//    val defaultMovieDash = MovieDash(
//        movieLists = listOf(
//            MovieList(
//                "Action movies", listOf(
//                    Movie(
//                        "The Gangster",
//                        "2006",
//                        TdApi.File(),
//                        TdApi.Message(),
//                        "Movie about a gangster in the late 90s of United States war",
//                    ),
//                    Movie(
//                        "The mechanic",
//                        "2003",
//                        TdApi.File(),
//                        TdApi.Message(),
//                        "Movie about a gangster in the late 90s of United States war"
//                    )
//                )
//            )
//        )
//    )
//    MovieDashUI(movieDash = defaultMovieDash)
//}

//@Composable
//fun MovieDashUI(movieDash: MovieDash) {
//
//    val selectedList = movieDash.movieLists.getOrNull(movieDash.selectedList)
//    val selectedMovie = selectedList?.movies?.getOrNull(selectedList.selectedMovie)
//
//    Column() {
//        if (selectedMovie != null) MovieInfoBar(movie = selectedMovie)
//        movieDash.movieLists.forEachIndexed { index, movieList ->
//            MoviesListUI(movieList = movieList, selected = (index == movieDash.selectedList))
//        }
//    }
//}