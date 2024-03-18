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
import java.security.Key
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AuthActivity : AppCompatActivity() {

    private val TAG = "AuthActivity"
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

        encrypt("Telkomsel")
        decryptWithIvPrefix(this.encryptedData, SecretKeySpec(this.secretKey.encoded, "AES")).also {
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
            loadUrl(webUrl, mapOf("deviceType" to "Android"))
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

    private lateinit var ivByte: ByteArray
    private lateinit var secretKey: Key
    private lateinit var encryptedData: String

    fun encrypt(dataToEncrypt: String): String {
        val key = generateKey()
        val secretKey = SecretKeySpec(key.encoded, "AES")
        println(TAG + " secretKey -> ${key.encoded}")
        this.secretKey = key

        return encryptWithIvPrefix(dataToEncrypt, secretKey).also {
            println(TAG + " encrypt: ENCRYPTED DATA -> $it")
            encryptedData = it
        }
    }

    fun decryptWithIvPrefix(encryptedDataWithIv: String, key: SecretKeySpec): String {
        val combinedIvAndEncrypted = Base64.decode(encryptedDataWithIv, Base64.DEFAULT)
        val ivBytes = combinedIvAndEncrypted.copyOfRange(0, 16) // Extract IV (first 16 bytes)
        val encryptedData = combinedIvAndEncrypted.copyOfRange(16, combinedIvAndEncrypted.size) // The rest is encrypted data

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
        val decrypted = cipher.doFinal(encryptedData)
        return String(decrypted, Charsets.UTF_8)
    }

    fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        val secretKey = keyGenerator.generateKey()
        return secretKey
    }

    fun encryptWithIvPrefix(data: String, key: SecretKeySpec): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
//        val ivBytes = ByteArray(16).apply {
//            SecureRandom().nextBytes(this)
//        }
        val ivBytes = ByteArray(16).apply {
            SecureRandom().nextBytes(this)
        }

        val baseByteArray = "Eldi".toByteArray()

        val strByteArray = baseByteArray.toString()
        val strByteArray2 = String(baseByteArray, Charsets.UTF_8)

        val byteArrayResp = strByteArray.toByteArray(Charsets.UTF_8)
        val byteArrayResp2 = strByteArray2.toByteArray(Charsets.UTF_8)


        println("Original ByteArray: ${baseByteArray.contentToString()}")
        println("Converted String: $strByteArray")
        println("Converted String 2: $strByteArray")
        println("Converted ByteArray: ${byteArrayResp.contentToString()}")
        println("Converted ByteArray2 : ${byteArrayResp2.contentToString()}")







        this.ivByte = "TelkomselJuara#1".toByteArray(Charset.defaultCharset())
        val ivSpec = IvParameterSpec(ivByte)
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
        val encrypted = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        val combinedIvAndEncrypted = ivByte + encrypted
        return Base64.encodeToString(combinedIvAndEncrypted, Base64.DEFAULT)
    }




    inner class WebAppInterface(
        private val context: Context,
        private val webView: WebView
    ) {

        @SuppressLint("MissingPermission")
        @JavascriptInterface
        fun getAuth(data: String): String {
            val jsonObj = JSONObject()
            val endcryptedData = encrypt(dataToEncrypt = "Telkomsel Super")

            jsonObj.put(
                "token",
                "Bearer $endcryptedData"
            )
            return jsonObj.toString()
        }
    }


}