package domain.stats

import entities.left
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SyncTraktWatchlist {

    operator fun invoke(): Flow<Either_SyncResult> =
        // TODO
        flowOf(Sync.Error.left())
}
