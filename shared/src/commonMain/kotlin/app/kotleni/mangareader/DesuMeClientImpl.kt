package app.kotleni.mangareader

import app.kotleni.mangareader.entities.Manga
import app.kotleni.mangareader.entities.MangaChapter
import app.kotleni.mangareader.entities.MangaGenre
import app.kotleni.mangareader.entities.MangaPage
import app.kotleni.mangareader.entities.MangaShort
import app.kotleni.mangareader.entities.MangaStatus
import app.kotleni.mangareader.entities.api.DesuMangaResult
import app.kotleni.mangareader.entities.api.DesuMangasInfoResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.TypeInfo
import kotlinx.serialization.json.Json

class DesuMeClientImpl : DesuMeClient {
    private val BASE_URL = "https://desu.me/manga"
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override suspend fun searchManga(
        query: String,
        genres: List<MangaGenre>,
        limit: Int,
        offset: Int
    ): List<MangaShort>? {
        val response = client.get("${BASE_URL}/api?search=$query&genres=${genres.joinToString(",")}&limit=$limit&page=${offset + 1}")
        if(response.status.value !in 200..299) return null
        val result: DesuMangasInfoResult = response.body()
        return result.response.map {
            MangaShort(
                id = it.id,
                name = it.russian,
                description = it.description,
                imageUrl = it.image.preview,
                status = MangaStatus.fromString(it.status),
            )
        }
    }

    override suspend fun fetchMangaInfo(mangaId: Long): Manga? {
        val response = client.get("${BASE_URL}/api/$mangaId")
        if(response.status.value !in 200..299) return null
        val result: DesuMangaResult = response.body()
        val it = result.response

        return Manga(
            id = it.id,
            name = it.russian,
            description = it.description,
            imageUrl = it.image.preview,
            status = MangaStatus.fromString(it.status),
            genres = it.genres.map { MangaGenre(it.kind, it.russian) },
            score = it.score,
            chapters = it.chapters.list.reversed().map {
                // TODO: fix stupid code
                val number = if(it.ch.toInt().toDouble() == it.ch) it.ch.toInt() else it.ch
                val title = if(it.title != null) "${it.title} ($number)" else "Глава $number"

                MangaChapter(
                    id = it.id,
                    title = title
                )
            }
        )
    }

    override suspend fun fetchPopularManga(limit: Int): List<MangaShort>? {
        return searchManga("", limit = limit)
    }

    override suspend fun fetchMangaPages(mangaId: Long, chapterId: Long): List<MangaPage>? {
        val response = client.get("${BASE_URL}/api/$mangaId/chapter/$chapterId")
        if(response.status.value !in 200..299) return null
        val result: DesuMangaResult = response.body()
        val it = result.response
        if(it.pages == null) return null
        return it.pages.list.map {
            MangaPage(
                number = it.page,
                width = it.width,
                height = it.height,
                imageUrl = it.img
            )
        }
    }
}