package com.chplalex.nasa.ui.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import com.chplalex.nasa.R


class SplashActivity : AppCompatActivity() {

    private val DURATION = 5000L
    private val DIFF_SCALE = 0.5f
    private val ANIMATION_COUNT = 5

    private val imageView: ImageView by lazy { findViewById(R.id.image_view_nasa_logo_splash) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startAnimation(DURATION / ANIMATION_COUNT)
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private var count = ANIMATION_COUNT

    private fun startAnimation(interval: Long) {
        val scaleValue = if (count % 2 == 0) (1f + DIFF_SCALE) else (1f - DIFF_SCALE)
        val scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", scaleValue)
        val scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", scaleValue)
        AnimatorSet().also {
            it.duration = interval
            it.interpolator = AccelerateDecelerateInterpolator()
            it.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    if (count > 0) {
                        startAnimation(interval)
                    } else {
                        startMainActivity()
                    }
                }
            })
            it.play(scaleX).with(scaleY)
            it.start()
        }
        count--
    }
}