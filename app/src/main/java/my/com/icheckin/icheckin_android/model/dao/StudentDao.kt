package my.com.icheckin.icheckin_android.model.dao

import android.arch.persistence.room.*
import my.com.icheckin.icheckin_android.model.entity.Student

/**
 * Created by gaara on 2/3/18.
 */
@Dao
interface StudentDao {

    @Query("SELECT * from student")
    fun allStudent(): MutableList<Student>

    @Query("SELECT * from student where username = :username")
    fun query(username: String): Student?

    @Insert
    fun insert(student: Student)

    @Update
    fun update(student: Student)

    @Delete
    fun delete(student: Student)
}