package app.kotleni.mangareader.ui.preview

import app.kotleni.mangareader.MangaRepository
import app.kotleni.mangareader.entities.Manga
import app.kotleni.mangareader.entities.MangaChapter
import app.kotleni.mangareader.entities.MangaShort
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PreviewViewModel(
    private val mangaRepository: MangaRepository
): ScreenModel {
    private val _mangaInfo = MutableStateFlow<Manga?>(null)
    val mangaInfo = _mangaInfo.asStateFlow()

    fun loadAdditionalInfo(mangaShort: MangaShort) = coroutineScope.launch {
        val mangaInfo = mangaRepository.fetchMangaInfo(mangaShort.id)
        if(mangaInfo != null) {
            _mangaInfo.value = mangaInfo
        }
    }
}