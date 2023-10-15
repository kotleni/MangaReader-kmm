package app.kotleni.mangareader.ui.reader

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import app.kotleni.mangareader.MangaRepository
import app.kotleni.mangareader.entities.Manga
import app.kotleni.mangareader.entities.MangaChapter
import app.kotleni.mangareader.ui.preview.PreviewViewModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

class ReaderScreen(
    private val manga: Manga,
    private val chapter: MangaChapter
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel { ReaderViewModel(MangaRepository()).also { it.loadPages(manga, chapter) } }

        val currentPage by viewModel.currentPage.collectAsState()
        var scale by remember { mutableStateOf(1f) }
        //var rotation by remember { mutableStateOf(0f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            scale *= zoomChange
            //rotation += rotationChange
            offset += offsetChange * scale
        }

        if(currentPage == null) {
            CircularProgressIndicator()
            return
        }

        val page = currentPage!! // Yes, i check it before

        Column {
            TopAppBar(
                title = { Text("${chapter.title} - ${page.number}") },
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

                KamelImage(
                    resource = asyncPainterResource(
                        // MARK: hot fix for image loading
                        // MARK: all images have only new backend
                        data = page.imageUrl.replace("desu.me", "desu.win")
                    ),
                    contentDescription = "Manga page image",
                    onLoading = { CircularProgressIndicator() },
                    onFailure = { Text("Loading (url: ${page.imageUrl}) error: ${it.message}").also { println(page.imageUrl) } },
                    animationSpec = tween(),
                    modifier = Modifier.fillMaxSize()
                        .clickable { viewModel.nextPage() }
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            // rotationZ = rotation,
                            translationX = offset.x,
                            translationY = offset.y
                        )
                        .transformable(state = state)
                )
        }
    }
}