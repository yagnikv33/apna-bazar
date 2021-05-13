package com.skeletonkotlin.e_cigarette.helper.util

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import android.bluetooth.BluetoothDevice



class BleUtil
    (val mActivity: Activity, val mContext: Context, private val ACTION_INTENT: String) {
    private var devices = arrayListOf<String>()
    private var mBluetoothGatt: BluetoothGatt? = null
    private var bluetoothManager: BluetoothManager? = null
    private var mDevice: BluetoothDevice? = null


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private val mBluetoothGattCallBack = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            when (newState) {
                NOT_CONNECTED -> {

                }
                CONNECTING -> {

                }
                CONNECTED -> {
                    mBluetoothGatt!!.discoverServices()
                }
                else -> {

                }
            }


        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)

            if (status == BluetoothGatt.GATT_SUCCESS)
                getBleServices(gatt.services)
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {

            super.onCharacteristicRead(gatt, characteristic, status)
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            super.onCharacteristicChanged(gatt, characteristic)

            broadcastUpdate(characteristic)
        }

        override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            super.onDescriptorRead(gatt, descriptor, status)
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            super.onDescriptorWrite(gatt, descriptor, status)
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    internal var mLeScanCallback: BluetoothAdapter.LeScanCallback =
        BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
            devices.add(device.address)

            if (device.name != null && device.name.toLowerCase().contains("idoor"))
                connectBLE(device.address)
        }


    init {

        devices = ArrayList()
        enableBle()

    }


    /**
     * Enable Bluetooth
     */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun enableBle() {
        bluetoothManager = mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager!!.adapter

        if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            mActivity.startActivityForResult(enableBtIntent,
                REQUEST_ENABLE_BT
            )

        }
    }

    /**
     * Check Permission
     *
     * @param permission
     * @return
     */
    fun checkPermission(permission: Int): Boolean {
        val permission_ACCESS_COARSE_LOCATION =
            ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
        //int permission_BLUETOOTH = ContextCompat.checkSelfPermission(mContext,Manifest.permission.BLUETOOTH);
        if (permission_ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED)
            return true
        else {
            ActivityCompat.requestPermissions(
                mActivity,
                arrayOf<String>(Manifest.permission.ACCESS_COARSE_LOCATION),
                permission
            )
            return false

        }
    }

    /**
     * scan Device
     *
     * @param enable
     * @param SCAN_PERIOD
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun scanLeDevice(enable: Boolean, SCAN_PERIOD: Int) {
        if (enable) {
            Handler().postDelayed({
                mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
            }, SCAN_PERIOD.toLong())


            mBluetoothAdapter!!.startLeScan(mLeScanCallback)
        } else {

            mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
        }
    }


    /**
     * Get The List Of Scanned Device
     *
     * @return
     */
    fun getDevices(): ArrayList<String>? {
        // Removing  All Duplicates
        val hashSet = HashSet<String>()
        hashSet.addAll(devices)
        devices.clear()
        return if (devices.addAll(hashSet)) {
            devices
        } else {
            null
        }

    }

    /**
     * Connecting To BlueTooth
     */

    // Connect Bluetooth !
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun connectBLE(deviceAddress: String?) {

        if (deviceAddress != null) {
            mDevice = mBluetoothAdapter!!.getRemoteDevice(deviceAddress)

            mBluetoothGatt = mDevice!!.connectGatt(mContext, false, mBluetoothGattCallBack)
            // creating a Bond
            //  mDevice.setPin(new byte[]{(byte)123456});


            val bondState = mDevice!!.createBond()
        }
    }

    /**
     * Disconnect BLe
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun disConnectBLE() {
        mBluetoothGatt!!.disconnect()
    }

    /**
     * Get Services
     *
     * @param gattServices
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getBleServices(gattServices: List<BluetoothGattService>) {
        var characteristics: List<BluetoothGattCharacteristic> = ArrayList()
        for (services in gattServices) {

            characteristics = services.characteristics
            for (characteristic in characteristics) {

                /**
                 * Enable notification here, data will get received in characteristicsChanged/'characteristicsWrite'
                 * enableNotification(characteristic, true);
                 */
            }
        }

        /**
         * To send commands
         */
        val lockArray = ByteArray(2)
        lockArray[0] = 0
        lockArray[1] = 111
        writeCharacteristics(
            lockArray, UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb"),
            UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb")
        )
    }

    /**
     * Enable Notification
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun enableNotification(characteristic: BluetoothGattCharacteristic, DESCRIPTOR_ID: UUID, enable: Boolean) {

        val descriptor = characteristic.getDescriptor(DESCRIPTOR_ID)

        mBluetoothGatt!!.setCharacteristicNotification(characteristic, enable)
        descriptor.value =
            if (enable) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
        mBluetoothGatt!!.writeDescriptor(descriptor)
    }

    /**
     * write On Characteristics
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun writeCharacteristics(data: ByteArray?, DLE_SERVICE: UUID, DLE_WRITE_CHAR: UUID) {
        val service = mBluetoothGatt!!.getService(DLE_SERVICE)
        if (service != null) {

            val characteristic = service.getCharacteristic(DLE_WRITE_CHAR)
            if (characteristic != null) {

                characteristic.value = data
                mBluetoothGatt!!.writeCharacteristic(characteristic)

                if (data != null && data.size > 0) {
                    val stringBuilder = StringBuilder(data.size)
                    for (byteChar in data)
                        stringBuilder.append(String.format("%02X ", byteChar))
                }
            }
        }
    }

    /**
     * Broadcasting Notification
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun broadcastUpdate(characteristic: BluetoothGattCharacteristic) {

        val data = characteristic.value
        if (data != null && data.isNotEmpty()) {
            val stringBuilder = StringBuilder(data.size)
            for (byteChar in data)
                stringBuilder.append(String.format("%02X ", byteChar))

            val intent = Intent(ACTION_INTENT)
            intent.putExtra("data", stringBuilder.toString())
            mContext.sendBroadcast(intent)
        }
    }

    companion object {
        val TAG = "Ble"

        private val NOT_CONNECTED = 0
        private val CONNECTING = 1
        private val CONNECTED = 2
        var REQUEST_ENABLE_BT = 102
        private var mBluetoothAdapter: BluetoothAdapter? = null
    }
}

class BluetoothSPPConnector
/**
 * @param device the device
 * @param secure if connection should be done via a secure socket
 * @param adapter the Android BT adapter
 * @param uuidCandidates a list of UUIDs. if null or empty, the Serial PP id is used
 */
    (
    private val device: BluetoothDevice, private val secure: Boolean, private val adapter: BluetoothAdapter,
    uuidCandidates: MutableList<UUID>
) {

    private var bluetoothSocket: BluetoothSocketWrapper? = null
    private var uuidCandidates: MutableList<UUID>? = null
    private var candidate: Int = 0


    init {
        this.uuidCandidates = uuidCandidates
    }

    /* fun connectViaSPP() {
         val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
         val socket = BluetoothSPPConnector(
             bluetoothAdapter.getRemoteDevice("22:22:AE:0A:7B:15"), false, bluetoothAdapter,
             mutableListOf(UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66"))
         ).connect()

         if (socket != null) {
             val ipStream = socket.inputStream
             val opStream = socket.outputStream

             opStream.write("wifi,Sumeet,yudiz!@!@".toByteArray())

             val buffer = ByteArray(1024)
             var bytes: Int

             while (true) {
                 try {
                     bytes = ipStream.read(buffer)
                     Log.i("readaaa", String(buffer, 0, bytes))

                 } catch (e: IOException) {
                     socket.close()
                     break
                 }

             }
         }
     }*/

    @Throws(IOException::class)
    fun connect(): BluetoothSocketWrapper? {
        var success = false
        while (selectSocket()) {
            adapter.cancelDiscovery()

            try {
                bluetoothSocket!!.connect()
                success = true
                break
            } catch (e: IOException) {
                //try the fallback
                try {
                    bluetoothSocket = FallbackBluetoothSocket(bluetoothSocket!!.underlyingSocket)
                    Thread.sleep(500)
                    bluetoothSocket!!.connect()
                    success = true
                    break
                } catch (e1: FallbackException) {
                    "BT -> Could not initialize FallbackBluetoothSocket classes $e".logI()
                } catch (e1: InterruptedException) {
                   "BT -> e1.message, $e1".logI()
                } catch (e1: IOException) {
                    "BT->Fallback failed. Cancelling. $e1".logI()
                }

            }

        }

        if (!success)
            "BT -> Could not connect to device: ${device.address}".logI()

        return bluetoothSocket
    }

    @Throws(IOException::class)
    private fun selectSocket(): Boolean {
        if (candidate >= uuidCandidates!!.size) {
            return false
        }

        val tmp: BluetoothSocket
        val uuid = uuidCandidates!![candidate++]

        tmp = if (secure)
            device.createRfcommSocketToServiceRecord(uuid)
        else
            device.createInsecureRfcommSocketToServiceRecord(uuid)
        bluetoothSocket =
            NativeBluetoothSocket(tmp)

        return true
    }

    interface BluetoothSocketWrapper {

        val inputStream: InputStream

        val outputStream: OutputStream

        val remoteDeviceName: String

        val remoteDeviceAddress: String

        val underlyingSocket: BluetoothSocket

        @Throws(IOException::class)
        fun connect()

        @Throws(IOException::class)
        fun close()

    }


    open class NativeBluetoothSocket(override val underlyingSocket: BluetoothSocket) :
        BluetoothSocketWrapper {

        override val inputStream: InputStream
            @Throws(IOException::class)
            get() = underlyingSocket.inputStream

        override val outputStream: OutputStream
            @Throws(IOException::class)
            get() = underlyingSocket.outputStream

        override val remoteDeviceName: String
            get() = underlyingSocket.remoteDevice.name

        override val remoteDeviceAddress: String
            get() = underlyingSocket.remoteDevice.address

        @Throws(IOException::class)
        override fun connect() {
            underlyingSocket.connect()
        }

        @Throws(IOException::class)
        override fun close() {
            underlyingSocket.close()
        }

    }

    inner class FallbackBluetoothSocket @Throws(FallbackException::class)
    constructor(tmp: BluetoothSocket) : NativeBluetoothSocket(tmp) {

        private var fallbackSocket: BluetoothSocket? = null

        override val inputStream: InputStream
            @Throws(IOException::class)
            get() = fallbackSocket!!.inputStream

        override val outputStream: OutputStream
            @Throws(IOException::class)
            get() = fallbackSocket!!.outputStream

        init {
            try {
                val clazz = tmp.remoteDevice.javaClass
                val paramTypes = arrayOf<Class<*>>(Integer.TYPE)
                val m = clazz.getMethod("createRfcommSocket", *paramTypes)
                val params = arrayOf<Any>(Integer.valueOf(1))
                fallbackSocket = m.invoke(tmp.remoteDevice, *params) as BluetoothSocket
            } catch (e: Exception) {
                throw FallbackException(e)
            }

        }


        @Throws(IOException::class)
        override fun connect() {
            fallbackSocket!!.connect()
        }


        @Throws(IOException::class)
        override fun close() {
            fallbackSocket!!.close()
        }

    }

    class FallbackException(e: Exception) : Exception(e) {
        companion object {

            private val serialVersionUID = 1L
        }

    }
}