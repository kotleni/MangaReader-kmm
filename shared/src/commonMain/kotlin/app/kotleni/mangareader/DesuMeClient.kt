package app.kotleni.mangareader

import app.kotleni.mangareader.entities.Manga
import app.kotleni.mangareader.entities.MangaChapter
import app.kotleni.mangareader.entities.MangaGenre
import app.kotleni.mangareader.entities.MangaPage
import app.kotleni.mangareader.entities.MangaShort

interface DesuMeClient {
    suspend fun searchManga(query: String, genres: List<MangaGenre> = listOf(), limit: Int = 10, offset: Int = 0): List<MangaShort>?
    suspend fun fetchMangaInfo(mangaId: Long): Manga?
    suspend fun fetchPopularManga(limit: Int = 10): List<MangaShort>?
    suspend fun fetchMangaPages(mangaId: Long, chapterId: Long): List<MangaPage>?
}