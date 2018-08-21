package my.com.icheckin.icheckin_android.utils.storage

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import my.com.icheckin.icheckin_android.model.dao.CredentialDao
import my.com.icheckin.icheckin_android.model.entity.Credential

/**
 * Created by gaara on 2/9/18.
 */
@Database(entities = [Credential::class], version = 5)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "icheckin"
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .addMigrations(MIGRATION_2_3)
                        .addMigrations(MIGRATION_3_4)
                        .addMigrations(MIGRATION_4_5)
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

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'credential' ADD COLUMN 'name' TEXT")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE IF EXISTS 'student'")
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DELETE FROM 'credential'")
                database.execSQL("ALTER TABLE 'credential' ADD COLUMN 'source' TEXT")
            }
        }
    }

    abstract fun credentialDao(): CredentialDao
}