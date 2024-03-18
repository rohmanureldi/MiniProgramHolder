package com.example.miniprogramholder

import android.util.Base64
import android.util.Log
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val TAG = "ExampleUnitTest"
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    
    @Test
    fun testJson() {
        val jsonObj = JSONObject()

        jsonObj.put(
            "token",
            "Bearer dhkjasfhajkshdbkahsjljgfnaskhdjhasbhjkfbndsakhjbfsdnfhjadjbnfhjsbfankjdsbnfjakdhsnka"
        )
        Log.e(TAG, "testJson: $jsonObj", )
    }

    @Test
    fun encrypt() {
        val secretKey = SecretKeySpec(generateKey().encoded, "AES")
        Log.e(TAG, "encrypt: secretKey -> $secretKey", )

        encryptWithIvPrefix("Telkomsel", secretKey).also {
            Log.e(TAG, "encrypt: ENCRYPTED DATA -> $it")
        }
    }

    fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        val secretKey = keyGenerator.generateKey()
        return secretKey
    }

    fun encryptWithIvPrefix(data: String, key: SecretKeySpec): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivBytes = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        Log.e(TAG, "encryptWithIvPrefix: ivBytes -> $ivBytes")
        val ivSpec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
        val encrypted = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        val combinedIvAndEncrypted = ivBytes + encrypted
        return Base64.encodeToString(combinedIvAndEncrypted, Base64.DEFAULT)
    }
}
