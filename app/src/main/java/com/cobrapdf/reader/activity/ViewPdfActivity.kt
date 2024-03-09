package com.cobrapdf.reader.activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.cobrapdf.reader.R
import com.cobrapdf.reader.data.PrintDocumentAdapter
import com.cobrapdf.reader.databinding.ActivityViewPdfBinding
import com.cobrapdf.reader.dialog.DetailsDialog
import com.cobrapdf.reader.dialog.JumpPageDialog
import com.cobrapdf.reader.dialog.PasswordDialog
import com.cobrapdf.reader.model.Pdf
import com.cobrapdf.reader.preference.UserPreferences
import com.cobrapdf.reader.utils.*
import com.cobrapdf.reader.viewmodel.PdfViewModel
import com.github.barteksc.pdfviewer.listener.*
import com.shockwave.pdfium.PdfDocument
import java.io.File

class ViewPdfActivity : BaseActivity(), OnPageChangeListener, OnLoadCompleteListener,
    OnPageErrorListener, OnTapListener, JumpPageDialog.OnButtonClick,PasswordDialog.OnClickListener,OnErrorListener {

    companion object {
        fun start(context: Context?, pdf: Pdf?) {
            val intent = Intent(context, ViewPdfActivity::class.java).apply {
                putExtra(PDF_INTENT, pdf)
            }
            context?.startActivity(intent)
        }
    }

    private var password: String? = null
    private var totalPage: Int = 0
    private var pageNumber: Int? = 0
    private val binding by lazy { ActivityViewPdfBinding.inflate(layoutInflater) }

    private val viewModel by lazy {
        ViewModelProvider(this)[PdfViewModel::class.java]
    }
    private val pdf by lazy {
        intent?.getSerializableExtra(PDF_INTENT) as? Pdf
            ?: throw IllegalArgumentException("No pdf passed")
    }


    private val userPreferences by lazy {
        UserPreferences(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getAppTheme(applicationContext))
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tool.toolBar.title = pdf.title
        setSupportActionBar(binding.tool.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (userPreferences.rememberPage) {
            pageNumber = viewModel.rememberPage(pdf.id)
        }
        if (checkIfPdfIsPasswordProtected(Uri.parse(pdf.uri),contentResolver)) {
            PasswordDialog.newInstance(this,true).show(supportFragmentManager, "")
        }else {
            loadPdf()
        }

        pdf.isFavourite = viewModel.isFavourite(pdf.id)
        val copyPdf = Pdf(
            id = pdf.id,
            title = pdf.title,
            uri = pdf.uri,
            path = pdf.path,
            addDate = pdf.addDate,
            modifiedDate = pdf.modifiedDate,
            size = pdf.size,
            time = System.currentTimeMillis(),
            isFavourite = pdf.isFavourite
        )
        viewModel.insert(copyPdf)
        changeBookmark()

        binding.swipeLayout.setOnClickListener {
            swipePage()
        }

        binding.lightModeLayout.setOnClickListener {
            changeMode()
        }
        binding.pageCount.text = "${pageNumber?.plus(1)} / $totalPage"

        binding.jumpPageLayout.setOnClickListener {
            JumpPageDialog.newInstance(totalPage).show(supportFragmentManager, "")
        }
        binding.favouriteLayout.setOnClickListener {
            pdf.isFavourite = !viewModel.isFavourite(pdf.id)
            viewModel.insert(pdf)
            changeBookmark()
        }


    }




    private fun changeBookmark() {
        if (viewModel.isFavourite(pdf.id)) {
            binding.favourite.setImageResource(R.drawable.ic_favourite_black_24dp)
        } else {
            binding.favourite.setImageResource(R.drawable.ic_favourite_border_black_24dp)
        }
    }

    private fun loadPdf() {
        binding.pdfView.useBestQuality(userPreferences.quality)
        binding.pdfView.fromUri(Uri.parse(pdf.uri)).defaultPage(pageNumber!!)
            .password(password)
            .onError(this)
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
            binding.pdfView.fromUri(Uri.parse(pdf.uri)).defaultPage(pageNumber!!)
                .password(password)
                .onError(this)
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
        Log.d("Error", "PageError", t)
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
                print(pdf.path?.getFile())
            }
            R.id.menu_share -> {
                sharePdf(this, Uri.parse(pdf.uri))
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

    override fun onButtonClick(page: Int?) {
        binding.pdfView.jumpTo((page!! - 1), userPreferences.animation)

    }

    override fun onStop() {
        super.onStop()
        val copyPdf = Pdf(
            id = pdf.id,
            title = pdf.title,
            uri = pdf.uri,
            path = pdf.path,
            addDate = pdf.addDate,
            modifiedDate = pdf.modifiedDate,
            size = pdf.size,
            time = System.currentTimeMillis(),
            isFavourite = pdf.isFavourite,
            pageNumber
        )
        viewModel.insert(copyPdf)
    }

    override fun onClickListener(password: String?) {
        this.password = password
        loadPdf()
    }

    override fun onCancelListener() {
        finish()
    }

    override fun onError(t: Throwable?) {
        if (t.toString() == "com.shockwave.pdfium.PdfPasswordException: Password required or incorrect password.") {
            PasswordDialog.newInstance(this,false).show(supportFragmentManager, "")
        }

    }
}