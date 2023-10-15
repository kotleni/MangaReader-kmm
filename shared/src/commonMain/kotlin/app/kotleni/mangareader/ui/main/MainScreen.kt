package app.kotleni.mangareader.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.kotleni.mangareader.MangaRepository
import app.kotleni.mangareader.ui.preview.PreviewScreen
import app.kotleni.mangareader.ui.shared.MangaItem
import app.kotleni.mangareader.ui.shared.SearchBar
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class MainScreen(
    private val state: MainScreenState = MainScreenState()
) : Screen {
    @OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel { MainViewModel(MangaRepository()) }

        val mangaList by viewModel.mangaList.collectAsState()
        val listState = rememberLazyListState()
        var searchText by state.searchText

        Column {
            SearchBar(
                searchText = searchText,
                onSearchTextChanged = { searchText = it },
                onClearClick = { viewModel.searchManga(searchText) },
                modifier = Modifier.padding(4.dp)
            )

            LazyColumn(
                state = listState
            ) {
                items(mangaList) {
                    MangaItem(it) {
                        navigator.push(PreviewScreen(it))
                    }
                }
            }
        }
    }
}