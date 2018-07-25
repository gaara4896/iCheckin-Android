package my.com.icheckin.icheckin_android.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

/**
 * Created by gaara on 13/7/18.
 */
@Entity
class Credential(@ColumnInfo val username: String, @ColumnInfo val deviceId: String, @ColumnInfo var name: String?) : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}