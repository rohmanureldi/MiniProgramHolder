package com.example.miniprogramholder

import android.annotation.SuppressLint
import android.content.Context
import android.net.http.SslError
import android.os.Bundle
import android.util.Base64
import android.util.Log
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


    inner class WebAppInterface(
        private val context: Context,
        private val webView: WebView
    ) {

        @SuppressLint("MissingPermission")
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
    }


}