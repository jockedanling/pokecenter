package com.example.pokecenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pokecenter.ui.theme.PokeCenterTheme
import com.example.pokecenter.ui.navigation.PokedexNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeCenterTheme {
                PokedexNavGraph()
            }
        }
    }
}