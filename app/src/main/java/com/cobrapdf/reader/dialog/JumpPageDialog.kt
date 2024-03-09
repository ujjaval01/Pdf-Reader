package com.cobrapdf.reader.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cobrapdf.reader.R
import com.cobrapdf.reader.databinding.LayoutJumpPageBinding
import java.util.*

class JumpPageDialog : DialogFragment() {

    interface OnButtonClick {
        fun onButtonClick(page: Int?)
    }

    companion object {
        private const val PAGE_ID_KEY = "PAGE_ID_KEY"

        fun newInstance(page: Int?): JumpPageDialog {
            return JumpPageDialog().apply {
                arguments = Bundle().apply {
                    putInt(PAGE_ID_KEY, page!!)
                }
                isCancelable = true
            }
        }
    }

    private var page: Int? = null

    private val binding by lazy { LayoutJumpPageBinding.inflate(LayoutInflater.from(context)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page = it.getInt(PAGE_ID_KEY)
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
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvGo.setOnClickListener {
            if ((binding.editText.text.toString()).isEmpty() || page!! < Integer.parseInt(binding?.editText?.text.toString())) {
                binding.editText.error = resources.getString(R.string.invalid_page)
            } else {
                if (activity is OnButtonClick) (activity as OnButtonClick).onButtonClick(
                    Integer.parseInt(binding.editText.text.toString())
                )
                dismiss()
            }
        }

        binding.tvCancel.setOnClickListener {
            dismiss()
        }

    }
}