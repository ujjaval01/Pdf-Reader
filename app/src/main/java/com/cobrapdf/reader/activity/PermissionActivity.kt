package com.cobrapdf.reader.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cobrapdf.reader.R
import com.cobrapdf.reader.databinding.ActivityPermissionBinding
import com.cobrapdf.reader.preference.UserPreferences
import com.cobrapdf.reader.utils.getAppTheme


class PermissionActivity : BaseActivity() {

    private val binding by lazy { ActivityPermissionBinding.inflate(layoutInflater) }
    private val userPreferences by lazy {
        UserPreferences(this)
    }
    private val MY_CAMERA_REQUEST_CODE: Int = 1000


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getAppTheme(applicationContext))
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        window?.navigationBarColor = resources?.getColor(R.color.white)!!
        window?.statusBarColor = resources?.getColor(R.color.white)!!

        storagePermission()

        binding.tvAllow.setOnClickListener {
            storagePermission()
        }

        binding.tvOpenSettings.setOnClickListener {
            val intent =
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(intent, MY_CAMERA_REQUEST_CODE)
        }

        if (userPreferences.doNotAskAgain) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                binding.tvAllow?.visibility = View.GONE
                binding.liPermission?.visibility = View.VISIBLE
                binding.tvOpenSettings?.visibility = View.VISIBLE
                userPreferences.doNotAskAgain = true
            }
        }

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            MY_CAMERA_REQUEST_CODE
        )
    }

    private fun storagePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startMainActivity()
        } else {
            requestPermission()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){

            MY_CAMERA_REQUEST_CODE->{
                storagePermission()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        if (grantResults != null && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startMainActivity()
        }else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                binding.tvAllow?.visibility = View.GONE
                binding.liPermission?.visibility = View.VISIBLE
                binding.tvOpenSettings?.visibility = View.VISIBLE
                userPreferences.doNotAskAgain = true
            }
            Toast.makeText(this, "storage permission denied", Toast.LENGTH_LONG).show()
        }
    }


    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}