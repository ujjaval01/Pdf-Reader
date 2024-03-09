package com.cobrapdf.reader.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.google.android.material.navigation.NavigationBarView
import com.cobrapdf.reader.R
import com.cobrapdf.reader.databinding.ActivityMainBinding
import com.cobrapdf.reader.fragment.FavouriteFragment
import com.cobrapdf.reader.fragment.MainFragment
import com.cobrapdf.reader.fragment.RecentFragment
import com.cobrapdf.reader.fragment.SettingsFragment
import com.cobrapdf.reader.utils.getAppTheme
import com.cobrapdf.reader.utils.loadFragment

class MainActivity : BaseActivity(), NavigationBarView.OnItemSelectedListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getAppTheme(applicationContext))
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.tool.toolBar)


        binding.bottomBar.setOnItemSelectedListener(this)
        loadMainFragment()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == binding.bottomBar.selectedItemId) {
            return false
        }
        loadFragment(item.itemId)
        return true
    }

    private fun loadFragment(itemId: Int) {
        when (itemId) {
            R.id.menu_home -> {
                loadMainFragment()
                binding.tool.toolBar.title = getString(R.string.app_name)
            }
            R.id.menu_recent -> {
                RecentFragment().loadFragment(this, R.id.container)
                binding.tool.toolBar.title = getString(R.string.recent)

            }
            R.id.menu_favourite -> {
                FavouriteFragment().loadFragment(this, R.id.container)
                binding.tool.toolBar.title = getString(R.string.favourite)

            }
            R.id.menu_settings -> {
                SettingsFragment().loadFragment(this, R.id.container)
                binding.tool.toolBar.title = getString(R.string.settings)

            }
        }
    }

    private fun loadMainFragment() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            MainFragment().loadFragment(this, R.id.container)
        } else {
            val intent = Intent(this, PermissionActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}