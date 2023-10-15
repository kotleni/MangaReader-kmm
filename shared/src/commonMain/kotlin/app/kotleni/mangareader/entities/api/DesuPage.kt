package app.kotleni.mangareader.entities.api

import kotlinx.serialization.Serializable

@Serializable
data class DesuPage(
    val page: Int,
    val width: Int,
    val height: Int,
    val img: String
)