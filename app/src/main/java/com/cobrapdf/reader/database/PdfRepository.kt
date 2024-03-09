package com.cobrapdf.reader.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.cobrapdf.reader.model.Pdf

class PdfRepository(application: Application) {

    private var allPdf: LiveData<List<Pdf>>? = null
    private var favourite: LiveData<List<Pdf>>? = null

    private val pdfDao = PdfDao?.getInstance(application)

    init {
        allPdf = pdfDao.getAllPdf()
        favourite = pdfDao.getBookmark()
    }

    fun insert(result: Pdf?) {

        pdfDao.insert(result!!)

    }

    fun delete(result: Pdf?) {

        pdfDao.delete(result!!)

    }


    fun getAllPdf(): LiveData<List<Pdf>>? {
        return allPdf!!
    }

    fun getFavourite(): LiveData<List<Pdf>> {
        return favourite!!
    }

    fun isFavourite(id: Long): Boolean{
        return pdfDao.isBookmark(id)
    }

    fun update(result: Pdf?) {

        pdfDao.update(result!!)

    }

    fun rememberPage(id: Long?): Int {
        return pdfDao.rememberPage(id!!)
    }


}