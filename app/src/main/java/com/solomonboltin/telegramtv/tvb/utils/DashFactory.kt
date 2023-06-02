//
//
//import org.drinkless.td.libcore.telegram.TdApi
//
//class DashFactory {
//
//    fun randomMovie() : ScrappedMovie {
////        val movies = listOf(
////            Movie("Hasandak", "1972", description = "Movie about the sandak"),
////            Movie("The Dark Knight", "2008", description = "Movie about the dark knight"),
////            Movie("The Godfather", "1972", description = "Movie about the godfather"),
////            Movie("The Godfather: Part II", "1974", description = "Movie about the godfather part 2"),
////            Movie("The Shawshank Redemption", "1994", description = "Movie about the shawshank redemption"),
////            Movie("Pulp Fiction", "1994", description = "Movie about the pulp fiction"),
////            Movie("The Lord of the Rings: The Return of the King", "2003", description = "Movie about the lord of the rings"),
////            Movie("The Good, the Bad and the Ugly", "1966", description = "Movie about the good, the bad and the ugly"),
////            Movie("The Dark Knight Rises", "2012", description = "Movie about the dark knight rises")
////        )
//
//        val movies = listOf<ScrappedMovie>{
//            object : ScrappedMovie {
//                override val info: ScrappedMovieInfo
//                    get() = TODO("Not yet implemented")
//                override val images: ScrapedMovieImages
//                    get() = TODO("Not yet implemented")
//                override val files: ScrapedMovieFiles
//                    get() = TODO("Not yet implemented")
//            }
//        }
//        return movies.map { it.toMovie() }.random()
//    }
//
//    fun movieList() : DashData.MovieList {
//        val categories = listOf(
//            "Action",
//            "Adventure",
//            "Animation",
//            "Biography",
//            "Comedy",
//            "Crime",
//            "Documentary",
//            "Drama",
//            "Family",
//            "Fantasy",
//            "Film-Noir",
//            "History",
//            "Horror",
//            "Music",
//            "Musical",
//            "Mystery",
//            "Romance",
//            "Sci-Fi",
//            "Sport",
//            "Thriller",
//            "War",
//            "Western"
//        )
//        val movies = (0..10).map {
//            randomMovie()
//        }
//        return DashData.MovieList(categories.random(), movies)
//    }
//
//    fun generateMoviesDash() : MovieDash {
//        val movieLists = (0..10).map {
//            movieList()
//        }
//        return MovieDash(movieLists)
//    }
//
//
//
//
//}