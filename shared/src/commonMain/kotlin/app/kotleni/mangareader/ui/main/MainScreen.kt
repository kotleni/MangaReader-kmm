package app.kotleni.mangareader.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
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
    @OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class,
        ExperimentalMaterial3Api::class
    )
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel { MainViewModel(MangaRepository()) }

        val mangaList by viewModel.mangaList.collectAsState()
        val listState = rememberLazyListState()
        var searchText by state.searchText

        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
            rememberTopAppBarState(),
            snapAnimationSpec = snap()
        )

        Column(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            SearchBar(
                searchText = searchText,
                placeholderText = "Search manga",
                onSearchTextChanged = { searchText = it },
                onSubmitClick = { viewModel.searchManga(searchText) },
                scrollBehavior = scrollBehavior
            )
            LazyColumn(
                state = listState,
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