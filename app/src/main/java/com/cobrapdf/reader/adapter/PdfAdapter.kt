package com.cobrapdf.reader.adapter

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.ListAdapter
import com.cobrapdf.reader.activity.ViewPdfActivity
import com.cobrapdf.reader.dialog.DetailsDialog
import com.cobrapdf.reader.model.Pdf
import com.cobrapdf.reader.viewmodel.PdfViewModel

import java.util.*
import com.cobrapdf.reader.activity.MainActivity
import com.cobrapdf.reader.bootomsheets.showBottomSheet
import com.cobrapdf.reader.databinding.PdfListItemBinding
import com.cobrapdf.reader.extension.Date.getDate
import com.cobrapdf.reader.interfaces.BottomSheetInterface
import com.cobrapdf.reader.utils.*
import com.cobrapdf.reader.viewholder.PdfViewHolder


class PdfAdapter(
    private val activity: Activity?,
    private val viewModel: PdfViewModel
) :
    ListAdapter<Pdf, PdfViewHolder>(ListAdapterCallBack), Filterable,BottomSheetInterface {

    private var pdfList: Pdf? = null
    private val resultsModel: MutableList<Pdf>? = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
       val binding = PdfListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PdfViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        pdfList = getItem(position)
        resultsModel?.addAll(listOf(getItem(position)))

        with(holder){
            with(getItem(position)){
                binding?.tvTitle?.text = title
                binding?.tvDate?.text = getDate(Date(path?.getFile()?.lastModified()!!))
                binding?.tvSize?.text = size?.getFileSize()
                binding?.cardLayout?.setOnClickListener {
                    ViewPdfActivity.start(activity, this)
                }
                binding?.bottomSheet?.setOnClickListener {
                    activity?.showBottomSheet(this@PdfAdapter,this,viewModel)
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



    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val filterResults = FilterResults()
                if (constraint?.isEmpty()!!) {
                    filterResults.count = resultsModel?.size!!
                    filterResults.values = resultsModel
                } else {
                    val resultsModel: MutableList<Pdf>? = ArrayList()
                    val searchStr = constraint.toString().lowercase()
                    val searchStr1 = constraint.toString().uppercase()
                    for (itemsModel in currentList) {
                        if (itemsModel.title.contains(searchStr) || itemsModel.title.contains(
                                searchStr1
                            )
                        ) {
                            resultsModel?.add(itemsModel)
                        }
                        filterResults.count = resultsModel?.size!!
                        filterResults.values = resultsModel
                    }
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults?) {
                submitList(results?.values as List<Pdf>?)
            }
        }
    }

    override fun onItemClick(string: String?) {
        when(string){
            BS_DETAIL_KEY->{
                val activity = activity as MainActivity
                DetailsDialog.newInstance(pdfList).show(activity.supportFragmentManager,"")
            }

            BS_SHARE_KEY->{
                sharePdf(activity, Uri.parse(pdfList?.uri))
            }
        }
    }
}
