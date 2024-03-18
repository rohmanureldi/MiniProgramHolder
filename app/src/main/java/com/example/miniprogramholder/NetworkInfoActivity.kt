package com.example.miniprogramholder

import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class NetworkInfoActivity : AppCompatActivity() {

    private lateinit var webview: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        initializeWebView()
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

        webview.loadUrl(BuildConfig.DEVICE_IP_ADDRESS+ "3005")

    }

}