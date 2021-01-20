package com.chplalex.nasa.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chplalex.nasa.R
import com.chplalex.nasa.mvp.view.IViewSettings
import moxy.MvpAppCompatFragment

class FragmentSettings : MvpAppCompatFragment(R.layout.fragment_settings), IViewSettings {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}