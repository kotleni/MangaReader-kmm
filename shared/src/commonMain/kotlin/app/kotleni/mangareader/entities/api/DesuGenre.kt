package app.kotleni.mangareader.entities.api

import kotlinx.serialization.Serializable

@Serializable
data class DesuGenre(
    val kind: String,
    val text: String,
    val russian: String
)
