package com.mis.route.todo.ui.home.fragments.settings

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.mis.route.todo.Constants
import com.mis.route.todo.R
import com.mis.route.todo.databinding.FragmentSettingsBinding
import java.util.Locale


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupExposedDropdownMenus()

        binding.modeInput.doOnTextChanged { text, _, _, _ ->
            activateDarkMode(when (text.toString()) {
                resources.getString(R.string.dark_mode) -> true
                else -> false
            })
        }

        binding.languageInput.doOnTextChanged { text, _, _, _ ->
            activateArabicLang(when (text.toString()) {
                resources.getString(R.string.arabic_language) -> true
                else -> false
            })
        }
    }

    private fun activateArabicLang(activate: Boolean) {
        setLocale(if (activate) Constants.ARABIC_CODE else Constants.ENGLISH_CODE)
        activity?.let { recreate(it) }
    }

    private fun activateDarkMode(activate: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (activate) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun populateMenusWithOptions() {
        binding.languageInput.setText(
            when (isArabicLangActive()) {
                true -> resources.getString(R.string.arabic_language)
                false -> resources.getString(R.string.english_language)
            }, false)

        binding.modeInput.setText(
            when (isDarkModeActive()) {
                true -> resources.getString(R.string.dark_mode)
                false -> resources.getString(R.string.light_mode)
            }, false)
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        activity?.let {
            @Suppress("DEPRECATION")
            requireActivity().baseContext.resources.updateConfiguration(
                config,
                requireActivity().baseContext.resources.displayMetrics
            )
        }
    }

    private fun isDarkModeActive(): Boolean {
        return when (
            context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun isArabicLangActive(): Boolean {
        return when (context?.resources?.configuration?.locales!![0].language) {
            Constants.ARABIC_CODE -> true
            else -> false
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupExposedDropdownMenus() {
        // setup
        val langItems = listOf(
            resources.getString(R.string.english_language),
            resources.getString(R.string.arabic_language))
        val lanAdapter = ArrayAdapter(requireContext(), R.layout.item_settings_dropdown_menus, langItems)
        binding.languageInput.setAdapter(lanAdapter)

        val modeItems = listOf(
            resources.getString(R.string.light_mode),
            resources.getString(R.string.dark_mode))
        val modeAdapter = ArrayAdapter(requireContext(), R.layout.item_task_status_dropdown_menu, modeItems)
        binding.modeInput.setAdapter(modeAdapter)

        // populate
        populateMenusWithOptions()
    }
}