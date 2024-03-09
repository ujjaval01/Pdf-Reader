package com.cobrapdf.reader.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "pdf")
data class Pdf(
    @PrimaryKey(autoGenerate = false)
    val id: Long?,
    val title: String,
    val uri: String,
    val path: String?,
    val addDate: Long?,
    val modifiedDate: Long?,
    val size: Long?,
    val time:Long?,
    var isFavourite: Boolean = false,
    var rememberPage: Int? = 0
): Serializable

