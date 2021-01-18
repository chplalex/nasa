package com.chplalex.nasa.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.chplalex.nasa.R
import com.chplalex.nasa.mvp.presenter.PresenterNasaApod
import com.chplalex.nasa.mvp.view.IViewNasaApod
import com.chplalex.nasa.ui.App
import com.chplalex.nasa.utils.TAG
import com.google.android.material.bottomsheet.BottomSheetBehavior
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

class FragmentNasaApod : MvpAppCompatFragment(R.layout.fragment_nasa_apod), IViewNasaApod {

    @Inject
    lateinit var injectProvider: Provider<PresenterNasaApod>

    private val presenter by moxyPresenter {
        injectProvider.get()
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var imageView: ImageView
    private lateinit var textViewTitle: TextView
    private lateinit var textViewExplanation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.activityComponent?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.nasa_apod_image)
        textViewTitle = view.findViewById(R.id.nasa_apod_title)
        textViewExplanation = view.findViewById(R.id.nasa_apod_explanation)
        setBottomSheetBehavior(view.findViewById(R.id.nasa_apod_bottom_sheet_container))
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun setTitle(title: String) {
        textViewTitle.text = title
    }

    override fun setImage(url: String) {
        Glide.with(requireContext())
            .load(url)
            .centerCrop()
            .error(R.drawable.ic_load_error)
            .into(imageView)
    }

    override fun setExplanation(explanation: String) {
        textViewExplanation.text = explanation
    }

    override fun showError(msg: String, error: Throwable) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_error)
            .setMessage(msg)
            .setMessage(error.toString())
            .create()
    }
}