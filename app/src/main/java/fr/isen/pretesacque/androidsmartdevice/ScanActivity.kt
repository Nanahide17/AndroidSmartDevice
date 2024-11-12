package fr.isen.pretesacque.androidsmartdevice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Scaffold
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
import fr.isen.pretesacque.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme
import fr.isen.pretesacque.androidsmartdevice.ble.BLEManager

class ScanActivity : ComponentActivity() {
    private lateinit var bleManager: BLEManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bleManager = BLEManager(this)
        setContent {
            AndroidSmartDeviceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScanScreen(
                        innerPadding = innerPadding,
                        BLE_List = listOf("Bonjour", "Hello", "Guten Tag"),
                        onStartScan = { bleManager.startScan() },
                        onStopScan = { bleManager.stopScan() }
                    );
                }
            }
        }
    }
}

@Composable
fun ScanScreen(
    innerPadding: PaddingValues,
    BLE_List: List<String>,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit
) {
    var scanning by remember { mutableStateOf(false) }
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
                Text(if (scanning) "Scan en cours" else "Lancer la recherche")
                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(20.dp, 0.dp)
                        .clickable {
                            if (scanning) {
                                onStopScan()
                            } else {
                                onStartScan()
                            }
                            scanning = !scanning
                        },
                    painter = painterResource(if (scanning) R.drawable.pause_blue_button_1 else R.drawable.play_blue_button),
                    contentDescription = "pause",
                )
            }


        }
        if (scanning) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Espace entre les éléments
            ) {
                items(BLE_List) { name ->
                    Text(text = name)
                }

            }

        }
    }
}