package my.com.icheckin.icheckin_android.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.Context
import my.com.icheckin.icheckin_android.utils.security.Cryptography
import java.io.Serializable

/**
 * Created by gaara on 2/3/18.
 */
@Entity
class Student : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo
    var username: String? = null

    @ColumnInfo(name = "password")
    var _password: String? = null

    fun init(context: Context, username: String, password: String) {
        this.username = username
        password(context, password)
    }

    fun password(context: Context, password: String) {
        this._password = Cryptography.encryptAES(context, password)
    }

    fun password(context: Context): String {
        return Cryptography.decryptAES(context, this._password!!)
    }
}