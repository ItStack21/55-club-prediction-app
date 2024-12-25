package com.itstack.predictionapp3.Modal

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ModalDisp(e_txt: String, h_txt: String, buttonLink: String, context: Context) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .width(300.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$e_txt",
                fontSize = 18.sp,
                color = Color.Red,
                fontWeight = FontWeight.ExtraBold
            )
            Text(text = "")
            Text(text = "")
            Text(
                text = "$h_txt",
                fontSize = 18.sp,
                color = Color.Red,
                fontWeight = FontWeight.ExtraBold
                
            )

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(buttonLink))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red // Set the button's background color to red
                )
            ) {
                Text(
                    text = "Register",
                    color = Color.White, // Set the text color to white for contrast
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
