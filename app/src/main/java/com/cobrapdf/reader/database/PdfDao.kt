package com.cobrapdf.reader.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cobrapdf.reader.model.Pdf

@Dao
interface PdfDao {

    companion object {
        private var INSTANCE: PdfDao? = null
        fun getInstance(context: Context): PdfDao {
            return INSTANCE ?: Room
                .databaseBuilder(context.applicationContext, PdfDataBase::class.java, "pdf_db")
                .addMigrations(object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("ALTER TABLE Result ADD COLUMN name TEXT")
                    }
                }).allowMainThreadQueries()
                .build()
                .pdfDao().apply {
                    INSTANCE = this
                }
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(result: Pdf)

    @Update
    fun update(result: Pdf)

    @Delete
    fun delete(result: Pdf)

    @Query("delete from pdf")
    fun deleteAllPdf()

    @Query("select * from pdf ORDER BY time DESC")
    fun getAllPdf(): LiveData<List<Pdf>>

    @Query("SELECT * FROM pdf WHERE isFavourite = 1 ORDER BY id DESC")
    fun getBookmark(): LiveData<List<Pdf>>

    @Query("SELECT EXISTS (SELECT * FROM pdf WHERE id=:id AND isFavourite=1)")
    fun isBookmark(id: Long): Boolean

    @Query("select rememberPage from pdf WHERE id =:id")
    fun rememberPage(id: Long): Int
}