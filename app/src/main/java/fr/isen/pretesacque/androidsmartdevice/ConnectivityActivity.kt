package fr.isen.pretesacque.androidsmartdevice

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import fr.isen.pretesacque.androidsmartdevice.composable.ConnectivityScreen

@SuppressLint("MissingPermission")
class ConnectivityActivity : ComponentActivity() {

    private var bluetoothGatt: BluetoothGatt? = null
    private var bluetoothDevice: BluetoothDevice? = null
    private var isConnected by mutableStateOf(false)

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val bluetoothGattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                isConnected = true
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                isConnected = false
            }
        }
    }

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

        bluetoothDevice = bluetoothAdapter?.getRemoteDevice(deviceAddress)
        bluetoothGatt = bluetoothDevice?.connectGatt(this, false, bluetoothGattCallback)

        if (isConnected) {
            goToInterraction()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Problème lors de la connexion")
            builder.setMessage("Votre appareil n'a pas pu se connecter à l'appareil séléctionner. Veuillez en resélectionner un")
            builder.setPositiveButton("OK") { _, _ ->
                //goBack() TODO : Switch une fois le code tester sur carte
                goToInterraction()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun goBack() {
        val intent = Intent(this, ScanActivity::class.java)
        startActivity(intent)
    }

    private fun goToInterraction(){
        val intent = Intent(this, InterractionActivity::class.java)
        startActivity(intent)
    }
}