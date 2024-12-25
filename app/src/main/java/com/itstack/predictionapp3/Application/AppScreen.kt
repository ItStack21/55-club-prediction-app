package com.itstack.predictionapp3.Application

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.itstack.predictionapp3.R
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.itstack.predictionapp3.DataClasses.GETDATA.DataHandler
import com.itstack.predictionapp3.DataClasses.GETDATA.MainData
import com.itstack.predictionapp3.DataClasses.POSTDATA.PostHandler
import java.util.Calendar
import kotlinx.coroutines.delay

@Composable
fun ContentScreen(context: Context) {
    val selectedBalls = remember { mutableStateListOf<Int>() }
    var arr by remember { mutableStateOf(arrayOf<Int>()) }
    val predictionResult = remember { mutableStateOf("PREDICTION: ") }
    val showModal = remember { mutableStateOf(false) } // Track modal visibility
    // State for API data (MainData)
    val mainData = remember { mutableStateOf<MainData?>(null) }


    fun handlePrediction() {
        if (selectedBalls.size == 5) {
            PostHandler(arr, context) { prediction ->
                predictionResult.value = prediction // "Small" or "Big"
                showModal.value = true // Show the modal after receiving the result
            }.generateResponse()
        } else {
            Toast.makeText(context, "PLEASE SELECT 5 NUMBERS.", Toast.LENGTH_SHORT).show()
        }
    }


    // Fetch API data
    LaunchedEffect(Unit) {
        DataHandler.fetchRegisterData(context) { data ->
            mainData.value = data
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFFFA3939),
                        Color(0xFFD56B6B)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 35.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White // White Background
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (mainData.value != null) {
                        // Display fetched logo
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                        ) {
                            Image(
                                painter = rememberImagePainter(mainData.value?.logo), // Using URL from API
                                contentDescription = "App Logo",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = mainData.value?.text ?: "Loading...",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 8.dp),
                                color = Color.Red,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Button(
                                onClick = {
                                    val buttonLink = mainData.value?.button_link ?: "Loading....."
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(buttonLink))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(Color.Red)
                            ) {
                                Text(
                                    text = "REGISTER",
                                    color = Color.White
                                )
                            }
                        }
                    } else {
                        // Show loading indicator while fetching data
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp)
                        )
                        Text(
                            text = "Loading...",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
        Text(
            text = "ENTER LAST 5 NUMBERS",
            modifier = Modifier.padding(start = 25.dp, top = 25.dp),
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold

        )


        // First Card - Displaying Selected Balls
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 13.dp)
                .height(65.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xA0FFFFFF) // White with high opacity (63%)
            ),
        ){
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Display the selected balls as images
                    for (i in selectedBalls.indices) {
                        val ballImage = getBallImage(selectedBalls[i]) // Get the image for each selected ball

                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape) ,
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = ballImage),
                                contentDescription = "Ball ${selectedBalls[i]}",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        selectedBalls.clear() // Clear the selected balls
                        predictionResult.value = "PREDICTION: " // Reset prediction text
                        arr = arrayOf() // Clear the array as well
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .padding(10.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "CLEAR SECTION",
                        tint = Color.Black
                    )
                }
            }
        }

        var timeLeft by remember { mutableStateOf(59) }
        val isSelectable = timeLeft in 6..59
        // LaunchedEffect to handle countdown logic
        LaunchedEffect(timeLeft) {
            while (true) {
                timeLeft = getSecondsLeftInMinute() // Update the time left dynamically
                delay(1000L) // Wait for 1 second
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp) ,
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center // Space between logo and timer
            ) {
                // Logo on the left
                Image(
                    painter = rememberImagePainter(mainData.value?.wingo_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(64.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                // Timer on the right
                Box(
                    modifier = Modifier
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ) // White background for the timer
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(48.dp), // Inner padding for the timer
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("00 : %02d", timeLeft),
                        fontSize = 24.sp,
                        color = Color.Black, // Adjust text color as needed
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Prediction Text
//        Text(
//            text = predictionResult.value,
//            modifier = Modifier.padding(horizontal = 25.dp),
//            fontSize = 18.sp,
//            color = Color.White // Set the text color to white
//        )

        if (showModal.value) {
            when (predictionResult.value) {
                "Small" -> PredictionModal(
                    imageResId = R.drawable.small_card,
                    onDismiss = { showModal.value = false }
                )
                "Big" -> PredictionModal(
                    imageResId = R.drawable.big_card,
                    onDismiss = { showModal.value = false }
                )
            }
        }

        // Second Card - Available Balls to Select
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White // White Background
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // First row (balls 0 to 4)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (ballIndex in 0..4) {
                        BallView(context, ballIndex, selectedBalls, isSelectable) { updatedArr -> arr = updatedArr }
                    }
                }

                // Second row (balls 5 to 9)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (ballIndex in 5..9) {
                        BallView(context, ballIndex, selectedBalls, isSelectable) { updatedArr -> arr = updatedArr }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Check Button
            Button(
                onClick = {
                    if (selectedBalls.size == 5) {
                        PostHandler(arr, context) { prediction ->
                            predictionResult.value = "PREDICTION: $prediction"
                        }.generateResponse()
                    } else {
                        Toast.makeText(context, "PLEASE SELECT 5 NUMBERS.", Toast.LENGTH_SHORT).show()
                    }

                    handlePrediction()
                },
                colors = ButtonDefaults.buttonColors(Color.Red),
                modifier = Modifier
                    .border(
                        width = 2.dp, // Thickness of the border
                        color = Color.White, // Border color
                        shape = CircleShape // Shape of the border matches CircleShape
                    )
                    .clip(CircleShape) // Ensures the Button itself is circular
            ) {
                Text(
                    text = "CHECK PREDICTION",
                    color = Color.White
                )
            }

            // Clear Button (New button added here)
            Button(
                onClick = {
                    selectedBalls.clear()
                    predictionResult.value = "PREDICTION: "
                    arr = arrayOf() // Clear the array as well
                },
                colors = ButtonDefaults.buttonColors(Color.Red),
                modifier = Modifier
                    .padding(top = 4.dp)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
            ) {
                Text(
                    text = "CLEAR SECTION",
                    color = Color.White
                )
            }

        }
        Text(
            text = mainData.value?.buttom_text1 ?: "",
            color = Color.Red,
            fontSize = 19.sp,
            modifier = Modifier
                .fillMaxWidth() // Make the background span the full width
                .background(Color.White) // Apply the background color
                .padding(5.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // First text
            Text(
                text = mainData.value?.buttom_text2 ?: "",
                color = Color.White,
                fontSize = 19.sp,
                modifier = Modifier
                    .fillMaxWidth() // Make the background span the full width
                    .padding(5.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold
            )

            // Row for levels
            Row(
                modifier = Modifier
                    .fillMaxWidth() // Make the background span the full width
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Level 1 to Level 6
                val chart = arrayOf("RS - 10", "RS - 20", "RS - 40", "RS - 80", "RS - 160", "RS - 320")
                (1..6).forEach { level ->
                    Column {
                        Text(
                            text = "Level $level",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .background(Color.White)
                                .padding(3.dp),// Rounded background
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = chart[level-1],  // Access the value from the array using index
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(top = 3.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                }
            }



        }

    }
}


@Composable
fun BallView(context: Context, ballIndex: Int, selectedBalls: MutableList<Int>, isSelectable: Boolean, onUpdateArr: (Array<Int>) -> Unit) {
    // Get the ball image corresponding to the index
    val ballImage = getBallImage(ballIndex)

    // Check if the ball is selected
    // Remove the 'isSelected' condition to allow duplicate selections

    Box(
        modifier = Modifier
            .size(45.dp)
            .clickable(enabled = isSelectable) {
                // Add the ball to the selected list without checking for duplicates
                if (selectedBalls.size < 5) {
                    selectedBalls.add(ballIndex) // Allow duplicates
                    onUpdateArr(selectedBalls.toTypedArray()) // Update the array of selected balls
                } else {
                    Toast
                        .makeText(
                            context,
                            "YOU CAN ONLY SELECT UPTO 5 NUMBERS.",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Show the ball image
        Image(
            painter = painterResource(id = ballImage),
            contentDescription = "Ball $ballIndex",
            modifier = Modifier.fillMaxSize()
        )
    }
}


fun getBallImage(ballIndex: Int): Int {
    return when (ballIndex) {
        0 -> R.drawable.coin0
        1 -> R.drawable.coin1
        2 -> R.drawable.coin2
        3 -> R.drawable.coin3
        4 -> R.drawable.coin4
        5 -> R.drawable.coin5
        6 -> R.drawable.coin6
        7 -> R.drawable.coin7
        8 -> R.drawable.coin8
        9 -> R.drawable.coin9
        else -> R.drawable.coin0
    }
}

@Composable
fun PredictionModal(imageResId: Int, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = { onDismiss() }) // Close modal when clicked anywhere on the screen
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp) // Modal content size
                    .align(Alignment.Center) // Center modal content
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize() // Image fills the modal content
                )
            }
        }
    }

}
fun getSecondsLeftInMinute(): Int {
    val calendar = Calendar.getInstance()
    return 59 - calendar.get(Calendar.SECOND)
}



