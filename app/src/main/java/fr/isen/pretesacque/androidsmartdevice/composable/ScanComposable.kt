package fr.isen.pretesacque.androidsmartdevice.composable

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.pretesacque.androidsmartdevice.R

@SuppressLint("MissingPermission")
@Composable
fun ScanScreen(
    innerPadding: PaddingValues,
    scanning: Boolean,
    BLE_List: List<ScanResult>,
    toggleScan: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SCAN",
                fontSize = 22.sp,
                textAlign = Center,
                modifier = Modifier.padding(innerPadding)
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(if (!scanning) "Scan en cours" else "Lancer la recherche")
                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(20.dp, 0.dp)
                        .clickable {
                            toggleScan()
                        },
                    painter = painterResource(if (!scanning) R.drawable.pause_blue_button_1 else R.drawable.play_blue_button),
                    contentDescription = "pause",
                )
            }


        }
        if (!scanning) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(BLE_List) { result ->
                val deviceName = result.device.name
                    ?: "Unknown Device" // Récupérer le nom ou afficher "Unknown Device"
                val deviceAddress = result.device.address // Adresse MAC de l'appareil

                Text(
                    text = deviceName,
                )

            }
        }
    }
}

