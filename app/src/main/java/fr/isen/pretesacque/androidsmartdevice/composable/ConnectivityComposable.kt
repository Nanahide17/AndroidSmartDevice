package fr.isen.pretesacque.androidsmartdevice.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ConnectivityScreen(
    innerPadding : PaddingValues,
    deviceName: String,
    deviceAddress: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8E8E8)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Nom : $deviceName",
            color = Color(0xFF747474))
        Text(text = "Adresse : $deviceAddress",
            color = Color(0xFF747474))
        Text(text="Connexion en cours",
            modifier = Modifier.padding(16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF747474))

        CircularProgressIndicator(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}