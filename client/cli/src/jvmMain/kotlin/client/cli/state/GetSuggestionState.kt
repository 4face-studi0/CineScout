package client.cli.state

import client.awaitData
import client.cli.Action
import client.cli.HomeAction
import client.cli.error.throwWrongCommand
import client.cli.view.Suggestion
import client.resource.Strings
import client.viewModel.GetSuggestedMovieViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class GetSuggestionState(private val getSuggestedMovieViewModel: GetSuggestedMovieViewModel) : State() {

    override val actions = Companion.actions

    override suspend infix fun execute(command: String): State {
        return when (actionBy(command)) {
            HomeAction -> MenuState
            YesAction -> {
                getSuggestedMovieViewModel.likeCurrent()
                GetSuggestionState(getSuggestedMovieViewModel)
            }
            NoAction -> {
                getSuggestedMovieViewModel.dislikeCurrent()
                GetSuggestionState(getSuggestedMovieViewModel)
            }
            SkipAction -> {
                getSuggestedMovieViewModel.skipCurrent()
                GetSuggestionState(getSuggestedMovieViewModel)
            }
            else -> command.throwWrongCommand()
        }
    }

    override fun render(): String = runBlocking {
        when (val state = getSuggestedMovieViewModel.result.first()) {
            is GetSuggestedMovieViewModel.State.Success -> Suggestion(state.movie, actions).render()
            GetSuggestedMovieViewModel.State.Loading -> Strings.LoadingMessage
            GetSuggestedMovieViewModel.State.NoSuggestions -> Strings.NoRateMoviesError
        }
    }

    companion object : GetActions {

        private val YesAction = Action("Yes", "yes", "y")
        private val NoAction = Action("No", "no", "n")
        private val SkipAction = Action("Skip", "skip", "s")

        override val actions
            get() = setOf(
                YesAction,
                NoAction,
                SkipAction,
                HomeAction
            )

    }
}
