package com.gscapin.chattapp.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.gscapin.chattapp.R
import com.gscapin.chattapp.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var binding: FragmentSettingsBinding

    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsBinding.bind(view)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val defaultValue = 0


        binding.configToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.switchNightMode.isChecked = sharedPref?.getInt("nightMode", defaultValue) == 1

        setNightMode(sharedPref)


    }

    private fun setNightMode(sharedPref: SharedPreferences?) {
        binding.switchNightMode.setOnCheckedChangeListener { compoundButton, checked ->
            if (checked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPref!!.edit().putInt("nightMode", 1).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPref!!.edit().putInt("nightMode", 0).apply()
            }
        }
    }
}