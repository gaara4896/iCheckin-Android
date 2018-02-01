package my.com.icheckin.icheckin_android.utils.database

import android.content.Context
import ninja.sakib.pultusorm.core.PultusORM
import ninja.sakib.pultusorm.core.PultusORMCondition
import ninja.sakib.pultusorm.core.PultusORMUpdater

/**
 * Created by gaara on 1/29/18.
 */
object Database {

    private const val databaseName = "icheckin.db"

    fun <T : Any> insert(context: Context, row: T): Boolean {
        val pultusORM = PultusORM(databaseName, context.filesDir.absolutePath)
        val success = pultusORM.save(row)
        pultusORM.close()
        return success
    }

    fun <T : Any> query(context: Context, clazz: T, condition: PultusORMCondition = PultusORMCondition.Builder().build()): MutableList<T> {
        val pultusORM = PultusORM(databaseName, context.filesDir.absolutePath)
        val query = pultusORM.find(clazz, condition)
        pultusORM.close()
        return query as MutableList<T>
    }

    fun <T : Any> update(context: Context, clazz: T, updater: PultusORMUpdater): Boolean {
        val pultusORM = PultusORM(databaseName, context.filesDir.absolutePath)
        val success = pultusORM.update(clazz, updater)
        pultusORM.close()
        return success
    }

    fun <T : Any> delete(context: Context, clazz: T, condition: PultusORMCondition = PultusORMCondition.Builder().build()): Boolean {
        val pultusORM = PultusORM(databaseName, context.filesDir.absolutePath)
        val success = pultusORM.delete(clazz, condition)
        pultusORM.close()
        return success
    }

    fun <T : Any> drop(context: Context, clazz: T): Boolean {
        val pultusORM = PultusORM(databaseName, context.filesDir.absolutePath)
        val success = pultusORM.drop(clazz)
        pultusORM.close()
        return success
    }
}