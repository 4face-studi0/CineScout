package client.android

import client.viewModel.GetSuggestedMovieViewModel
import client.viewModel.MovieDetailsViewModel
import client.viewModel.RateMovieViewModel
import client.viewModel.SearchViewModel
import entities.TmdbId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.junit.experimental.categories.Category
import org.koin.core.parameter.parametersOf
import org.koin.test.AutoCloseKoinTest
import org.koin.test.category.CheckModuleTest
import org.koin.test.check.checkModules
import kotlin.test.*

@Category(CheckModuleTest::class)
class DiTest : AutoCloseKoinTest() {

    @Test
    fun checkModules() = checkModules(parameters = {
        create<GetSuggestedMovieViewModel> { parametersOf(CoroutineScope(Job())) }
        create<MovieDetailsViewModel> { parametersOf(CoroutineScope(Job()), TmdbId(0)) }
        create<RateMovieViewModel> { parametersOf(CoroutineScope(Job())) }
        create<SearchViewModel> { parametersOf(CoroutineScope(Job())) }
    }) {
        modules(androidClientModule)
    }
}
