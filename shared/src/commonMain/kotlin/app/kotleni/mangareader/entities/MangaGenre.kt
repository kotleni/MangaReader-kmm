package app.kotleni.mangareader.entities

/**
 * Represents a genre with its kind and name.
 *
 * @property kind Kind name (like a romance, other).
 * @property name Translated name.
 */
data class MangaGenre(
    val kind: String,
    val name: String
)