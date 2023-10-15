import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.kotleni.mangareader.MangaRepository
import app.kotleni.mangareader.entities.Manga
import app.kotleni.mangareader.entities.MangaShort
import app.kotleni.mangareader.ui.shared.SearchBar
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

class MainViewModel(
    private val mangaRepository: MangaRepository
) {
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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    val mainViewModel by remember { mutableStateOf(MainViewModel(MangaRepository())) }
    MaterialTheme {
        MainScreen(viewModel = mainViewModel)
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val mangaList by viewModel.mangaList.collectAsState()
    val listState = rememberLazyListState()
    var searchText by remember { mutableStateOf("") }

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
                    // navigator.push(PreviewScreen(it))
                }
            }
        }
    }
}

@Composable
fun MangaItem(manga: MangaShort, onItemClick: () -> Unit) {
    Column(
        modifier = Modifier.padding(8.dp).clickable { onItemClick() }
    ) {
        Row {
            MangaImagePreview(manga.imageUrl)
            Column {
                Text(manga.name, fontWeight = FontWeight.Bold)
                Text(manga.description.substring(0, 50))
                Row {
                    Text(manga.status.toString())
                }
            }
        }
    }
}

@Composable
fun MangaImagePreview(imageUrl: String, height: Dp = 140.dp) {
    KamelImage(
        resource = asyncPainterResource(data = imageUrl),
        contentDescription = "",
        animationSpec = tween(),
        modifier = Modifier.padding(8.dp)
            .height(height)
            .width(100.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}

expect fun getPlatformName(): String