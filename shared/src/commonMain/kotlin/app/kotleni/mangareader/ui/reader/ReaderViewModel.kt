package app.kotleni.mangareader.ui.reader

import app.kotleni.mangareader.MangaRepository
import app.kotleni.mangareader.entities.Manga
import app.kotleni.mangareader.entities.MangaChapter
import app.kotleni.mangareader.entities.MangaPage
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReaderViewModel(
    private val mangaRepository: MangaRepository
) : ScreenModel {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _allPages = MutableStateFlow<List<MangaPage>>(listOf())
    private var currentPageIndex = 0

    private val _currentPage = MutableStateFlow<MangaPage?>(null)
    val currentPage = _currentPage.asStateFlow()

    fun loadPages(manga: Manga, chapter: MangaChapter) = coroutineScope.launch {
        val pages = withContext(Dispatchers.IO) {
            mangaRepository.fetchMangaPages(manga.id, chapter.id)
        }
        if(pages != null) {
            _allPages.value = pages
            _currentPage.value = pages.first()
        }
    }

    fun nextPage() {
        if(currentPageIndex >= _allPages.value.lastIndex)
            return

        currentPageIndex += 1
        _currentPage.value = _allPages.value[currentPageIndex]
    }

    fun prevPage() {
        if(currentPageIndex <= 0)
            return

        currentPageIndex -= 1
        _currentPage.value = _allPages.value[currentPageIndex]
    }
}