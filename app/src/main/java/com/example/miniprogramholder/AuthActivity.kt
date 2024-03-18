package com.example.miniprogramholder

import android.annotation.SuppressLint
import android.content.Context
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.miniprogramholder.databinding.ActivityBluetoothBinding
import org.json.JSONObject

class AuthActivity : AppCompatActivity() {

    private val webUrl = BuildConfig.DEVICE_IP_ADDRESS + "3011/"

    private lateinit var binding: ActivityBluetoothBinding


    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWebView()

        val jsonObj = JSONObject()

        jsonObj.put(
            "token",
            "Bearer dhkjasfhajkshdbkahsjljgfnaskhdjhasbhjkfbndsakhjbfsdnfhjadjbnfhjsbfankjdsbnfjakdhsnka"
        ).toString()
        Log.e("AuthActivity", "onCreate: $jsonObj", )
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
                WebAppInterface(applicationContext, binding.webViewBluetooth)
            addJavascriptInterface(webInterface, "Android")
            webViewClient = this@AuthActivity.webViewClient
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
        private val webView: WebView
    ) {

        @SuppressLint("MissingPermission")
        @JavascriptInterface
        fun getAuth(): String {
            val jsonObj = JSONObject()

            jsonObj.put(
                "token",
                "Bearer dhkjasfhajkshdbkahsjljgfnaskhdjhasbhjkfbndsakhjbfsdnfhjadjbnfhjsbfankjdsbnfjakdhsnka"
            )
            return jsonObj.toString()
        }
    }


}