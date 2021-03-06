package movies.remote.tmdb

import movies.remote.TmdbRemoteMovieSource
import movies.remote.remoteMoviesModule
import movies.remote.tmdb.mapper.MovieDetailsMapper
import movies.remote.tmdb.mapper.MoviePageResultMapper
import movies.remote.tmdb.mapper.MovieResultMapper
import movies.remote.tmdb.movie.MovieDiscoverService
import movies.remote.tmdb.movie.MovieSearchService
import movies.remote.tmdb.movie.MovieService
import network.tmdb.tmdbNetworkModule
import network.tmdb.v3Client
import org.koin.dsl.module

val tmdbRemoteMoviesModule = module {

    factory<TmdbRemoteMovieSource> {
        TmdbRemoteMovieSourceImpl(
            movieDiscoverService = get(),
            movieService = get(),
            movieSearchService = get(),
            moviePageResultMapper = get(),
            movieDetailsMapper = get()
        )
    }

    factory { MovieDiscoverService(client = get(v3Client)) }
    factory { MovieService(client = get(v3Client)) }
    factory { MovieSearchService(client = get(v3Client)) }

    factory { MoviePageResultMapper(movieService = get(), movieDetailsMapper = get()) }
    factory { MovieResultMapper(movieService = get(), movieDetailsMapper = get()) }
    factory { MovieDetailsMapper() }

} + remoteMoviesModule + tmdbNetworkModule
