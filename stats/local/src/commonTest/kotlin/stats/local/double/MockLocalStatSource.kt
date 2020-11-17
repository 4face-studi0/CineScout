package stats.local.double

import database.movies.Movie
import database.stats.StatType
import entities.IntId
import entities.TmdbId
import entities.model.Name
import entities.model.Video
import io.mockk.spyk
import stats.LocalStatSource
import stats.local.LocalStatSourceImpl

fun mockLocalStatSource(): LocalStatSource {
    val actors = mutableListOf<Pair<TmdbId, Name>>()
    val genres = mutableListOf<Pair<TmdbId, Name>>()
    val movies = mutableListOf<Movie>()
    val movieActors = mutableListOf<Pair<IntId, IntId>>()
    val movieGenres = mutableListOf<Pair<IntId, IntId>>()
    val movieVideos = mutableListOf<Pair<IntId, IntId>>()
    val stats = mutableListOf<Triple<IntId, StatType, Int>>()
    val videos = mutableListOf<Video>()
    val watchlist = mutableListOf<IntId>()

    return spyk(
        LocalStatSourceImpl(
            actors = mockActorQueries(actors),
            genres = mockGenreQueries(genres),
            movies = mockMovieQueries(
                movies,
                movieActors,
                movieGenres,
                movieVideos,
                actors,
                genres,
                stats,
                videos,
                watchlist
            ),
            movieActors = mockMovieActorQueries(movieActors),
            movieGenres = mockMovieGenreQueries(movieGenres),
            stats = mockStatQueries(actors, genres, movies, stats),
            watchlist = mockWatchlistQueries(watchlist),
            years = mockYearRangeQueries(),
        )
    )
}

fun <T> Collection<T>.indexOf(find: (T) -> Boolean) = indexOfFirst(find).takeIf { it >= 0 }

fun <T> MutableList<T>.insert(index: Int?, element: T) {
    if (index != null) this[index] = element
    else this += element
}
