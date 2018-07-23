package my.com.icheckin.icheckin_android.model.dao

import android.arch.persistence.room.*
import my.com.icheckin.icheckin_android.model.entity.Credential

/**
 * Created by gaara on 13/7/18.
 */
@Dao
interface CredentialDao {

    @Query("SELECT * from credential")
    fun allCredential(): MutableList<Credential>

    @Query("SELECT * from credential where username = :username")
    fun query(username: String): Credential?

    @Insert
    fun insert(credential: Credential)

    @Update
    fun update(credential: Credential)

    @Delete
    fun delete(credential: Credential)
}