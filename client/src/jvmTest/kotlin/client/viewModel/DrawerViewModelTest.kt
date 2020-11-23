package client.viewModel

import assert4k.*
import client.util.ViewModelTest
import domain.auth.IsTmdbLoggedIn
import domain.auth.Link
import domain.auth.LinkToTmdb
import domain.profile.GetPersonalTmdbProfile
import entities.Either
import entities.TestData.DummyProfile
import entities.auth.Auth.LoginState
import entities.right
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlin.test.*
import kotlin.time.minutes

class DrawerViewModelTest : ViewModelTest {

    private val hasLoginCompleted = MutableStateFlow(false)
    private val getPersonalTmdbProfile = mockk<GetPersonalTmdbProfile> {
        every { this@mockk() } returns flowOf(DummyProfile.right())
    }
    private val isTmdbLoggedIn = mockk<IsTmdbLoggedIn> {
        every { this@mockk() } returns hasLoginCompleted
    }
    private val approveRequestToken = LoginState.ApproveRequestToken.WithoutCode("", Channel())
    private val linkToTmdb = mockk<LinkToTmdb> {
        every { this@mockk() } returns flowOf(
            Link.State.Login(LoginState.Loading),
            Link.State.Login(approveRequestToken),
            Link.State.Login(LoginState.Completed),
        ).onCompletion { hasLoginCompleted.value = true }.map { it.right() }
    }

    @Test
    fun `profile is emitted correctly after the login`() = viewModelTest(
        { DrawerViewModel(this, getPersonalTmdbProfile, isTmdbLoggedIn, linkToTmdb) },
        ignoreUnfinishedJobs = true
    ) { viewModel ->

        val result = mutableListOf<Any>(viewModel.profile.value)
        advanceTimeBy(5.minutes.toLongMilliseconds())
        result += "input"
        viewModel.startLinkingToTmdb()
        result += viewModel.profile.value

        assert that result equals listOf(
            DrawerViewModel.ProfileState.LoggedOut,
            "input",
            DrawerViewModel.ProfileState.LoggedIn(DummyProfile),
        )
    }

    @Test
    fun `profile is emitted correctly is the user is already logged in`() = viewModelTest(
        {
            every { isTmdbLoggedIn() } returns flowOf(true)
            DrawerViewModel(this, getPersonalTmdbProfile, isTmdbLoggedIn, linkToTmdb)
        },
        ignoreUnfinishedJobs = true
    ) { viewModel ->
        val result = viewModel.profile.first()
        assert that result equals DrawerViewModel.ProfileState.LoggedIn(DummyProfile)
    }

    @Test
    fun `token approval request is prompted correctly`() = viewModelTest(
        { DrawerViewModel(this, getPersonalTmdbProfile, isTmdbLoggedIn, linkToTmdb) },
        ignoreUnfinishedJobs = true
    ) { viewModel ->

        val result = mutableListOf<Either<Link.Error, Link.State>>()
        launch {
            viewModel.tmdbLinkResult.toList(result)
        }
        viewModel.startLinkingToTmdb()

        val state = result[result.lastIndex - 1].rightOrNull()
        assert that state `is` type<Link.State.Login>()
        val loginState = (state as Link.State.Login).loginState
        assert that loginState `is` type<LoginState.ApproveRequestToken.WithoutCode>()
    }
}
