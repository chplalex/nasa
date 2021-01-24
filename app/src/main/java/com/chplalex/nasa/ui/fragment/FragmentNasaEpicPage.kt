package com.chplalex.nasa.ui.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chplalex.nasa.R
import com.chplalex.nasa.mvp.model.NasaEpicItem
import com.chplalex.nasa.mvp.presenter.PresenterNasaEpicPage
import com.chplalex.nasa.mvp.view.IViewNasaEpicPage
import com.chplalex.nasa.ui.App
import com.chplalex.nasa.utils.TAG
import com.google.android.material.progressindicator.LinearProgressIndicator
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

private const val NASA_EPIC_PAGE_KEY = "NASA_EPIC_PAGE_KEY"

class FragmentNasaEpicPage : MvpAppCompatFragment(R.layout.fragment_nasa_epic_page), IViewNasaEpicPage {

    @Inject lateinit var injectProvider: Provider<PresenterNasaEpicPage>

    private val presenter by moxyPresenter {
        injectProvider.get()
    }

    private lateinit var image: ImageView
    private lateinit var textCaption: TextView
    private lateinit var textTime: TextView
    private lateinit var progressIndicator: LinearProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.activityComponent?.inject(this)
        super.onCreate(savedInstanceState)
        arguments?.let {
            val item: NasaEpicItem? = it.getParcelable(NASA_EPIC_PAGE_KEY)
            presenter.onLoadData(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        image = view.findViewById(R.id.nasa_epic_image_view)
        textCaption = view.findViewById(R.id.nasa_epic_text_caption)
        textTime = view.findViewById(R.id.nasa_epic_text_time)
        progressIndicator = view.findViewById(R.id.nasa_epic_progress_indicator)
    }

    override fun setImage(url: String) {
        Log.d(TAG, url)
        setProgressVisibility(View.VISIBLE)
        Glide.with(requireContext())
            .load(url)
            .error(R.drawable.ic_load_error)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    setProgressVisibility(View.INVISIBLE)
                    return false
                }
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    setProgressVisibility(View.INVISIBLE)
                    return false
                }
            })
            .into(image)
    }

    override fun setCaption(caption: String) {
        textCaption.text = caption
    }

    override fun setTime(time: String) {
        textTime.text = time
    }

    override fun setProgressVisibility(state: Int) {
        progressIndicator.visibility = state
    }

    companion object {

        fun newInstance(item: NasaEpicItem) = FragmentNasaEpicPage().apply {
            arguments = Bundle().apply {
                putParcelable(NASA_EPIC_PAGE_KEY, item)
            }
        }
    }
}