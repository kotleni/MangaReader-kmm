package app.kotleni.mangareader.entities

/***
 * Represent status of manga release.
 */
sealed class MangaStatus {
    data object Ongoing: MangaStatus()
    data object Released: MangaStatus()
    data object Unknown: MangaStatus()

    companion object {
        fun fromString(inp: String) = when(inp) {
            "ongoing" -> Ongoing
            "released" -> Released
            else -> Unknown
        }
    }
}