package fr.isen.pretesacque.androidsmartdevice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import fr.isen.pretesacque.androidsmartdevice.composable.ConnectivityScreen

class ConnectivityActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val deviceName = intent.getStringExtra("device_name") ?: "Unknown Device"
        val deviceAddress = intent.getStringExtra("device_address") ?: "N/A"

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                ConnectivityScreen(
                    innerPadding = innerPadding,
                    deviceName = deviceName,
                    deviceAddress = deviceAddress
                )
            }
        }
    }
}