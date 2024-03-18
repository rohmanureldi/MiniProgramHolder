package com.example.miniprogramholder

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.PermissionRequest
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class BluetoothActivityWithCapacitor : AppCompatActivity() {

    private val REQUEST_ENABLE_BT = 1

    private lateinit var webview: WebView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        // Request user to enable Bluetooth
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.BLUETOOTH_CONNECT,
                    android.Manifest.permission.BLUETOOTH_SCAN,
                    android.Manifest.permission.BLUETOOTH,
                    android.Manifest.permission.BLUETOOTH_ADMIN,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                ), REQUEST_ENABLE_BT
            )
        } else {
            initializeWebView()
        }
    }

    private fun initializeWebView() {
        webview = findViewById<WebView>(R.id.webViewBluetooth)
        val webSettings: WebSettings = webview.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.mediaPlaybackRequiresUserGesture = false; // Ensure autoplay works
        webSettings.setGeolocationEnabled(true)
        webSettings.setDomStorageEnabled(true);

        webSettings.setMediaPlaybackRequiresUserGesture(false) // Optional

        webSettings.setAllowUniversalAccessFromFileURLs(true) // Optional


        webview.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed();
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // Handle URL loading (if needed)
                return false
            }
        }

        webview.setLayerType(View.LAYER_TYPE_HARDWARE, null); // Enable hardware acceleration


        webview.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                request?.grant(request.resources)
            }

            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                callback?.invoke(origin, true, false);
            }

            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                // Handle file chooser dialog for file input (if needed)
                return true
            }
        }

        webview.loadUrl(BuildConfig.DEVICE_IP_ADDRESS + "3009")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initialize WebView
                initializeWebView()
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
            }
        }
    }
}