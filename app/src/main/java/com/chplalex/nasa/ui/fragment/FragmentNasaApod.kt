package com.chplalex.nasa.ui.fragment

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chplalex.nasa.R
import com.chplalex.nasa.mvp.model.Color
import com.chplalex.nasa.mvp.presenter.PresenterNasaApod
import com.chplalex.nasa.mvp.view.IViewNasaApod
import com.chplalex.nasa.ui.App
import com.chplalex.nasa.utils.TAG
import com.chplalex.nasa.utils.randomColor
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.LinearProgressIndicator
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import java.text.BreakIterator
import java.util.*
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
    private lateinit var progressIndicator: LinearProgressIndicator
    private lateinit var chipGroup: ChipGroup
    private lateinit var bottomSheet: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.activityComponent?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.nasa_apod_image)

        textViewTitle = view.findViewById(R.id.nasa_apod_title)
        textViewTitle.typeface = Typeface.createFromAsset(context?.assets, "LongFox-o77A.ttf")

        textViewExplanation = view.findViewById(R.id.nasa_apod_explanation)
        textViewExplanation.typeface = Typeface.createFromAsset(
            context?.assets,
            "AlienWars-3V3M.ttf"
        )

        progressIndicator = view.findViewById(R.id.nasa_apod_progress_indicator)
        chipGroup = view.findViewById(R.id.nasa_apod_chip_group)
        bottomSheet = view.findViewById(R.id.nasa_apod_bottom_sheet_container)
        setBottomSheetBehavior(bottomSheet)
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun setChipGroupListener(listener: ChipGroup.OnCheckedChangeListener) {
        chipGroup.setOnCheckedChangeListener(listener)
    }

    override fun setChipToday() {
        chipGroup.check(R.id.nasa_apod_chip_today)
    }

    override fun hideAnimatedViews() {
        animateViews(View.INVISIBLE) { presenter.onViewsHided() }
    }

    override fun showAnimatedViews() {
        animateViews(View.VISIBLE, null)
    }

    private fun animateViews(visibilityAfterAnimation: Int, onAnimationFinish: (() -> Unit)?) {
        val animationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                imageView.visibility = visibilityAfterAnimation
                bottomSheet.visibility = visibilityAfterAnimation
                onAnimationFinish?.invoke()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        }

        val alphaStart = if (visibilityAfterAnimation == View.VISIBLE) 0.0f else 1.0f
        val alphaEnd = if (visibilityAfterAnimation == View.VISIBLE) 1.0f else 0.0f

        val animation = AlphaAnimation(alphaStart, alphaEnd).apply {
            duration = 350
            fillAfter = true
            setAnimationListener(animationListener)
        }

        imageView.clearAnimation()
        bottomSheet.clearAnimation()

        imageView.animation = animation
        bottomSheet.animation = animation

        animation.start()
    }

    override fun setTitle(title: String) {
        textViewTitle.text = title
    }

    override fun setImage(url: String) {
        imageView.setImageDrawable(null)
        setProgressVisibility(View.VISIBLE)
        Glide.with(requireContext())
            .load(url)
            .centerCrop()
            .error(R.drawable.ic_load_error)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    presenter.onImageLoadFailed()
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
                    presenter.onImageLoadSuccess()
                    setProgressVisibility(View.INVISIBLE)
                    return false
                }
            })
            .into(imageView)
    }

    override fun setVideo(url: String) {
    }

    override fun setDefaultImage() {
        imageView.setImageResource(R.drawable.ic_nasa)
    }

    override fun setErrorImage() {
        imageView.setImageResource(R.drawable.ic_load_error)
    }

    override fun setExplanation(explanation: String) {
        Log.d(TAG, "explanation = ${explanation}")

        val spannableString = SpannableString(explanation)
        textViewExplanation.setText(spannableString, TextView.BufferType.SPANNABLE)
        val spannableText = textViewExplanation.text as Spannable

        // делим текст на отдельные предложения
        val iterator: BreakIterator = BreakIterator.getSentenceInstance(Locale.US)
        iterator.setText(explanation)
        var start = iterator.first()
        var end = iterator.next()
        while (end != BreakIterator.DONE) {
            Log.d(TAG, explanation.substring(start, end))
            setSpan(spannableText, start, end, randomColor())
            start = end
            end = iterator.next()
        }
    }

    private fun setSpan(spannableText: Spannable, start: Int, end: Int, color: Color) {
        spannableText.setSpan(
            BackgroundColorSpan(resources.getColor(color.getColorRes())),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    fun setProgressVisibility(state: Int) {
        progressIndicator.visibility = state
    }

    override fun showError(title: String, error: Throwable) {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(title)
            .setMessage(error.message)
            .setNeutralButton(R.string.nasa_apod_button_clear) { _, _ -> presenter.onDialogButtonPressed() }
            .create()
            .show()
    }

    private fun Color.getColorRes() = when (this) {
        Color.WHITE -> R.color.color_white
        Color.VIOLET -> R.color.color_violet
        Color.YELLOW -> R.color.color_yellow
        Color.RED -> R.color.color_red
        Color.PINK -> R.color.color_pink
        Color.GREEN -> R.color.color_green
        Color.BLUE -> R.color.color_blue
    }
}