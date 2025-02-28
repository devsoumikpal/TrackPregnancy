package com.example.trackpregnancy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.trackpregnancy.models.VitalsViewModel
import com.example.trackpregnancy.ui.theme.TrackPregnancyTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        VitalsWorkManager.scheduleVitalsReminder(this)
        setContent {
            TrackPregnancyTheme {
                val viewModel: VitalsViewModel by viewModels()
                var showDialog by remember { mutableStateOf(false) }
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Track My Pregnancy") },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color(0xFFC8ADFC)
                        )
                    ) },
                    floatingActionButton = { FloatingActionButton(onClick = {showDialog = true}){
                        Icon(Icons.Default.Add, contentDescription = "Add Vitals")
                    } }
                ) { innerPadding ->
                    VitalsScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )

                    if (showDialog){
                        AddVitalsDialog(
                            onDissmiss = {showDialog= false},
                            onSave = {vital->
                                viewModel.addvital(vital)
                                showDialog=false
                            }
                        )
                    }
                }
            }
        }
    }
}