package client.cli.state

import client.cli.controller.GetSuggestionsController
import client.cli.headerThemed
import client.cli.themed
import com.jakewharton.picnic.TextAlignment.MiddleCenter
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table

class GetSuggestionsState : State<GetSuggestionsController>(GetSuggestionsController::class) {

    override fun render() = table {
        themed()

        header {
            headerThemed()

            row {
                cell("${cyan}Do you like this movie?${Reset}") {
                    alignment = MiddleCenter
                    columnSpan = 3
                }
            }
        }
        for (action in actions) {
            row(action.description, *action.commands.toTypedArray())
        }

    }.renderText()
}
