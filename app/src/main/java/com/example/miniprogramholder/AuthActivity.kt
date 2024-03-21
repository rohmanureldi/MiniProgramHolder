package com.example.miniprogramholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.miniprogramholder.databinding.ActivityBluetoothBinding
import org.json.JSONObject
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AuthActivity : AppCompatActivity() {

    private val TAG = "AuthActivity"
    private val webUrl = BuildConfig.DEVICE_IP_ADDRESS + "3011/"
    private val ivByte: ByteArray = "TelkomselJuara#1".toByteArray(Charset.defaultCharset())
    private val secretKey: ByteArray = "TelkomselACTION!".toByteArray()
    private lateinit var encryptedData: String

    private lateinit var binding: ActivityBluetoothBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWebView()

        encrypt("Telkomsel")
        decryptWithIvPrefix(this.encryptedData, SecretKeySpec(this.secretKey, "AES")).also {
            Log.e(TAG, "onCreate: decrypted -> $it")
        }
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
            val baseCustomUserAgentJson = JSONObject()
            val customUserAgentExtras = JSONObject()
            customUserAgentExtras.put("deviceType", "Android")
            baseCustomUserAgentJson.put("default", settings.userAgentString)
            baseCustomUserAgentJson.put("extra", customUserAgentExtras)
            settings.userAgentString = baseCustomUserAgentJson.toString()
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

    fun encrypt(dataToEncrypt: String): String {
        val secretKey = SecretKeySpec(secretKey, "AES")

        return encryptWithIvPrefix(dataToEncrypt, secretKey).also {
            println(TAG + " encrypt: ENCRYPTED DATA -> $it")
            encryptedData = it
        }
    }

    private fun decryptWithIvPrefix(encryptedDataWithIv: String, key: SecretKeySpec): String {
        val encryptedData = Base64.decode(encryptedDataWithIv, Base64.DEFAULT)
        val ivBytes = ivByte

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
        val decrypted = cipher.doFinal(encryptedData)
        return String(decrypted, Charsets.UTF_8)
    }

    private fun encryptWithIvPrefix(data: String, key: SecretKeySpec): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(ivByte)
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
        val encrypted = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        val combinedIvAndEncrypted = encrypted
        return Base64.encodeToString(combinedIvAndEncrypted, Base64.DEFAULT)
    }

    fun changeStatusBarColor(colorHexCode: String) {
        runCatching {
            runOnUiThread {
                val color = Color.parseColor(colorHexCode)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = color
            }
        }

    }

    fun setLightStatusBar(isLight: Boolean) {
        runCatching {
            runOnUiThread {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val windowInsetsController = window.insetsController
                    if (isLight) {
                        windowInsetsController?.setSystemBarsAppearance(
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        )
                    } else {
                        windowInsetsController?.setSystemBarsAppearance(
                            0,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        )
                    }
                } else {
                    window.decorView.apply {
                        systemUiVisibility = if (isLight) {
                            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        } else {
                            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                        }
                    }
                }
            }
        }
    }


    inner class WebAppInterface(
        private val context: Context,
        private val webView: WebView
    ) {

        @JavascriptInterface
        fun getAuth(): String {
            val jsonObj = JSONObject()
            val endcryptedData = encrypt(dataToEncrypt = "Telkomsel Super Mantaps")

            jsonObj.put(
                "token",
                endcryptedData
            )
            return jsonObj.toString()
        }

        @JavascriptInterface
        fun setStatusBarColorBackground(colorHexCode: String) {
            changeStatusBarColor(colorHexCode)
        }

        @JavascriptInterface
        fun isStatusBarLightTheme(isLight: Boolean) {
            setLightStatusBar(isLight)
        }
    }


}