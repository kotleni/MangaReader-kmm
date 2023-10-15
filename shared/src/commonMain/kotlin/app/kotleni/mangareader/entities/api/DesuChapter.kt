package app.kotleni.mangareader.entities.api

import kotlinx.serialization.Serializable

@Serializable
data class DesuChapter(
    val id: Long,
    val vol: Int,
    val ch: Double,
    val title: String?,
    val date: Long
)
