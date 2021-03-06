package entities.stats

import entities.Either
import entities.ResourceError
import entities.model.Actor
import entities.model.FiveYearRange
import entities.model.Genre
import entities.model.UserRating
import entities.model.UserRating.Negative
import entities.model.UserRating.Positive
import entities.movies.Movie
import kotlinx.coroutines.flow.Flow

interface StatRepository {

    // Get
    suspend fun topActors(limit: Int): Collection<Actor>
    suspend fun topGenres(limit: Int): Collection<Genre>
    suspend fun topYears(limit: Int): Collection<FiveYearRange>
    suspend fun ratedMovies(): Collection<Pair<Movie, UserRating>>
    fun rating(movie: Movie): Flow<UserRating>
    fun watchlist(): Flow<Either<ResourceError, Collection<Movie>>>
    fun isInWatchlist(movie: Movie): Flow<Boolean>
    fun suggestions(): Flow<Either<ResourceError, Collection<Movie>>>

    // Insert
    suspend fun rate(movie: Movie, rating: UserRating)
    suspend fun addToWatchlist(movie: Movie)
    suspend fun removeFromWatchlist(movie: Movie)
    suspend fun addSuggestions(movies: Collection<Movie>)
    suspend fun removeSuggestion(movie: Movie)

    companion object {
        const val STORED_SUGGESTIONS_LIMIT = 200
    }
}

val Collection<Pair<Movie, UserRating>>.movies get() =
    map { it.first }

fun <T> Collection<Pair<T, UserRating>>.positives() = filter { it.second == Positive }.map { it.first }
fun <T> Collection<Pair<T, UserRating>>.negatives() = filter { it.second == Negative }.map { it.first }
