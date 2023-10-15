import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import app.kotleni.mangareader.MyTheme
import app.kotleni.mangareader.ui.main.MainScreen
import app.kotleni.mangareader.ui.main.MainScreenState
import cafe.adriel.voyager.navigator.Navigator

@Composable
fun App() {
    MyTheme {
        Scaffold() {
            Navigator(MainScreen())
        }
    }
}

expect fun getPlatformName(): String