package fr.isen.pretesacque.androidsmartdevice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import fr.isen.pretesacque.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme
import fr.isen.pretesacque.androidsmartdevice.composable.ScanScreen

class ScanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndroidSmartDeviceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScanScreen(
                        innerPadding = innerPadding,
                        BLE_List = listOf("Bonjour", "Hello", "Guten Tag"),
                        onStartScan = TODO(),
                        onStopScan = TODO()
                    );
                }
            }
        }
    }
}
