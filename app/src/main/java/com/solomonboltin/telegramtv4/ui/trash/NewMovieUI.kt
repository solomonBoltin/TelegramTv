package com.solomonboltin.telegramtv4.ui.trash


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