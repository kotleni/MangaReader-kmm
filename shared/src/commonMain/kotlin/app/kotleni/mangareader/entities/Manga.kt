package app.kotleni.mangareader.entities

import app.kotleni.mangareader.entities.MangaGenre
import app.kotleni.mangareader.entities.MangaStatus

data class Manga(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val status: MangaStatus,
    val genres: List<MangaGenre>,
    val score: Double,
    val chapters: List<MangaChapter>
)