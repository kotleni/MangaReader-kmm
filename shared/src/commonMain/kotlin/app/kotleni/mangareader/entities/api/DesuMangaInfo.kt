package app.kotleni.mangareader.entities.api

import kotlinx.serialization.Serializable

/***
 * Represent id-only slice of manga object
 */
@Serializable
class DesuMangaInfo(
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
)