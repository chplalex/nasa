package com.chplalex.nasa.ui.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import com.chplalex.nasa.R
import com.chplalex.nasa.mvp.view.IViewSettings
import com.chplalex.nasa.utils.*
import com.google.android.material.chip.ChipGroup
import moxy.MvpAppCompatFragment

class FragmentSettings : MvpAppCompatFragment(R.layout.fragment_settings), IViewSettings {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChipGroupTheme(view)
    }

    private fun initChipGroupTheme(view: View) {
        val chipGroupTheme = view.findViewById<ChipGroup>(R.id.settings_group_themes)
        val sp = requireActivity().getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE)

        val idTheme = sp.getInt(PREFS_THEME_ID, THEME_ID_SYSTEM)
        val idChecked = when (idTheme) {
            THEME_ID_SYSTEM -> R.id.settings_chip_theme_system
            THEME_ID_LIGHT -> R.id.settings_chip_theme_light
            THEME_ID_DARK -> R.id.settings_chip_theme_dark
            THEME_ID_CUSTOM -> R.id.settings_chip_theme_custom
            else -> R.id.settings_chip_theme_system
        }
        chipGroupTheme.check(idChecked)

        chipGroupTheme.setOnCheckedChangeListener { group, checkedId ->
            val themeId = when (checkedId) {
                R.id.settings_chip_theme_system -> THEME_ID_SYSTEM
                R.id.settings_chip_theme_light -> THEME_ID_LIGHT
                R.id.settings_chip_theme_dark -> THEME_ID_DARK
                R.id.settings_chip_theme_custom -> THEME_ID_CUSTOM
                else -> {
                    group.check(R.id.settings_chip_theme_system)
                    return@setOnCheckedChangeListener
                }
            }
            with(sp.edit()) {
                putInt(PREFS_THEME_ID, themeId)
                apply()
            }
            activity?.recreate()
        }
    }
}