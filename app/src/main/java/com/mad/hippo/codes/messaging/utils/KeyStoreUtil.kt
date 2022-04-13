package com.mad.hippo.codes.messaging.utils

import android.content.Context
import android.util.Base64
import android.util.Base64.decode
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.*
import java.security.spec.KeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

private const val TAG = "KeyStoreUtil"
object KeyStoreUtil {

    fun generateKeys(context: Context) {
        // generate a new public/private key pair to test with (note. you should only do this once and keep them!)
        val kp = getKeyPair()
        val publicKey = kp!!.public
        val publicKeyBytes = publicKey.encoded
        val publicKeyBytesBase64 = String(Base64.encode(publicKeyBytes, Base64.DEFAULT))
        val privateKey = kp.private
        val privateKeyBytes = privateKey.encoded
        val privateKeyBytesBase64 = String(Base64.encode(privateKeyBytes, Base64.DEFAULT))

       val dataStoreManager = DataStoreManager(context.baseDataStore)
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreManager.editPreference(PRIVATE_KEY, privateKeyBytesBase64)
            dataStoreManager.editPreference(PUBLIC_KEY, publicKeyBytesBase64)
        }
    }

    fun localText(dataToEncrypt : String, publicKeyBytesBase64: String, privateKeyBytesBase64 :String){
        Log.d(TAG, "localText: $dataToEncrypt $publicKeyBytesBase64 $privateKeyBytesBase64}")
        val encrypted = encryptRSAToString(dataToEncrypt, publicKeyBytesBase64)
        Log.d(TAG, "localText: $encrypted")
        val decrypted = decryptRSAToString(encrypted, privateKeyBytesBase64)
        Log.d(TAG, "localText: $decrypted")

    }

    private fun getKeyPair(): KeyPair? {
        var kp: KeyPair? = null
        try {
            val kpg = KeyPairGenerator.getInstance("RSA")
            kpg.initialize(2048)
            kp = kpg.generateKeyPair()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return kp
    }

    fun encryptRSAToString(clearText: String, publicKey: String): String {
        var encryptedBase64 = ""
        try {
            val keyFac = KeyFactory.getInstance("RSA")
            val keySpec: KeySpec = X509EncodedKeySpec(decode(publicKey.trim { it <= ' ' }
                .toByteArray(), Base64.DEFAULT))
            val key: Key = keyFac.generatePublic(keySpec)

            // get an RSA cipher object and print the provider
            val cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING")
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val encryptedBytes = cipher.doFinal(clearText.toByteArray(charset("UTF-8")))
            encryptedBase64 = String(Base64.encode(encryptedBytes, Base64.DEFAULT))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return encryptedBase64.replace("(\\r|\\n)".toRegex(), "")
    }

    fun decryptRSAToString(encryptedBase64: String?, privateKey: String): String {
        Log.d(TAG, "decryptRSAToString: $privateKey")
        var decryptedString = ""
        try {
            val keyFac = KeyFactory.getInstance("RSA")
            val keySpec: KeySpec = PKCS8EncodedKeySpec(decode(privateKey.trim { it <= ' ' }
                .toByteArray(), Base64.DEFAULT))
            val key: Key = keyFac.generatePrivate(keySpec)

            // get an RSA cipher object and print the provider
            val cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING")
            // encrypt the plain text using the public key
            cipher.init(Cipher.DECRYPT_MODE, key)
            val encryptedBytes = decode(encryptedBase64, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            decryptedString = String(decryptedBytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return decryptedString
    }
}