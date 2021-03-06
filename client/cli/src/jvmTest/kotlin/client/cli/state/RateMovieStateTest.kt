package client.cli.state

import assert4k.*
import client.cli.util.CliTest
import client.viewModel.RateMovieViewModel
import domain.Test.Movie.Inception
import entities.model.UserRating
import io.mockk.coVerify
import kotlinx.coroutines.test.runBlockingTest
import org.koin.test.get
import kotlin.test.*

internal class RateMovieStateTest : CliTest() {

    private val state by lazy { RateMovieState(get()) }

    @Test
    fun `Home is displayed, when command "*home" is inserted`() = runBlockingTest {
        val result = state execute "*home"
        assert that result `is` type<MenuState>()
    }

    @Test
    fun `Home is displayed, when command "*h" capitalized is inserted`() = runBlockingTest {
        val result = state execute "*h"
        assert that result `is` type<MenuState>()
    }

    @Test
    fun `Movie is rated correctly`() = runBlockingTest {
        state execute "${Inception.id.i}"
        coVerify { get<RateMovieViewModel>()[Inception.id] = UserRating.Positive }
    }
}
