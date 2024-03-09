package com.cobrapdf.reader.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.cobrapdf.reader.databinding.PdfListItemBinding

class PdfViewHolder(private val pdfListItemBinding: PdfListItemBinding?) : RecyclerView.ViewHolder(
    pdfListItemBinding?.root!!
){
    val binding = pdfListItemBinding
}