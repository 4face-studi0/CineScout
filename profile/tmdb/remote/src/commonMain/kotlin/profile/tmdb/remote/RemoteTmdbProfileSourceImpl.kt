package profile.tmdb.remote

import entities.TmdbId
import entities.model.GravatarImage
import entities.model.Name
import entities.model.Profile
import profile.tmdb.RemoteTmdbProfileSource
import profile.tmdb.remote.model.AccountResult

internal class RemoteTmdbProfileSourceImpl(
    private val accountService: AccountService
) : RemoteTmdbProfileSource {

    override suspend fun getPersonalProfile(): Profile =
        accountService.getPersonalProfile().toBusinessModel()

    // TODO build GravatarImage
    private fun AccountResult.toBusinessModel() = Profile(
        id = TmdbId(id),
        username = Name(username),
        name = Name(name),
        avatar = GravatarImage("TODO", "TODO"),
        adult = includeAdult
    )
}