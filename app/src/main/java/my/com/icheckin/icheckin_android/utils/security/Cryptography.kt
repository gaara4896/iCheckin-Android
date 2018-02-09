package my.com.icheckin.icheckin_android.utils.security

import android.content.Context
import com.afollestad.androidsecurestorage.RxSecureStorage

/**
 * Created by gaara on 2/2/18.
 */
object Cryptography {

    private const val ALIAS_NAME = "icheckin"

    fun encryptAES(context: Context, payload: String): String {
        val secureStorage = RxSecureStorage.create(context, ALIAS_NAME)
        return secureStorage.encryptString(payload).blockingGet()
    }

    fun decryptAES(context: Context, encryptedString: String): String {
        val secureStorage = RxSecureStorage.create(context, ALIAS_NAME)
        return secureStorage.decryptString(encryptedString).blockingGet()
    }

    fun secretKey(context: Context, key: String, payload: String) {
        val secureStorage = RxSecureStorage.create(context, ALIAS_NAME)
        secureStorage.putString(key, payload).subscribe()
    }

    fun secretKey(context: Context, key: String): String {
        val secureStorage = RxSecureStorage.create(context, ALIAS_NAME)
        return secureStorage.getString(key).blockingLast()
    }
}