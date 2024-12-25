package com.itstack.predictionapp3

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itstack.predictionapp3.Application.ContentScreen
import com.itstack.predictionapp3.DataClasses.GETDATA.RegisterDataHandler
import com.itstack.predictionapp3.Modal.ModalDisp
import com.itstack.predictionapp3.ui.theme.PredictionApp3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            com.itstack.predictionapp3.appScreen(LocalContext.current)
        }
    }
}

@Composable
fun appScreen(context: Context) {
    // Remember state for storing the texts
    val englishText = remember { mutableStateOf("") }
    val hindiText = remember { mutableStateOf("") }
    val buttonLink = remember { mutableStateOf("") }
    val isModalVisible = remember { mutableStateOf(true) } // State to control modal visibility
    val isLoading = remember { mutableStateOf(true) } // Loading state to show loading indicator

    // Create an instance of RegisterDataHandler to fetch the data
    val registerDataHandler = RegisterDataHandler(context)

    // Fetch data from API
    LaunchedEffect(Unit) {
        registerDataHandler.registretionData(
            onResult = { data ->
                // Update the texts with the data from the API
                englishText.value = data.pop_up_data.english
                hindiText.value = data.pop_up_data.hindi
                buttonLink.value = data.pop_up_data.button_link

                // Data fetched, set loading to false
                isLoading.value = false
            },
            onError = { error ->
                // Handle the error
                englishText.value = "Error: $error"
                hindiText.value = "Error: $error"
                isLoading.value = false // Hide loading on error
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ContentScreen(context = context)

        if (isModalVisible.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)) // Darkened background
                    .clickable { // Handle outside click to close the modal
                        isModalVisible.value = false
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center // Center the modal content
                ) {
                    if (isLoading.value) {
                        // Show loading circle and text while data is being fetched
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = Color.White)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Loading data...", color = Color.White)
                        }
                    } else {
                        // Show the modal content once data is fetched
                        ModalDisp(
                            e_txt = englishText.value,
                            h_txt = hindiText.value,
                            buttonLink = buttonLink.value,
                            context = context
                        )
                    }
                }
            }
        }
    }
}


