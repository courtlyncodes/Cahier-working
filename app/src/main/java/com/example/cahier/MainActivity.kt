package com.example.cahier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cahier.ui.CahierApp
import com.example.cahier.ui.StylusState
import com.example.cahier.ui.viewmodels.StylusViewModel
import com.example.cahier.ui.theme.CahierTheme
import com.example.cahier.ui.viewmodels.StylusViewModelFactory
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: StylusViewModel by viewModels() {
        StylusViewModelFactory((application as CahierApplication).container.notesRepository)
    }
    private var stylusState: StylusState by mutableStateOf(StylusState())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Stylus codelab is helpful for this LifecycleScope
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stylusState
                    .onEach {
                        stylusState = it
                    }
                    // throws error
                    .collect {}
            }
        }
        setContent {
            CahierTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   CahierApp()
                }
            }
        }

    }
}