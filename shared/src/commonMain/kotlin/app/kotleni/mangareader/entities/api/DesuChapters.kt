package app.kotleni.mangareader.entities.api

import kotlinx.serialization.Serializable

@Serializable
data class DesuChapters(
    val list: List<DesuChapter>
)
