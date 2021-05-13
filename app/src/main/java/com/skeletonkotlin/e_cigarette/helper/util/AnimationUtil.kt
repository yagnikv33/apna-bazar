package com.skeletonkotlin.e_cigarette.helper.util

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Transformation
import android.widget.ProgressBar
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener

object AnimationUtil {
}

class ProgressBarAnimation(private val progressBar: ProgressBar, private val to: Float) :
    Animation() {

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        super.applyTransformation(interpolatedTime, t)
        val value = 0 + (to - 0) * interpolatedTime
        progressBar.progress = value.toInt()
    }

}

fun View.animateView(context: Context, animationType: Int, animationListener: Animation.AnimationListener?) {
    val animation = AnimationUtils.loadAnimation(context, animationType)
    this.startAnimation(animation)
    animation.setAnimationListener(animationListener)
}

fun View.clickAnimation(completionListener: ((view: View) -> Unit)) {
    val duration = 100
    ViewCompat.animate(this).scaleX(0.8f).scaleY(0.8f).setDuration(duration.toLong()).setInterpolator(null)
        .setListener(object : ViewPropertyAnimatorListener {
            override fun onAnimationStart(view: View) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(view: View) {
                ViewCompat.animate(view)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(duration.toLong())
                    .setInterpolator(null)
                    .setStartDelay(0)
                    .setListener(object : ViewPropertyAnimatorListener {
                        override fun onAnimationStart(view: View) {

                        }

                        override fun onAnimationEnd(view: View) {
                            completionListener.apply { this(view) }
                            view.isEnabled = true
                        }

                        override fun onAnimationCancel(view: View) {}
                    })
                    .start()
            }

            override fun onAnimationCancel(view: View) {
                view.isEnabled = true
            }
        }).setStartDelay(0).start()
    this.isEnabled = false
}

interface AnimationListener {
    fun onAnimationEnd(v: View)
}



