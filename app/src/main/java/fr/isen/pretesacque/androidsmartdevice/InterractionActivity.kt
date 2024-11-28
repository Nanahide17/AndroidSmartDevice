package fr.isen.pretesacque.androidsmartdevice

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fr.isen.pretesacque.androidsmartdevice.composable.InterractionScreen

@SuppressLint("MissingPermission")
class InterractionActivity : ComponentActivity() {


    private var bluetoothGatt: BluetoothGatt? = null
    private var ledCharacteristic: BluetoothGattCharacteristic? = null
    private val leds = listOf(
        LED(1, false),
        LED(2, false),
        LED(3, false)
    )


    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                val builder = AlertDialog.Builder(this@InterractionActivity)
                builder.setTitle("Problème lors de la connexion")
                builder.setMessage("Vous êtes déconnecter de l'appareil, veuillez vous reconnecter")
                builder.setPositiveButton("OK") { _, _ ->
                    goBack()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val service = gatt.services
                ledCharacteristic = service?.get(2)?.characteristics?.get(0)
            } else {
                val builder = AlertDialog.Builder(this@InterractionActivity)
                builder.setTitle("Problème lors des services")
                builder.setMessage("Votre appareil n'a pas pu identifier les services. Veuillez vous reconnecter à votre appareil")
                builder.setPositiveButton("OK") { _, _ ->
                    goBack()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //Log.d("BLE", "Characteristic mise a jour: ${characteristic?.uuid}")
            } else {
                Log.d(
                    "BLE",
                    "Erreur dans la mise a jour de la characteristic : ${characteristic?.uuid}"
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = { Text("Interraction", color = Color.White) },
                        navigationIcon = {
                            IconButton(onClick = { goBack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Retour",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF0082FC),
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White
                        )
                    )
                })
            { innerPadding ->

                InterractionScreen(innerPadding = innerPadding,
                    ledInterraction = { numLED ->
                        val selectedLED = when(numLED){
                            1 -> leds[0]
                            2 -> leds[1]
                            3 -> leds[2]
                            else -> null
                        }
                        if (selectedLED != null) {
                            ledInterraction(bluetoothGatt = bluetoothGatt, ledCharacteristic, selectedLED)
                        }
                    },
                    listLeds = leds)

            }
        }
        bluetoothGatt?.discoverServices()
    }

    private fun ledInterraction(bluetoothGatt: BluetoothGatt?, ledCharacteristic: BluetoothGattCharacteristic?, ledToSwitch: LED) {
        ledCharacteristic?.let { characteristic ->
            if (!ledToSwitch.isOn) {
                val valeur = when (ledToSwitch.valeur) {
                    1 -> byteArrayOf(0x01)
                    2 -> byteArrayOf(0x02)
                    3 -> byteArrayOf(0x03)
                    else -> byteArrayOf(0x00)
                }
                val writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                bluetoothGatt?.let { gatt ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        gatt.writeCharacteristic(characteristic,valeur, writeType)
                    } else {
                        // Fall back to deprecated version of writeCharacteristic for Android <13
                        gatt.legacyCharacteristicWrite(characteristic, valeur, writeType)
                    }
                }
            } else {
                val off = byteArrayOf(0x00)
                val writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                bluetoothGatt?.let { gatt ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        gatt.writeCharacteristic(characteristic,off, writeType)
                    } else {
                        // Fall back to deprecated version of writeCharacteristic for Android <13
                        gatt.legacyCharacteristicWrite(characteristic, off, writeType)
                    }
                }
            }
            ledToSwitch.toogle()

        }
    }

    @Suppress("DEPRECATION")
    private fun BluetoothGatt.legacyCharacteristicWrite(
        characteristic: BluetoothGattCharacteristic,
        value: ByteArray,
        writeType: Int
    ) {
        characteristic.writeType = writeType
        characteristic.value = value
        writeCharacteristic(characteristic)
    }

    private fun goBack() {
        val intent = Intent(this, ScanActivity::class.java)
        startActivity(intent)
    }
}


class LED(var valeur: Int, var isOn: Boolean) {
    fun toogle() {
        isOn = !isOn
    }
}