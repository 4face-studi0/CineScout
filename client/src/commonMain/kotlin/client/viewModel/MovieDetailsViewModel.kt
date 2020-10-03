package client.viewModel

import client.ViewState
import client.ViewStateFlow
import client.viewModel.CineViewModel.Companion.ErrorDelay
import domain.AddMovieToWatchlist
import domain.FindMovie
import domain.GetMovieRating
import domain.IsMovieInWatchlist
import domain.RateMovie
import domain.RemoveMovieFromWatchlist
import entities.Rating
import entities.TmdbId
import entities.movies.Movie
import entities.movies.MovieWithStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import util.DispatchersProvider
import kotlin.time.seconds

class MovieDetailsViewModel(
    override val scope: CoroutineScope,
    dispatchers: DispatchersProvider,
    private val movieId: TmdbId,
    private val findMovie: FindMovie,
    private val getMovieRating: GetMovieRating,
    private val isMovieInWatchlist: IsMovieInWatchlist,
    private val addMovieToWatchlist: AddMovieToWatchlist,
    private val removeMovieFromWatchlist: RemoveMovieFromWatchlist,
    private val rateMovie: RateMovie,
): CineViewModel, DispatchersProvider by dispatchers {

    val result = ViewStateFlow<MovieWithStats, Error>()
    private val movieOrThrow get() = result.data!!.movie

    init {
        scope.launch(Io) {
            val movie = findMovie(movieId)

            if (movie == null) {
                result set Error.MovieNotFound
                return@launch
            }

            var rating = Rating.Neutral
            var isInWatchlist = false
            set(movie, rating, isInWatchlist)

            launch {
                getMovieRating(movie).collect {
                    rating = it
                    set(movie, rating, isInWatchlist)
                }
            }

            launch {
                isMovieInWatchlist(movie).collect {
                    isInWatchlist = it
                    set(movie, rating, isInWatchlist)
                }
            }
        }
    }

    private fun set(movie: Movie, rating: Rating, isInWatchlist: Boolean) {
        result set MovieWithStats(movie, rating, isInWatchlist)
    }

    fun like() {
        emitCatching(Error.CantRate) {
            rateMovie(movieOrThrow, Rating.Positive)
        }
    }

    fun dislike() {
        emitCatching(Error.CantRate) {
            rateMovie(movieOrThrow, Rating.Negative)
        }
    }

    fun addToWatchlist() {
        emitCatching(Error.CantAddToWatchlist) {
            addMovieToWatchlist(movieOrThrow)
        }
    }

    fun removeFromWatchlist() {
        emitCatching(Error.CantRemoveFromWatchlist) {
            removeMovieFromWatchlist(movieOrThrow)
        }
    }

    private inline fun emitCatching(error: Error, crossinline block: suspend () -> Unit) {
        scope.launch(Io) {
            val prevData = result.data
            val r = runCatching { block() }

            if (r.isFailure) {
                result set error

                // If any, restore previous data after a delay
                if (prevData != null) {
                    delay(ErrorDelay)
                    result set prevData
                }
            }
        }
    }

    sealed class Error: ViewState.Error() {
        object MovieNotFound: MovieDetailsViewModel.Error()
        object CantAddToWatchlist: MovieDetailsViewModel.Error()
        object CantRemoveFromWatchlist: MovieDetailsViewModel.Error()
        object CantRate: MovieDetailsViewModel.Error()
    }
}