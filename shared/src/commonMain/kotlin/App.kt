import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import app.kotleni.mangareader.ui.main.MainScreen
import app.kotleni.mangareader.ui.main.MainScreenState
import cafe.adriel.voyager.navigator.Navigator

@Composable
fun App() {
    MaterialTheme {
        Navigator(MainScreen())
    }
}

expect fun getPlatformName(): String