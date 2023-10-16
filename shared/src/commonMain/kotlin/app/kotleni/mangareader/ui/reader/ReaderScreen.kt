package app.kotleni.mangareader.ui.reader

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.PointerInputFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
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
import kotlin.math.max

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

        var containerSize by remember { mutableStateOf(IntSize.Zero) }
        var size by remember { mutableStateOf(IntSize.Zero) }
        var scale by remember { mutableStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }

        val state = rememberTransformableState { zoom, pan, _ ->
            scale *= zoom
            val updatedOffset = Offset(
                x = (offset.x + pan.x * scale),
                y = (offset.y + pan.y * scale)
            )
            val maxX = max(0f, (size.width * scale) - containerSize.width)
            val maxY = max(0f, (size.height * scale) - containerSize.height)

            offset = Offset(
                x = updatedOffset.x.coerceIn(-maxX, maxX),
                y = updatedOffset.y.coerceIn(-maxY, maxY)
            )
        }

        if(currentPage == null) {
            CircularProgressIndicator()
            return
        }

        val page = currentPage!! // Yes, i check it before

        Column(
            modifier = Modifier.onGloballyPositioned { coordinates ->
                containerSize = coordinates.size
            }
        ) {
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
                        .onGloballyPositioned { coordinates ->
                            size = coordinates.size
                        }
                        .pointerInput(Unit) {
                            detectTapGestures {
                                val halfX = size.width / 2
                                if(it.x > halfX)
                                    viewModel.nextPage()
                                else
                                    viewModel.prevPage()

                                scale = 1f
                                offset = Offset.Zero
                            }
                        }
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offset.x,
                            translationY = offset.y
                        )
                        .transformable(state = state)
                )
        }
    }
}