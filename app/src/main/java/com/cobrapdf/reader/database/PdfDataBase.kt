package com.cobrapdf.reader.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cobrapdf.reader.model.Pdf

@Database(entities = [Pdf::class], version = 1)
abstract class PdfDataBase:  RoomDatabase() {
    abstract fun pdfDao():  PdfDao
}