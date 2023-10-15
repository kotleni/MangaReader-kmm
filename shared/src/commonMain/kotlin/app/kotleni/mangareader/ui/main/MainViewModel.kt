package app.kotleni.mangareader.ui.main

import app.kotleni.mangareader.MangaRepository
import app.kotleni.mangareader.entities.MangaShort
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val mangaRepository: MangaRepository
) : ScreenModel {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _mangaList = MutableStateFlow<List<MangaShort>>(listOf())
    val mangaList = _mangaList.asStateFlow()

    init {
        loadPopular()
    }

    fun loadPopular() = coroutineScope.launch {
        val list = withContext(Dispatchers.IO) {
            mangaRepository.fetchPopularManga()
        }
        if(list != null)
            _mangaList.value = list
    }

    fun searchManga(searchText: String) = coroutineScope.launch {
        val list = withContext(Dispatchers.IO) {
            mangaRepository.searchManga(searchText)
        }
        if(list != null)
            _mangaList.value = list
    }
}