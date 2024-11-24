package fr.isen.pretesacque.androidsmartdevice

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import android.app.AlertDialog
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import fr.isen.pretesacque.androidsmartdevice.composable.ScanScreen

class ScanActivity : ComponentActivity() {

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private val bluetoothLeScanner: BluetoothLeScanner? by lazy {
        (getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter?.bluetoothLeScanner
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                // Si toutes les permissions sont accordées, procéder à l'action suivante
                //TODO : mettre fonction scanning
            } else {
                // Gérer le cas où une ou plusieurs permissions sont refusées
                // Par exemple, montrer un message à l'utilisateur

            }
        }
    private val devices = mutableStateListOf<ScanResult>()
    private var connectionStatus by mutableStateOf(true)

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            addDeviceIfNotExists(result)
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("ScanActivity", "Scan échoué avec le code d'erreur : $errorCode")
            Toast.makeText(this@ScanActivity, "Scan échoué", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                ScanScreen(
                    innerPadding = innerPadding,
                    BLE_List = devices,
                    toggleScan = {
                        connectionStatus = !connectionStatus
                        toggleScan()
                    },
                    scanning = connectionStatus
                )
            }
        }

        initScanBLE()

    }

    //Lance les différentes vérifications du Bluetooth
    private fun initScanBLE() {
        if (checkBluetoothAvailable(this)) {
            if (!allPermissionGranted()) {
                requestPermissionLauncher.launch(getAllPermissionsForBLE())
            }
            checkBluetoothActivated()
        }
    }

    ///Fonction pour vérifier Bluetooth
    ///Vérifie si le Bluetooth est présent sur l'appareil ou non
    private fun checkBluetoothAvailable(activity: Activity): Boolean {
        //Si le bluetooth est disponible ou non
        if (bluetoothAdapter != null) {
            return true
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Bluetooth indisponible")
            builder.setMessage("Votre appareil ne prend pas en charge ")
            builder.setPositiveButton("OK") { _, _ ->
                activity.finishAffinity()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
            return false
        }
    }

    //Vérifie si le Bluetooth est activé ou non
    private fun checkBluetoothActivated() {
        if (bluetoothAdapter!!.isEnabled) {
            toggleScan()
        } else {
            requestBluetoothActivation()
        }

    }

    //Demande à l'utilisateur d'activé son Bluetooth
    private fun requestBluetoothActivation() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        val bluetoothLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // L'utilisateur a activé le Bluetooth
                    Toast.makeText(this, "Bluetooth activé", Toast.LENGTH_SHORT).show()
                } else {
                    // L'utilisateur a refusé d'activer le Bluetooth
                }
            }
        bluetoothLauncher.launch(enableBtIntent)
    }


    ///Fonctions pour vérifier permission
    //Vérifie si toutes les permissions sont autorisés
    private fun allPermissionGranted(): Boolean {
        val allPermission = getAllPermissionsForBLE()
        return allPermission.all { permission ->
            ActivityCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    //Demande toutes les permissions pour le Bluetooth
    private fun getAllPermissionsForBLE(): Array<String> {
        var allPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_ADMIN
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            allPermissions = allPermissions.plus(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADMIN
                )
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            allPermissions = allPermissions.plus(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
        return allPermissions
    }


    ///Fonctions pour lancer le bluetooth
    // Méthode pour démarrer la découverte Bluetooth

    private fun toggleScan() {
        if (connectionStatus) {
            stopScan()
        } else {
            startScan()
        }
    }

    private fun startScan() {
        try {
            devices.clear()  // Effacer les appareils détectés avant de lancer un nouveau scan
            bluetoothLeScanner?.startScan(leScanCallback)
            Toast.makeText(this, "Scan BLE démarré", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Log.e("ScanActivity", "Permissions manquantes : ${e.message}")
            Toast.makeText(this, "Permissions requises pour le scan BLE.", Toast.LENGTH_LONG).show()
        }
    }

    private fun stopScan() {
        try {
            bluetoothLeScanner?.stopScan(leScanCallback)
            Toast.makeText(this, "Scan BLE arrêté", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Log.e("ScanActivity", "Permissions manquantes pour arrêter le scan : ${e.message}")
        }
    }

    private fun addDeviceIfNotExists(result: ScanResult) {
        // Vérifiez si l'adresse MAC du périphérique existe déjà dans la liste
        if (devices.none { it.device.address == result.device.address }) {
            devices.add(result)
        }
    }
}