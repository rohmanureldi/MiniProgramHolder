package com.example.miniprogramholder

import android.Manifest
import android.content.pm.PackageManager
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.PermissionRequest
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class DeviceMotionActivityWithCapacitor : AppCompatActivity() {

    private val REQUEST_ACTIVITY_RECOGNITION = 1

    private lateinit var webview: WebView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        // Request user to enable Bluetooth
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
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
                    android.Manifest.permission.ACTIVITY_RECOGNITION,
                    ), REQUEST_ACTIVITY_RECOGNITION
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


    webview.webViewClient = object : WebViewClient() {
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed();
        }
    }

    webview.setLayerType(View.LAYER_TYPE_HARDWARE, null); // Enable hardware acceleration


    webview.webChromeClient = object : WebChromeClient() {
        override fun onPermissionRequest(request: PermissionRequest?) {
            request?.grant(request.resources)
        }
    }

    webview.loadUrl(BuildConfig.DEVICE_IP_ADDRESS+ "3008")

}

override fun onRequestPermissionsResult(
    requestCode: Int, permissions: Array<String?>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == REQUEST_ACTIVITY_RECOGNITION) {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, initialize WebView
            initializeWebView()
        } else {
            // Permission denied, handle accordingly (e.g., show a message)
        }
    }
}
}