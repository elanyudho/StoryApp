package com.elanyudho.storyapp.utils.extensions

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.elanyudho.storyapp.R
import com.elanyudho.storyapp.utils.version.VersionUtil.hasSdkAboveEqual

fun ImageView.glide(view: View, image: Any) {
    try {
        Glide.with(view)
            .load(image)
            .error(R.color.grey_nobel)
            .into(this)
    } catch (ignored: Throwable) {
    }
}

fun ImageView.glide(context: Context, image: Any) {
    try {
        Glide.with(context)
            .load(image)
            .error(R.color.grey_nobel)
            .into(this)
    } catch (ignored: Throwable) {
    }
}

fun enable(view: View) {
    view.apply {
        isFocusable = true
        isClickable = true
        isEnabled = true
        invalidate()
    }
}

fun disable(view: View) {
    view.apply {
        isFocusable = false
        isClickable = false
        isEnabled = false
        invalidate()
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Context.setStatusBar(color: Int, activity: Activity?) {
    activity?.window?.statusBarColor = ContextCompat.getColor(this, color)
    if (hasSdkAboveEqual(Build.VERSION_CODES.R)) {
        if(ContextCompat.getColor(this, color).isColorDark()){
            activity?.window?.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            activity?.window?.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
    }
    else {
        val theme = if (ContextCompat.getColor(this, color).isColorDark()) 0 else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity?.window?.decorView?.systemUiVisibility = theme
        //Timber.d("${ContextCompat.getColor(this, color).isColorDark()}, ${ContextCompat.getColor(this, color)}, ${theme}")
    }

}