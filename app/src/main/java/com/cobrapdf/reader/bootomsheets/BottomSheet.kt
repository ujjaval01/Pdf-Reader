package com.cobrapdf.reader.bootomsheets

import android.app.Activity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.cobrapdf.reader.R
import com.cobrapdf.reader.databinding.BottomSheetDialogBinding
import com.cobrapdf.reader.interfaces.BottomSheetInterface
import com.cobrapdf.reader.model.Pdf
import com.cobrapdf.reader.utils.BS_DETAIL_KEY
import com.cobrapdf.reader.utils.BS_SHARE_KEY
import com.cobrapdf.reader.viewmodel.PdfViewModel


fun Activity.showBottomSheet(listener: BottomSheetInterface, pdf: Pdf?, viewModel: PdfViewModel?) {

    val binding by lazy { BottomSheetDialogBinding.inflate(layoutInflater) }
    val dialog: BottomSheetDialog = createBottomSheet()
    dialog.setContentView(binding.root)
    dialog.show()

    pdf?.isFavourite = viewModel?.isFavourite(pdf?.id)!!
    if (viewModel.isFavourite(pdf?.id)) {
        binding.imageFavourite.setImageResource(R.drawable.ic_favourite_black_24dp)
        binding.tvFavourite.text = getString(R.string.un_favourite)
    } else {
        binding.imageFavourite.setImageResource(R.drawable.ic_favourite_border_black_24dp)
        binding.tvFavourite.text = getString(R.string.favourite)
    }

    binding.detailLayout.setOnClickListener {
        listener.onItemClick(BS_DETAIL_KEY)
        dialog.dismiss()
    }

    binding.shareLayout.setOnClickListener {
        listener.onItemClick(BS_SHARE_KEY)
        dialog.dismiss()
    }

    binding.favouriteLayout.setOnClickListener {
        pdf?.isFavourite = !viewModel.isFavourite(pdf?.id)
        viewModel.insert(pdf)
        if (viewModel.isFavourite(pdf?.id)) {
            binding.imageFavourite.setImageResource(R.drawable.ic_favourite_black_24dp)
            binding.tvFavourite.text = getString(R.string.un_favourite)
        } else {
            binding.imageFavourite.setImageResource(R.drawable.ic_favourite_border_black_24dp)
            binding.tvFavourite.text = getString(R.string.favourite)
        }
        dialog.dismiss()
    }

}


fun Activity.createBottomSheet(): BottomSheetDialog {
    return BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
}