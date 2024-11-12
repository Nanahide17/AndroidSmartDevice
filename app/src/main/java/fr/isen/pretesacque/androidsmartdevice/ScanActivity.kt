package fr.isen.pretesacque.androidsmartdevice


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import fr.isen.pretesacque.androidsmartdevice.composable.ScanScreen
import fr.isen.pretesacque.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme
import android.bluetooth.BluetoothAdapter as BluetoothAdapter

private val REQUEST_ENABLE_BT = 1
private val PERMISSION_REQUEST_CODE = 100

@Suppress("DEPRECATION")
class ScanActivity : ComponentActivity() {
    //val bluetoothManager = getSystemService(BluetoothManager::class.java)
    //val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()
    private lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkBluetoothAvailability()
        setContent {
            AndroidSmartDeviceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScanScreen(
                        innerPadding = innerPadding,
                        BLE_List = listOf("Bonjour", "Hello", "Guten Tag"),
                        onStartScan = { startScanning() },
                        onStopScan = { stopScanning() }
                    )
                }
            }
        }
    }

    private fun checkBluetoothPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val hasScanPermission = checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
            val hasConnectPermission = checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED

            if (!hasScanPermission || !hasConnectPermission) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ),
                    PERMISSION_REQUEST_CODE
                )
                false // Les permissions n'étaient pas encore accordées
            } else {
                true // Permissions déjà accordées
            }
        } else {
            // Pas besoin de permissions spéciales pour les versions < API 31
            true
        }
    }

    fun checkBluetoothAvailability() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        // Vérification de la présence de Bluetooth
        if(checkBluetoothPermissions()) {
            if (bluetoothAdapter == null) {
                // Si l'appareil ne prend pas en charge le Bluetooth, afficher une boîte de dialogue
                AlertDialog.Builder(this)
                    .setTitle("Bluetooth non disponible")
                    .setMessage("Votre appareil ne prend pas en charge le Bluetooth.")
                    .setPositiveButton("OK") { dialog, _ -> finish() }
                    .show()
            } else {
                //TODO : à refaire !!!
                if (bluetoothAdapter?.isEnabled == false) {
                    AlertDialog.Builder(this)
                        .setTitle("Bluetooth désactivé")
                        .setMessage("Voulez vous activer votre Bluetooth ?")
                        .setPositiveButton("Oui") { dialog, _ ->
                            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Non") { dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }
                        .show()
                } else {
                    //TODO : ajouter scan
                }
            }
        }
    }

    private fun startScanning() {
        Toast.makeText(this, "Démarrage du scan...", Toast.LENGTH_SHORT).show()
        // Ajouter la logique de scan BLE ici

    }

    private fun stopScanning() {
        Toast.makeText(this, "Scan arrêté.", Toast.LENGTH_SHORT).show()
        // Ajouter la logique d'arrêt du scan ici
    }
}


