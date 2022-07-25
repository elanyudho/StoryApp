package com.elanyudho.storyapp.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import com.elanyudho.core.abstraction.BaseActivityBinding
import com.elanyudho.storyapp.data.pref.Session
import com.elanyudho.storyapp.databinding.ActivitySplashScreenBinding
import com.elanyudho.storyapp.ui.auth.login.LoginActivity
import com.elanyudho.storyapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity : BaseActivityBinding<ActivitySplashScreenBinding>() {

    @Inject
    lateinit var session: Session

    override val bindingInflater: (LayoutInflater) -> ActivitySplashScreenBinding
        get() = { ActivitySplashScreenBinding.inflate(it) }

    override fun setupView() {
        //propertyAnimation
        playAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            moveNext()
        }, 3500L)
    }

    private fun moveNext() {
        if (session.isLogin) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }

    private fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.ivLogo, View.ALPHA, 1f).setDuration(800)
        val appName = ObjectAnimator.ofFloat(binding.tvAppName, View.ALPHA, 1f).setDuration(800)
        val poweredBy = ObjectAnimator.ofFloat(binding.tvPoweredBy, View.ALPHA, 1f).setDuration(800)

        AnimatorSet().apply {
            playTogether(logo, appName, poweredBy)
            start()
        }
    }

}