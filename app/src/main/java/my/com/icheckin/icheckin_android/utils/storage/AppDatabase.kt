package my.com.icheckin.icheckin_android.utils.storage

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import my.com.icheckin.icheckin_android.model.dao.StudentDao
import my.com.icheckin.icheckin_android.model.entity.Student

/**
 * Created by gaara on 2/9/18.
 */
@Database(entities = arrayOf(Student::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private val DATABASE_NAME = "icheckin"
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE!!
        }
    }

    abstract fun studenDao(): StudentDao
}