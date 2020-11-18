package client

import auth.tmdb.tmdbAuthModule
import client.viewModel.DrawerViewModel
import client.viewModel.GetSuggestedMovieViewModel
import client.viewModel.MovieDetailsViewModel
import client.viewModel.RateMovieViewModel
import client.viewModel.SearchViewModel
import client.viewModel.WatchlistViewModel
import domain.domainModule
import entities.TmdbId
import kotlinx.coroutines.CoroutineScope
import movies.remote.tmdb.tmdbRemoteMoviesModule
import org.koin.dsl.module
import profile.tmdb.local.localTmdbProfileModule
import profile.tmdb.remote.remoteTmdbProfileModule
import stats.local.localStatsModule

val clientModule = module {

    single<Navigator> { NavigatorImpl() }

    factory { (scope: CoroutineScope) ->
        DrawerViewModel(
            scope = scope,
            getPersonalTmdbProfile = get(),
            linkToTmdb = get()
        )
    }
    factory { (scope: CoroutineScope) ->
        GetSuggestedMovieViewModel(
            scope = scope,
            dispatchers = get(),
            getSuggestedMovies = get(),
            addMovieToWatchlist = get(),
            rateMovie = get(),
        )
    }
    factory { (scope: CoroutineScope, id: TmdbId) ->
        MovieDetailsViewModel(
            scope = scope,
            movieId = id,
            findMovie = get(),
            getMovieRating = get(),
            isMovieInWatchlist = get(),
            addMovieToWatchlist = get(),
            removeMovieFromWatchlist = get(),
            rateMovie = get(),
        )
    }
    factory { (scope: CoroutineScope) ->
        RateMovieViewModel(
            scope = scope,
            addMovieToWatchlist = get(),
            rateMovie = get(),
            findMovie = get()
        )
    }
    factory { (scope: CoroutineScope) ->
        SearchViewModel(
            scope = scope,
            searchMovies = get(),
        )
    }
    factory { (scope: CoroutineScope) ->
        WatchlistViewModel(
            scope = scope,
            getMoviesInWatchlist = get()
        )
    }

} +
    // domain
    domainModule +

    // auth
    tmdbAuthModule +

    // movies
    tmdbRemoteMoviesModule +

    // profile
    localTmdbProfileModule +
    remoteTmdbProfileModule +

    // stats
    localStatsModule