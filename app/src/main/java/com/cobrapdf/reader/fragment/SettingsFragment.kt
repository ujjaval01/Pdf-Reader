package com.cobrapdf.reader.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cobrapdf.reader.BuildConfig
import com.cobrapdf.reader.R
import com.cobrapdf.reader.activity.MainActivity
import com.cobrapdf.reader.databinding.FragmentSettingsBinding
import com.cobrapdf.reader.preference.UserPreferences
import com.cobrapdf.reader.utils.gotoGooglePlay
import com.cobrapdf.reader.utils.shareApp
import com.cobrapdf.reader.utils.submitFeedback


class SettingsFragment : Fragment() {

    private val userPreferences by lazy {
        UserPreferences(requireContext())
    }
    private val binding by lazy { FragmentSettingsBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(userPreferences.theme){
            0 ->  {
                binding.scDarkMode.isChecked = false
                binding.tvDrakMode.text = getString(R.string.off)
            }
            1 -> {
                binding.scDarkMode.isChecked = true
                binding.tvDrakMode.text = getString(R.string.on)
            }
        }
        binding.scDarkMode.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                userPreferences.theme = 1
                binding.tvDrakMode.text = getString(R.string.on)
            }else {
                userPreferences.theme = 0
                binding.tvDrakMode.text = getString(R.string.off)
            }
            restartMainActivity()
        }

        binding.scQuality.isChecked = userPreferences.quality
        binding.scQuality.setOnCheckedChangeListener { buttonView, isChecked ->
            userPreferences.quality = isChecked

        }
        binding.scAnimation.isChecked = userPreferences.animation
        binding.scAnimation.setOnCheckedChangeListener { buttonView, isChecked ->
            userPreferences.animation = isChecked

        }
        binding.scRememberPage.isChecked = userPreferences.rememberPage
        binding.scRememberPage.setOnCheckedChangeListener { buttonView, isChecked ->
            userPreferences.rememberPage = isChecked

        }

        binding.feedbackLayout.setOnClickListener {
            submitFeedback(requireContext())
        }

        binding.shareLayout.setOnClickListener {
            shareApp(requireContext())
        }

        binding.rateUsLayout.setOnClickListener {
            gotoGooglePlay(requireContext(),requireContext().packageName)

        }

        binding.checkUpdateLayout.setOnClickListener {
            gotoGooglePlay(requireContext(),requireContext().packageName)
        }

        binding.tvVersion.text = "Version : ${BuildConfig.VERSION_NAME}"
    }

    private fun restartMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}