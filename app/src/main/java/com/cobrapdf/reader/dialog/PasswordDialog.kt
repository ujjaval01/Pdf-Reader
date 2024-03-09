package com.cobrapdf.reader.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cobrapdf.reader.R
import com.cobrapdf.reader.databinding.LayoutPasswordBinding
import com.github.barteksc.pdfviewer.listener.OnErrorListener

class PasswordDialog : DialogFragment() {

    interface OnClickListener {
        fun onClickListener(password: String?)
        fun onCancelListener()
    }

    companion object {
        private const val URI_KEY = "URI_KEY"

        fun newInstance(listener: OnClickListener,boolean: Boolean): PasswordDialog {
            return PasswordDialog().apply {
                this.onClickListener = listener
                this.password = boolean
                isCancelable = false
            }
        }
    }

    private val binding by lazy { LayoutPasswordBinding.inflate(LayoutInflater.from(context)) }

    private var onClickListener: OnClickListener? = null
    private var password : Boolean = false

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

        if (!password){
            binding.editText.error = resources.getString(R.string.invalid_password)
        }
        binding.tvOk.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener?.onClickListener(
                        (binding.editText.text.toString())
                    )
            }
            dismiss()
        }

        binding.tvCancel.setOnClickListener {
            onClickListener?.onCancelListener()
            dismiss()
        }

    }

}