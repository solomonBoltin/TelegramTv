//
//
//
//enum class PlayingState {
//    STARTING,
//    BUFFERING,
//    PLAYING,
//    PAUSED,
//    STOPPED
//}
//
//class PlayerVM () : ViewModel()
//{
//    private val _playingMovie = MutableStateFlow<Movie?>(null)
//    val playingMovie = _playingMovie.asStateFlow()
//
//
//    val playingMovie: Movie?
//    val playingState: PlayingState
//
//
//    fun bufferMovie(movie: Movie, bufferSizeMB: Int=20)
//    fun playMovie(movie: Movie)
//    fun pauseMovie()
//    fun exit()
//}