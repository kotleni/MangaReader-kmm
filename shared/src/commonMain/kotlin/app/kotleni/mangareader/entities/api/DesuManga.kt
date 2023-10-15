package app.kotleni.mangareader.entities.api

import kotlinx.serialization.Serializable

@Serializable
data class DesuManga(
    val id: Long,
    val name: String,
    val russian: String,
    val description: String,
    val kind: String,
    val image: DesuImage,
    val url: String,
    val reading: String,
    val status: String,
    val score: Double,
    val genres: List<DesuGenre>,
    val chapters: DesuChapters,
    val pages: DesuPages?
)
