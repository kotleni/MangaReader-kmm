package app.kotleni.mangareader.ui.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class MainScreenState(
    var searchText: MutableState<String> = mutableStateOf(""),
)