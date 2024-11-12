package fr.isen.pretesacque.androidsmartdevice.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log

class BLEManager(context: Context) {
    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private var bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    private var isScanning = false

    // Callback pour les résultats de scan BLE
    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.let {
                Log.d("BLEManager", "Device found: ${it.device.name} - ${it.device.address}")
                // Ici, vous pouvez ajouter des actions pour traiter les résultats (mise à jour de la liste, etc.)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            results?.forEach { result ->
                Log.d("BLEManager", "Device found: ${result.device.name} - ${result.device.address}")
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("BLEManager", "Scan failed with error: $errorCode")
        }
    }

    // Fonction pour démarrer le scan BLE
    fun startScan() {
        if (!isScanning) {
            bluetoothLeScanner?.startScan(leScanCallback)
            isScanning = true
            Log.d("BLEManager", "Scan started")
        }
    }

    // Fonction pour arrêter le scan BLE
    fun stopScan() {
        if (isScanning) {
            bluetoothLeScanner?.stopScan(leScanCallback)
            isScanning = false
            Log.d("BLEManager", "Scan stopped")
        }
    }

    // Vérifie si le Bluetooth est activé
    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }
}

