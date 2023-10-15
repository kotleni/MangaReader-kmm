package app.kotleni.mangareader.entities.api

import kotlinx.serialization.Serializable

@Serializable
data class DesuMangasInfoResult(
    val response: List<DesuMangaInfo>
)