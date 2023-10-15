package app.kotleni.mangareader.ui.preview

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.kotleni.mangareader.MangaRepository
import app.kotleni.mangareader.entities.MangaShort
import app.kotleni.mangareader.ui.shared.MangaImagePreview
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow


class PreviewScreen(val mangaShort: MangaShort): Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel { PreviewViewModel(MangaRepository()).also { it.loadAdditionalInfo(mangaShort) } }

        val mangaNullable by viewModel.mangaInfo.collectAsState()
        val scrollState = rememberScrollState()

        if(mangaNullable == null) {
            CircularProgressIndicator()
            return
        }

        val manga = mangaNullable!! // Yes, i check it before

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scrollable(scrollState, Orientation.Vertical)
        ) {
            TopAppBar(
                title = { Text(mangaShort.name) },
                navigationIcon = {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            modifier = Modifier,
                            contentDescription = "Back button"
                        )
                    }
                }
            )

            MangaImagePreview(manga.imageUrl, height = 220.dp, width = 200.dp)

            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(manga.name, fontWeight = FontWeight.Bold)
                Text(manga.description.substring(0, 50))
            }

            Column {
                Text(manga.status.toString())
                Text("Оценка: " + manga.score.toString() + "/10")
            }

            LazyRow {
                for(genre in manga.genres) {
                    item(genre.name) {
                        AssistChip(
                            onClick = { },
                            label = { Text(genre.name) },
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }

            Spacer(
                Modifier.height(16.dp)
            )

            LazyColumn {
                items(manga.chapters) {
                    Row(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = it.title)
                        Spacer(modifier = Modifier.weight(1f))
                        OutlinedButton(onClick = { /*TODO*/ }) {
                            Text(text = "Читать")
                        }
                    }
                }
            }
        }
    }
}