package com.cobrapdf.reader.activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener
import com.github.barteksc.pdfviewer.listener.OnTapListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.cobrapdf.reader.R
import com.cobrapdf.reader.data.PrintDocumentAdapter
import com.cobrapdf.reader.databinding.ActivityViewPdfBinding
import com.cobrapdf.reader.dialog.DetailsDialog
import com.cobrapdf.reader.extension.getFilePathFromURI
import com.cobrapdf.reader.extension.getRealPathFromUriAPI19
import com.cobrapdf.reader.model.Pdf
import com.cobrapdf.reader.preference.UserPreferences
import com.cobrapdf.reader.utils.getAppTheme
import com.cobrapdf.reader.utils.getFile
import com.cobrapdf.reader.utils.sharePdf
import com.cobrapdf.reader.viewmodel.PdfViewModel
import com.shockwave.pdfium.PdfDocument
import java.io.File


class ViewPdfURIActivity : BaseActivity(), OnPageChangeListener, OnLoadCompleteListener,
    OnPageErrorListener, OnTapListener {


    private var totalPage: Int = 0
    private var pageNumber: Int? = 0
    private lateinit var binding: ActivityViewPdfBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[PdfViewModel::class.java]
    }

    private var pdf: Pdf? = null

    private val userPreferences by lazy {
        UserPreferences(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getAppTheme(applicationContext))
        super.onCreate(savedInstanceState)
        binding = ActivityViewPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uri: Uri = intent.data as Uri


       Log.d("asd", getRealPathFromUriAPI19(applicationContext, intent.data!!)!!)

       // binding.tool.toolBar.title = pdf?.title
        setSupportActionBar(binding.tool.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val  name = File(getFilePathFromURI(uri,applicationContext)!!).name
        val  path = getFilePathFromURI(uri,applicationContext)!!
        val  modified = File(getFilePathFromURI(uri,applicationContext)!!).lastModified()
        val  size = File(getFilePathFromURI(uri,applicationContext)!!).length()

      //  pdf = Pdf(null,name,path,null,modified,size,System.currentTimeMillis())
        loadPdf()

       // pdf?.isBookmark = viewModel.isBookmark(pdf?.id)
       /* val copyPdf = Pdf(
            id = pdf?.id,
            title = pdf?.title!!,
            path = pdf?.path,
            addDate = pdf?.addDate,
            modifiedDate = pdf?.modifiedDate,
            size = pdf?.size,
            time = System.currentTimeMillis(),
            isBookmark = pdf?.isBookmark!!
        )*/
       // viewModel.insert(copyPdf)
       // changeBookmark()

        binding.swipeLayout.setOnClickListener {
            swipePage()
        }

        binding.lightModeLayout.setOnClickListener {
            changeMode()
        }
        binding.pageCount.text = "${pageNumber?.plus(1)} / $totalPage"

        binding.jumpPageLayout.setOnClickListener {
            jumpPage()
        }
        binding.favourite.setOnClickListener {
            pdf?.isFavourite = !viewModel.isFavourite(pdf?.id)
            viewModel.insert(pdf)
            changeBookmark()
        }

    }


    private fun changeBookmark() {
        if (viewModel.isFavourite(pdf?.id)) {
            binding.favourite.setColorFilter(resources.getColor(R.color.app_default_color))
        } else {
            binding.favourite.setColorFilter(resources.getColor(R.color.black))
        }
    }

    private fun loadPdf() {
        binding.pdfView.useBestQuality(userPreferences.quality)
        binding.pdfView.fromFile(File(pdf?.path)).defaultPage(pageNumber!!)
            .onPageChange(this)
            .enableAnnotationRendering(true)
            .onLoad(this)
            .scrollHandle(DefaultScrollHandle(this))
            .onPageError(this)
            .onTap(this)
            .swipeHorizontal(false)
            .autoSpacing(true)
            .nightMode(binding.pdfView.isNightMode)
            .load()
    }

    private fun changeMode() {
        if (binding.pdfView.isNightMode) {
            binding.tvMode.text = getString(R.string.light_mode)
            binding.lightModeImage.setImageResource(R.drawable.ic_light_mode_black_24dp)
            binding.pdfView.setBackgroundColor(resources.getColor(R.color.white))
            binding.pdfView.isNightMode = false
        } else {
            binding.tvMode.text = getString(R.string.night_mode)
            binding.lightModeImage.setImageResource(R.drawable.ic_mode_night_black_24dp)
            binding.pdfView.setBackgroundColor(resources.getColor(R.color.black))
            binding.pdfView.isNightMode = true
        }
    }

    private fun swipePage() {
        if (binding.pdfView.isSwipeVertical) {
            binding.pdfView.useBestQuality(userPreferences.quality)
            binding.tvSwipe.text = getString(R.string.swipe_horizontal)
            binding.swipeImage.setImageResource(R.drawable.ic_swipe_left_black_24dp)
            binding.pdfView.fromFile((File(pdf?.path))).defaultPage(pageNumber!!)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(DefaultScrollHandle(this))
                .onPageError(this)
                .swipeHorizontal(true)
                .pageFling(true)
                .fitEachPage(true)
                .autoSpacing(true)
                .onTap(this)
                .nightMode(binding.pdfView.isNightMode)
                .load()
        } else {
            binding.tvSwipe.text = getString(R.string.swipe_vertical)
            binding.swipeImage.setImageResource(R.drawable.ic_swipe_up_black_24dp)
            loadPdf()
        }
    }

    override fun loadComplete(nbPages: Int) {
        val meta: PdfDocument.Meta = binding.pdfView.documentMeta
        Log.e(TAG, "title = " + meta.title)
        Log.e(TAG, "author = " + meta.author)
        Log.e(TAG, "subject = " + meta.subject)
        Log.e(TAG, "keywords = " + meta.keywords)
        Log.e(TAG, "creator = " + meta.creator)
        Log.e(TAG, "producer = " + meta.producer)
        Log.e(TAG, "creationDate = " + meta.creationDate)
        Log.e(TAG, "modDate = " + meta.modDate)
        printBookmarksTree(binding.pdfView.tableOfContents, "-")
    }

    private fun printBookmarksTree(tree: List<PdfDocument.Bookmark>, sep: String) {
        for (b in tree) {
            if (b.hasChildren()) {
                printBookmarksTree(b.children, "$sep-")
            }
        }
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page
        totalPage = pageCount
        binding.pageCount.text = "${pageNumber?.plus(1)} / $totalPage"
    }

    override fun onPageError(page: Int, t: Throwable?) {
        Log.e("Error", "PageError", t)
    }

    override fun onTap(e: MotionEvent?): Boolean {
        if (binding.pageCountLayout.visibility == View.VISIBLE) {
            binding.pageCountLayout.visibility = View.GONE
        } else {
            binding.pageCountLayout.visibility = View.VISIBLE
        }
        if (binding.bottomBar.visibility == View.VISIBLE) {
            binding.bottomBar.visibility = View.GONE
        } else {
            binding.bottomBar.visibility = View.VISIBLE
        }
        return true
    }


    private fun jumpPage() {
        var editText: EditText? = null
        val alertDialog = AlertDialog.Builder(this, R.style.DialogAlertTheme)
        alertDialog.setNegativeButton(
            getString(
                R.string.cancel
            )
        ) { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }
        alertDialog.setPositiveButton(
            getString(
                R.string.go
            )
        ) { dialog: DialogInterface, _: Int ->
            if ((editText?.text.toString()).isNullOrEmpty().not()) {
                binding.pdfView.jumpTo((Integer.parseInt(editText?.text.toString()) - 1), true)
                dialog.dismiss()
            }
        }
        val inflater = layoutInflater
        val convertView = inflater.inflate(R.layout.layout_jump_page, null)
        editText = convertView.findViewById<EditText>(R.id.editText)

        alertDialog.setView(convertView)
        alertDialog?.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.menu_search -> {

            }
            R.id.menu_detail -> {
                DetailsDialog.newInstance(pdf).show(supportFragmentManager, "")
            }
            R.id.menu_print -> {
                print(pdf?.path?.getFile())
            }
            R.id.menu_share -> {
                sharePdf(applicationContext, Uri.parse(pdf?.uri))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun print(file: File?) {
        val manager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        val adapter = PrintDocumentAdapter(file)
        val attributes = PrintAttributes.Builder().build()
        manager.print("Document", adapter, attributes)
    }
}