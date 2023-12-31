package app.kotleni.mangareader.ui.shared

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun MangaImagePreview(imageUrl: String, height: Dp = 140.dp, width: Dp = 100.dp, modifier: Modifier = Modifier) {
    KamelImage(
        resource = asyncPainterResource(data = imageUrl),
        contentDescription = "",
        animationSpec = tween(),
        onLoading = { CircularProgressIndicator() },
        modifier = modifier.height(height)
            .width(width)
            // .clip(RoundedCornerShape(8.dp))
    )
}