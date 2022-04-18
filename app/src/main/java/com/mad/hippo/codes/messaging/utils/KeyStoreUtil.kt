package com.mad.hippo.codes.messaging.utils

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Base64.decode
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.*
import java.security.spec.KeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.security.auth.x500.X500Principal


private const val TAG = "KeyStoreUtil"
object KeyStoreUtil {

    private const val KEY_ALIAS =
        "com.mad.hippo.codes.supchat.android.security.publickeycryptography.key"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val TYPE_RSA = "RSA"
    private const val CYPHER = "RSA/ECB/PKCS1Padding"


    @RequiresApi(Build.VERSION_CODES.M)
    fun encryptString(toEncrypt: String, publicKey : String): String? {
        try {
            if (publicKey != null) {
                val keyFac = KeyFactory.getInstance(TYPE_RSA)
                val keySpec: KeySpec = X509EncodedKeySpec(decode(publicKey.trim { it <= ' ' }
                    .toByteArray(), Base64.DEFAULT))
                val key: Key = keyFac.generatePublic(keySpec)

                // Encrypt the text
                val input = Cipher.getInstance(CYPHER)
                input.init(Cipher.ENCRYPT_MODE, key)
                val outputStream = ByteArrayOutputStream()
                val cipherOutputStream = CipherOutputStream(
                    outputStream, input)
                cipherOutputStream.write(toEncrypt.toByteArray(charset = Charset.defaultCharset()))
                cipherOutputStream.close()
                val vals: ByteArray = outputStream.toByteArray()
                return Base64.encodeToString(vals, Base64.DEFAULT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun decryptString(encrypted: String): String {
        try {
            val privateKeyEntry = getPrivateKey()
            if (privateKeyEntry != null) {
                val privateKey = privateKeyEntry.privateKey
                val output = Cipher.getInstance(CYPHER)
                output.init(Cipher.DECRYPT_MODE, privateKey)
                val cipherInputStream = CipherInputStream(
                    ByteArrayInputStream(decode(encrypted, Base64.DEFAULT)), output)
                val values: ArrayList<Byte> = ArrayList()
                var nextByte: Int
                while (cipherInputStream.read().also { nextByte = it } != -1) {
                    values.add(nextByte.toByte())
                }
                val bytes = ByteArray(values.size)
                for (i in bytes.indices) {
                    bytes[i] = values[i]
                }
                return String(bytes, 0, bytes.size, Charset.defaultCharset())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getPublicKeyAsString(): String{
        val privateKeyEntry = getPrivateKey()
        if (privateKeyEntry != null) {
            val publicKey = Base64.encode(privateKeyEntry.certificate.publicKey.encoded, Base64.DEFAULT)
            return String(publicKey)
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getPrivateKey(): KeyStore.PrivateKeyEntry? {
        var ks = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
            load(null)
        }

        var entry = ks.getEntry(KEY_ALIAS, null)

         if (entry == null) {
             Log.d(TAG, "getPrivateKey: ")
            try {
                createKeys()
                ks = KeyStore.getInstance(ANDROID_KEYSTORE)

                ks.load(null)
                entry = ks.getEntry(KEY_ALIAS, null)
                if (entry == null) {
                    return null
                }
            } catch (e: NoSuchProviderException) {
                e.printStackTrace()
                return null
            } catch (e: InvalidAlgorithmParameterException) {
                e.printStackTrace()
                return null
            }
        }

         if (entry !is KeyStore.PrivateKeyEntry) {
            return null
        }
        return entry
    }

    /**
     * Creates a public and private key and stores it using the Android Key Store, so that only
     * this application will be able to access the keys.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun createKeys() {
        val spec = KeyGenParameterSpec.Builder(KEY_ALIAS,
                KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY).run {
            setCertificateSerialNumber(BigInteger.valueOf(1337))
            setCertificateSubject(X500Principal("CN=$KEY_ALIAS"))
            build()
        }
        val kpGenerator: KeyPairGenerator =
            KeyPairGenerator.getInstance(TYPE_RSA,
                ANDROID_KEYSTORE)
        kpGenerator.initialize(spec)
        kpGenerator.generateKeyPair()
    }
}