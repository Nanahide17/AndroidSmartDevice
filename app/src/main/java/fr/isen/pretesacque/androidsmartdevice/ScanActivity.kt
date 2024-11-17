package fr.isen.pretesacque.androidsmartdevice

import android.Manifest
import android.annotation.SuppressLint
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
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private const val REQUEST_ENABLE_BT = 1
private const val REQUEST_PERMISSION_BT = 1001

class ScanActivity : ComponentActivity() {

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
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

    private val discoveredDevices = mutableListOf<BluetoothDevice>()

    // BroadcastReceiver pour gérer les appareils découverts
    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                // Récupérer l'appareil Bluetooth détecté
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    if (!discoveredDevices.contains(it)) {
                        discoveredDevices.add(it) // Ajouter à la liste si non présent
                        val deviceName = it.name ?: "Appareil inconnu"
                        val deviceHardwareAddress = it.address // Adresse MAC
                        // Affiche un Toast avec les informations de l'appareil
                        Toast.makeText(context, "Appareil ajouté: $deviceName [$deviceHardwareAddress]", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initScanBLE()
        setContent {
            //TODO : mettre composable pour affichage
            //ScanScreen(

        }

    }

    //Lance les différentes vérifications du Bluetooth
    private fun initScanBLE() {
        if (checkBluetoothAvailable(this)) {
            if (allPermissionGranted()) {
                checkBluetoothActivated()
            } else {
                requestPermissionLauncher.launch(getAllPermissions())
            }
        }
    }

    ///Fonction Pour vérifier Bluetooth
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
    private fun checkBluetoothActivated(){
        if (bluetoothAdapter!!.isEnabled) {

        } else {
            requestBluetoothActivation()
        }
    }

    //Demande à l'utilisateur d'activé son Bluetooth
    private fun requestBluetoothActivation(){
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        val bluetoothLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
        val allPermission = getAllPermissions()
        return allPermission.all { permission ->
            ActivityCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    //Renvoie toutes les permissions pour le Bluetooth
    private fun getAllPermissions(): Array<String> {
        val allPermissions = getAllPermissionsForBLE()
        return allPermissions
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

    // Méthode pour démarrer la découverte Bluetooth
    @SuppressLint("MissingPermission")
    private fun startBluetoothDiscovery() {
        if (bluetoothAdapter?.isDiscovering == true) {
            bluetoothAdapter?.cancelDiscovery()
        }

        // Vider la liste avant chaque découverte
        discoveredDevices.clear()

        // Enregistrer le BroadcastReceiver pour ACTION_FOUND
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)

        // Démarrer la découverte
        bluetoothAdapter?.startDiscovery()
        Toast.makeText(this, "Début de la découverte Bluetooth", Toast.LENGTH_SHORT).show()
    }

    // Méthode pour arrêter la découverte Bluetooth
    @SuppressLint("MissingPermission")
    private fun stopBluetoothDiscovery() {
        if (bluetoothAdapter?.isDiscovering == true) {
            bluetoothAdapter?.cancelDiscovery()
        }

        // Désenregistrer le BroadcastReceiver
        unregisterReceiver(receiver)
        Toast.makeText(this, "Fin de la découverte Bluetooth", Toast.LENGTH_SHORT).show()
    }

    // Méthode pour accéder aux appareils découverts
    private fun getDiscoveredDevices(): List<BluetoothDevice> {
        return discoveredDevices
    }

}