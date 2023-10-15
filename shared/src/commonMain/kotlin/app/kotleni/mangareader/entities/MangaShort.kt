package app.kotleni.mangareader.entities

data class MangaShort(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val status: MangaStatus,
)