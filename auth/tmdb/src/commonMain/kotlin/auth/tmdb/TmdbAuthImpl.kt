package auth.tmdb

import entities.auth.Either_LoginResult
import entities.auth.TmdbAuth
import kotlinx.coroutines.flow.Flow

internal class TmdbAuthImpl(
    private val authService: AuthService
) : TmdbAuth {

    override fun login(): Flow<Either_LoginResult> =
        authService.login()
}
