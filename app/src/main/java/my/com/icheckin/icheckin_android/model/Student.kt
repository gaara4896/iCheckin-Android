package my.com.icheckin.icheckin_android.model

import ninja.sakib.pultusorm.annotations.AutoIncrement
import ninja.sakib.pultusorm.annotations.PrimaryKey

/**
 * Created by gaara on 1/29/18.
 */
class Student {
    @PrimaryKey
    @AutoIncrement
    var id: Int = 0
    var username: String? = null
    var password: String? = null

    fun init(username: String, password: String) {
        this.username = username
        this.password = password
    }
}