package com.cobrapdf.reader.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.cobrapdf.reader.model.Pdf

object ListAdapterCallBack : DiffUtil.ItemCallback<Pdf>() {
    override fun areItemsTheSame(oldItem: Pdf, newItem: Pdf): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Pdf, newItem: Pdf): Boolean {
        return oldItem == newItem
    }
}