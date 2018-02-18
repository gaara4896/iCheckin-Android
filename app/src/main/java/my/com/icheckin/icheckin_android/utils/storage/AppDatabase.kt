package my.com.icheckin.icheckin_android.utils.storage

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import kotlinx.coroutines.experimental.async
import my.com.icheckin.icheckin_android.model.dao.StudentDao
import my.com.icheckin.icheckin_android.model.entity.Student

/**
 * Created by gaara on 2/9/18.
 */
@Database(entities = [(Student::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private val DATABASE_NAME = "icheckin"
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                val callback = object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        async {
                            val students = my.com.icheckin.icheckin_android.utils.storage.Database.query(
                                    context,
                                    my.com.icheckin.icheckin_android.model.Student())
                            val dao = getDatabase(context).studentDao()
                            for (student in students) {
                                val studentNew = Student()
                                studentNew.init(context, student.username!!, student.password!!)
                                dao.insert(studentNew)
                            }
                            my.com.icheckin.icheckin_android.utils.storage.Database.drop(
                                    context,
                                    my.com.icheckin.icheckin_android.model.Student())
                        }
                    }
                }
                INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DATABASE_NAME)
                        .addCallback(callback)
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE!!
        }
    }

    abstract fun studentDao(): StudentDao
}