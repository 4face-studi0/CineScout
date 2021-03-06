package entities.auth

import kotlinx.coroutines.flow.Flow

interface TmdbAuth {

    fun login(): Flow<Either_LoginResult>
}
