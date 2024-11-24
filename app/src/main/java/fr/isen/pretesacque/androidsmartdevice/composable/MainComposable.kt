package fr.isen.pretesacque.androidsmartdevice.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.pretesacque.androidsmartdevice.R

@Composable
fun MainContentComponent(innerPadding: PaddingValues, onButtonClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = "Android Smart Device",
                fontSize = 22.sp,
                textAlign = Center,
                color = Color(0xFF0082FC),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(innerPadding)
            )
            Text(
                text = "Cette application permet de scanner des appareils BLE à proximité.",
                textAlign = Center,

                )
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .padding(12.dp, 10.dp),
                painter = painterResource(R.drawable.bluetooth),
                contentDescription = "logo"
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp, 10.dp),
                onClick = onButtonClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF191FB7),
                    contentColor = Color.White
                )
            )
            {
                Text(text = "Scan BLE")
            }
        }

    }
}