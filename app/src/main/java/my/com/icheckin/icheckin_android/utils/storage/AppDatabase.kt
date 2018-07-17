package my.com.icheckin.icheckin_android.utils.storage

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import my.com.icheckin.icheckin_android.model.dao.CredentialDao
import my.com.icheckin.icheckin_android.model.dao.StudentDao
import my.com.icheckin.icheckin_android.model.entity.Credential
import my.com.icheckin.icheckin_android.model.entity.Student

/**
 * Created by gaara on 2/9/18.
 */
@Database(entities = [Student::class, Credential::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "icheckin"
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                val callback = object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                }
                INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .addCallback(callback)
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE!!
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS 'credential'" +
                        "('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "'username' TEXT NOT NULL, " +
                        "'deviceId' TEXT NOT NULL)")
            }
        }
    }

    abstract fun studentDao(): StudentDao

    abstract fun credentialDao(): CredentialDao
}