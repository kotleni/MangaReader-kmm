package app.kotleni.mangareader

import app.kotleni.mangareader.entities.Manga
import app.kotleni.mangareader.entities.MangaGenre
import app.kotleni.mangareader.entities.MangaPage
import app.kotleni.mangareader.entities.MangaShort

class MangaRepository {
    private val desuMeClient: DesuMeClient = DesuMeClientImpl()

    suspend fun searchManga(query: String, genres: List<MangaGenre> = listOf(), limit: Int = 10, offset: Int = 0): List<MangaShort>? {
        return desuMeClient.searchManga(query, genres, limit, offset)
    }
    suspend fun fetchMangaInfo(mangaId: Long): Manga? {
        return desuMeClient.fetchMangaInfo(mangaId)
    }
    suspend fun fetchPopularManga(limit: Int = 10): List<MangaShort>? {
        return desuMeClient.fetchPopularManga(limit)
    }
    suspend fun fetchMangaPages(mangaId: Long, chapterId: Long): List<MangaPage>? {
        return desuMeClient.fetchMangaPages(mangaId, chapterId)
    }
}