package profile.tmdb.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Avatar(

    @SerialName("gravatar")
    val gravatar: Gravatar,

    @SerialName("tmdb")
    val tmdb: TmdbAvatar
)

@Serializable
internal data class Gravatar(

    @SerialName("hash")
    val hash: String // c9e9fc152ee756a900db85757c29815d
)

@Serializable
internal data class TmdbAvatar(

    @SerialName("avatar_path")
    val avatarPath: String?
)
