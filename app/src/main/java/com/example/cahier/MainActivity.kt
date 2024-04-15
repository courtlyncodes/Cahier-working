package com.example.cahier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.cahier.data.LocalNotesDataProvider
import com.example.cahier.ui.CahierList
//import com.example.cahier.ui.ListAndDetailScreen
import com.example.cahier.ui.ListDetailPaneScaffoldScreen
import com.example.cahier.ui.NavigationSuiteHomePane
//import com.example.cahier.ui.ListDetailPaneScaffoldScreen
//import com.example.cahier.ui.NavigationSuiteHomePane
import com.example.cahier.ui.NoteDetail
import com.example.cahier.ui.PracticeCanvas
import com.example.cahier.ui.theme.CahierTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CahierTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val note = LocalNotesDataProvider.allNotes.first()
                   ListDetailPaneScaffoldScreen()
                }
            }
        }
    }
}