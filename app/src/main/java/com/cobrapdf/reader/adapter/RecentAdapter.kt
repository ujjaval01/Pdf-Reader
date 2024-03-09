package com.cobrapdf.reader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.cobrapdf.reader.activity.ViewPdfActivity
import com.cobrapdf.reader.databinding.RecentListItemBinding
import com.cobrapdf.reader.extension.Date.getCurrentDate
import com.cobrapdf.reader.model.Pdf
import com.cobrapdf.reader.utils.getFileSize
import com.cobrapdf.reader.viewholder.RecentViewHolder
import com.cobrapdf.reader.viewmodel.PdfViewModel

import java.util.*


class RecentAdapter(
    private val context: Context?,
    private val viewModel: PdfViewModel
) :
    ListAdapter<Pdf, RecentViewHolder>(ListAdapterCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
       val binding = RecentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        val pdfList = getItem(position)

        with(holder) {
            with(pdfList) {
                binding?.tvTitle?.text = title
                binding?.tvDate?.text = getCurrentDate(Date(this?.time!!))
                binding?.tvSize?.text = size?.getFileSize()
                binding?.cardLayout?.setOnClickListener {
                    ViewPdfActivity.start(context, this)
                }

            }
        }


    }

    override fun getItemCount(): Int {
        return currentList?.size!!
    }

    override fun submitList(list: List<Pdf>?) {
        super.submitList(list?.let { ArrayList(it) })
    }


}
