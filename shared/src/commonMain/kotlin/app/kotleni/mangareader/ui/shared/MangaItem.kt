package app.kotleni.mangareader.ui.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.kotleni.mangareader.entities.MangaShort

@Composable
fun MangaItem(manga: MangaShort, onItemClick: () -> Unit) {
    Column(
        modifier = Modifier.padding(8.dp).clickable { onItemClick() }
    ) {
        Row {
            MangaImagePreview(manga.imageUrl, modifier = Modifier.padding(8.dp))
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