package com.cobrapdf.reader.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import com.cobrapdf.reader.R
import com.cobrapdf.reader.activity.MainActivity
import com.cobrapdf.reader.activity.ViewPdfActivity
import com.cobrapdf.reader.databinding.PdfListItemBinding
import com.cobrapdf.reader.dialog.DetailsDialog
import com.cobrapdf.reader.extension.Date.getCurrentDate
import com.cobrapdf.reader.model.Pdf
import com.cobrapdf.reader.utils.getFile
import com.cobrapdf.reader.utils.getFileSize
import com.cobrapdf.reader.utils.sharePdf
import com.cobrapdf.reader.viewholder.FavouriteViewHolder
import com.cobrapdf.reader.viewmodel.PdfViewModel

import java.util.*


class FavouriteAdapter(
    private val context: Context?,
    private val viewModel: PdfViewModel
) :
    ListAdapter<Pdf, FavouriteViewHolder>(ListAdapterCallBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
       val binding = PdfListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val pdfList = getItem(position)

        with(holder){
            with(pdfList){
                binding?.tvTitle?.text = title
                binding?.tvDate?.text =
                    getCurrentDate(Date(this?.time!!))
                binding?.tvSize?.text = size?.getFileSize()
                binding?.cardLayout?.setOnClickListener {
                    ViewPdfActivity.start(context, this)
                }
                binding?.bottomSheet?.setOnClickListener {
                    val popupMenu = PopupMenu(
                        context!!, it
                    )
                    popupMenu.inflate(R.menu.popupmenu)
                    popupMenu.show()

                    popupMenu.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.menu_share -> {
                                sharePdf(context, Uri.parse(pdfList?.uri))
                            }

                            R.id.menu_detail -> {
                                val activity = context as MainActivity
                                DetailsDialog.newInstance(pdfList).show(activity.supportFragmentManager,"")
                            }
                        }
                        true
                    }
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
