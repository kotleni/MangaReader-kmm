package app.kotleni.mangareader.entities.api

import kotlinx.serialization.Serializable

@Serializable
data class DesuImage(
    val original: String,
    val preview: String,
)
