package stats.local.double

import database.movies.Movie
import database.movies.MovieDetails
import database.movies.MovieDetailsWithRating
import database.movies.MovieQueries
import database.movies.SelectSuggested
import database.stats.StatType
import entities.IntId
import entities.TmdbId
import entities.model.Name
import entities.model.Video
import io.mockk.every
import io.mockk.mockk

fun mockMovieQueries(
    movies: MutableList<Movie>,
    movieActors: List<Pair<IntId, IntId>>,
    movieGenres: List<Pair<IntId, IntId>>,
    movieVideos: List<Pair<IntId, IntId>>,
    actors: MutableList<Pair<TmdbId, Name>>,
    genres: MutableList<Pair<TmdbId, Name>>,
    stats: List<Triple<IntId, StatType, Int>>,
    suggestions: Set<IntId>,
    videos: List<Video>,
    watchlist: List<IntId>
): MovieQueries = mockk {

    every {
        insert(
            tmdbId = TmdbId(any()),
            title = Name(any()),
            year = any<Int>().toUInt(),
            imageBaseUrl = any(),
            posterPath = any(),
            backdropPath = any(),
            voteAverage = any(),
            voteCount = any(),
            overview = any()
        )
    } answers {
        val idArg = firstArg<Int>()
        val tmdbIdArg = TmdbId(idArg)
        val index = movies.indexOf { it.tmdbId == tmdbIdArg }
        val movie = Movie(
            id = IntId(idArg),
            tmdbId = tmdbIdArg,
            title = Name(secondArg()),
            year = thirdArg<Int>().toUInt(),
            imageBaseUrl = arg(3),
            posterPath = arg(4),
            backdropPath = arg(5),
            voteAverage = arg(6),
            voteCount = arg(7),
            overview = arg(8)
        )
        movies.insert(index, movie)
    }

    every { selectIdByTmdbId(TmdbId(any())) } answers {
        val tmdbIdArg = TmdbId(firstArg())
        mockk {
            every { executeAsOne() } answers {
                IntId(movies.indexOf { it.tmdbId == tmdbIdArg }!!)
            }
            every { executeAsOneOrNull() } answers {
                movies.indexOf { it.tmdbId == tmdbIdArg }?.let(::IntId)
            }
        }
    }

    every { selectAllRated() } answers {
        mockk {
            every { executeAsList() } answers {
                movies.flatMapIndexed { index: Int, movie: Movie ->
                    val movieId = IntId(index)

                    val moviesActors =
                        movieActors.filter { (mId, _) -> mId == movieId }.map { it.second }
                            .map { actors[it.i] }
                    val moviesGenres =
                        movieGenres.filter { (mId, _) -> mId == movieId }.map { it.second }
                            .map { genres[it.i] }
                    val moviesVideos =
                        movieVideos.filter { (mId, _) -> mId == movieId }.map { it.second }
                            .map { videos[it.i] }
                            .ifEmpty { listOf(null) }

                    moviesActors.flatMap { actor ->
                        moviesGenres.flatMap { genre ->
                            moviesVideos.map { video ->
                                MovieDetailsWithRating(
                                    id = movieId,
                                    tmdbId = movie.tmdbId,
                                    title = movie.title,
                                    year = movie.year,
                                    imageBaseUrl = movie.imageBaseUrl,
                                    posterPath = movie.posterPath,
                                    backdropPath = movie.backdropPath,
                                    voteAverage = movie.voteAverage,
                                    voteCount = movie.voteCount,
                                    overview = movie.overview,
                                    actorTmdbId = actor.first,
                                    actorName = actor.second,
                                    genreTmdbId = genre.first,
                                    genreName = genre.second,
                                    videoTmdbId = video?.id,
                                    videoName = video?.title,
                                    videoSite = video?.site,
                                    videoKey = video?.key,
                                    videoType = video?.type,
                                    videoSize = video?.size?.toLong(),
                                    rating = stats.find { (statId, type, _) ->
                                        statId == movieId && type == StatType.MOVIE
                                    }?.third ?: 0
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    every { selectMovieRatingByTmdbId(TmdbId(any())) } answers {
        val tmdbIdArg = TmdbId(firstArg())
        mockk {
            every { executeAsOneOrNull() } answers {
                val id = IntId(movies.indexOf { tmdbIdArg == it.tmdbId }!!)
                stats.find { (statId, type, _) -> statId == id && type == StatType.MOVIE }?.third
            }
        }
    }

    every { selectAllInWatchlist() } answers {
        mockk {
            every { executeAsList() } answers {
                movies.filter { movie ->
                    IntId(movies.indexOf { movie.tmdbId == it.tmdbId }!!) in watchlist
                }.flatMapIndexed { index: Int, movie: Movie ->
                    val movieId = IntId(index)

                    val moviesActors =
                        movieActors.filter { (movieId, _) -> movieId == movieId }.map { it.second }
                            .map { actors[it.i] }
                    val moviesGenres =
                        movieGenres.filter { (movieId, _) -> movieId == movieId }.map { it.second }
                            .map { genres[it.i] }
                    val moviesVideos =
                        movieVideos.filter { (mId, _) -> mId == movieId }.map { it.second }
                            .map { videos[it.i] }
                            .ifEmpty { listOf(null) }

                    moviesActors.flatMap { actor ->
                        moviesGenres.flatMap { genre ->
                            moviesVideos.map { video ->
                                MovieDetails(
                                    id = movieId,
                                    tmdbId = movie.tmdbId,
                                    title = movie.title,
                                    year = movie.year,
                                    imageBaseUrl = movie.imageBaseUrl,
                                    posterPath = movie.posterPath,
                                    backdropPath = movie.backdropPath,
                                    voteAverage = movie.voteAverage,
                                    voteCount = movie.voteCount,
                                    overview = movie.overview,
                                    actorTmdbId = actor.first,
                                    actorName = actor.second,
                                    genreTmdbId = genre.first,
                                    genreName = genre.second,
                                    videoTmdbId = video?.id,
                                    videoName = video?.title,
                                    videoSite = video?.site,
                                    videoKey = video?.key,
                                    videoType = video?.type,
                                    videoSize = video?.size?.toLong(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    every { selectInWatchlistByTmdbId(TmdbId(any())) } answers {
        val tmdbIdArg = TmdbId(firstArg())
        mockk {
            every { executeAsOneOrNull() } answers {
                val id = IntId(movies.indexOf { tmdbIdArg == it.tmdbId }!!)
                if (id in watchlist) id else null
            }
        }
    }

    every { selectSuggested() } answers {
        mockk {
            every { executeAsList() } answers {
                movies.filter { movie ->
                    IntId(movies.indexOf { movie.tmdbId == it.tmdbId }!!) in suggestions
                }.flatMapIndexed { index: Int, movie: Movie ->
                    val movieId = IntId(index)

                    val moviesActors =
                        movieActors.filter { (movieId, _) -> movieId == movieId }.map { it.second }
                            .map { actors[it.i] }
                    val moviesGenres =
                        movieGenres.filter { (movieId, _) -> movieId == movieId }.map { it.second }
                            .map { genres[it.i] }
                    val moviesVideos =
                        movieVideos.filter { (mId, _) -> mId == movieId }.map { it.second }
                            .map { videos[it.i] }
                            .ifEmpty { listOf(null) }

                    moviesActors.flatMap { actor ->
                        moviesGenres.flatMap { genre ->
                            moviesVideos.map { video ->
                                SelectSuggested(
                                    id = movieId,
                                    id_ = movieId,
                                    movieId = movieId,
                                    tmdbId = movie.tmdbId,
                                    title = movie.title,
                                    year = movie.year,
                                    imageBaseUrl = movie.imageBaseUrl,
                                    posterPath = movie.posterPath,
                                    backdropPath = movie.backdropPath,
                                    voteAverage = movie.voteAverage,
                                    voteCount = movie.voteCount,
                                    overview = movie.overview,
                                    actorTmdbId = actor.first,
                                    actorName = actor.second,
                                    genreTmdbId = genre.first,
                                    genreName = genre.second,
                                    videoTmdbId = video?.id,
                                    videoName = video?.title,
                                    videoSite = video?.site,
                                    videoKey = video?.key,
                                    videoType = video?.type,
                                    videoSize = video?.size?.toLong(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
