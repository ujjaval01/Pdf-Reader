package com.cobrapdf.reader.data

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.cobrapdf.reader.model.Pdf

class GetPdfFromURI(private val context: Context?,private val string: String?) {


    fun getPdfList(): ArrayList<Pdf> {
        val pdfList: ArrayList<Pdf> = ArrayList()
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.SIZE
        )
        val collection: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Files.getContentUri("external")
        }
        val mimeType: String = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")!!
        val selectionArgs = arrayOf(mimeType)
        val uri = Uri.parse(string)
            context?.contentResolver?.query(uri, projection, null, null, null)
            .use { cursor ->
                if (cursor != null) {
                    while (cursor.moveToFirst()) {
                        val columnId = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
                        val columnAddDate =
                            cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
                        val columnModifiedDate =
                            cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)
                        val columnName =
                            cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
                        val columnTitle = cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE)
                        val columnData = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                        val columnSize = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE)

                        pdfList.add(
                            Pdf(
                                cursor.getLong(columnId),
                                cursor.getString(columnTitle),
                                "",
                                cursor.getString(columnData),
                                cursor.getLong(columnAddDate),
                                cursor.getLong(columnModifiedDate),
                                cursor.getLong(columnSize),
                                System.currentTimeMillis()
                            )
                        )


                    }
                    cursor.close()
                }
            }

        return pdfList
    }
}