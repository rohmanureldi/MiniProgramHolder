package com.example.miniprogramholder

import android.content.pm.PackageManager
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker


class CameraActivityWithCapacitor : AppCompatActivity() {

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private lateinit var webview: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkCameraPermission()
        }
    }

    private fun initializeWebView() {
        webview = findViewById<WebView>(R.id.webView)
        val webSettings: WebSettings = webview.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.mediaPlaybackRequiresUserGesture = false; // Ensure autoplay works
        webSettings.setAllowFileAccessFromFileURLs(true)
        webSettings.setAllowUniversalAccessFromFileURLs(true)
        webview.webViewClient = object: WebViewClient() {
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed();
            }
        }

        webview.setLayerType(View.LAYER_TYPE_HARDWARE, null); // Enable hardware acceleration


        webview.webChromeClient = object: WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                request?.grant(request.resources)
            }
        }

        webview.loadUrl(BuildConfig.DEVICE_IP_ADDRESS+ "3007")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            // Camera permission has not been granted
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA
            ), CAMERA_PERMISSION_REQUEST_CODE)

        } else {
            initializeWebView()
            // Camera permission is already granted
            // Proceed with camera-related operations
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initialize WebView
                initializeWebView()
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
            }
        }
    }
}

//Note the way to handle permission depends on UI/UX