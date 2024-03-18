package com.example.miniprogramholder

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.JavascriptInterface
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.example.miniprogramholder.databinding.ActivityBluetoothBinding

class BluetoothNativeImplActivity : AppCompatActivity() {

    private val webUrl = BuildConfig.DEVICE_IP_ADDRESS + "3011/"

    private lateinit var binding: ActivityBluetoothBinding

//    private var bluetoothCallbackLamba = { dataList: String ->
//        "(function() { var evt = new CustomEvent(\"MyEventType\", {detail: \"$dataList\"});\nwindow.dispatchEvent(evt); })();"
//    }

    private val bluetoothCallback = { btData: String ->
        "javascript:handleBluetoothData('${btData}')"
    }

    private val deviceList = arrayListOf<String>()

    private val btManager: BluetoothManager? by lazy {
        getSystemService()
    }

    private val receiver = object : BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action ?: "") {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return
                    val deviceName = device.name ?: "UNKNOWN"
                    val deviceHardwareAddress = device.address ?: "" // MAC address
                    deviceList.add("$deviceName - $deviceHardwareAddress")
                    val deviceListStr = deviceList.joinToString(separator = "|")
                    runOnUiThread {
                        binding.webViewBluetooth.evaluateJavascript(
                            bluetoothCallback(deviceListStr),
                            null
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        btManager?.adapter?.cancelDiscovery()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWebView()
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView() {
        binding.webViewBluetooth.apply {
            settings.apply {
                javaScriptEnabled = true
                allowFileAccess = false
                allowContentAccess = false
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            }
            val webInterface =
                WebAppInterface(applicationContext, btManager?.adapter, binding.webViewBluetooth)
            addJavascriptInterface(webInterface, "Android")
            webViewClient = this@BluetoothNativeImplActivity.webViewClient
            loadUrl(webUrl)
        }
    }

    private var webViewClient = object : WebViewClient() {
        @SuppressLint("WebViewClientOnReceivedSslError")
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            //For Dev purpose
            handler?.proceed()
            //super.onReceivedSslError(view, handler, error)
        }
    }

    inner class WebAppInterface(
        private val context: Context,
        private val bluetoothAdapter: BluetoothAdapter?,
        private val webView: WebView
    ) {

        @JavascriptInterface
        fun turnOn() {

        }

        @SuppressLint("MissingPermission")
        @JavascriptInterface
        fun discovery() {
            deviceList.clear()
            Log.i("####discovery::", "Clickeddd")
            bluetoothAdapter?.startDiscovery()
            // For Testing only on Emulator Uncomment the below line.
            /*runOnUiThread {
                binding.webViewBluetooth.evaluateJavascript(
                    bluetoothCallback("User 1 - 90:02:09:87:10|User 2 - 89:e0:a1:o5:7i|User 3 - 00:01:a1:oi:01"),
                    null
                )
            }*/
        }
    }


}