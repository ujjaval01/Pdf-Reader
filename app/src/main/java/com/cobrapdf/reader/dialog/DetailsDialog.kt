package com.cobrapdf.reader.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cobrapdf.reader.R
import com.cobrapdf.reader.databinding.DetailsDialogBinding
import com.cobrapdf.reader.extension.Date.getDate
import com.cobrapdf.reader.model.Pdf
import com.cobrapdf.reader.utils.getFile
import com.cobrapdf.reader.utils.getFileSize
import java.util.*

class DetailsDialog: DialogFragment() {
    companion object {
        private const val PDF_ID_KEY = "PDF_ID_KEY"

        fun newInstance(pdf: Pdf?): DetailsDialog {
            return DetailsDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(PDF_ID_KEY, pdf)
                }
                isCancelable = true
            }
        }
    }

    private var pdf: Pdf? = null

    private val binding by lazy { DetailsDialogBinding.inflate(LayoutInflater.from(context)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pdf = it.getSerializable(PDF_ID_KEY) as Pdf?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        return AlertDialog.Builder(requireActivity(), R.style.DialogAlertTheme)
            .setView(binding.root)
            .setPositiveButton(R.string.ok) { _, _ ->
                dismiss()
            }
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.text = pdf?.title
        binding.tvDate.text = getDate(Date(pdf?.path?.getFile()?.lastModified()!!))
        binding.tvSize.text = pdf?.size?.getFileSize()
        binding.tvPath.text = pdf?.path
    }


}