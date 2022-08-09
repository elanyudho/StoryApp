package com.elanyudho.storyapp.ui.auth.login

import android.content.Intent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.Observer
import com.elanyudho.core.abstraction.BaseActivityBinding
import com.elanyudho.core.exception.Failure
import com.elanyudho.storyapp.data.pref.EncryptedPreferences
import com.elanyudho.storyapp.data.pref.Session
import com.elanyudho.storyapp.databinding.ActivityLoginBinding
import com.elanyudho.storyapp.domain.model.User
import com.elanyudho.storyapp.ui.auth.register.RegisterActivity
import com.elanyudho.storyapp.ui.main.MainActivity
import com.elanyudho.storyapp.utils.extensions.gone
import com.elanyudho.storyapp.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivityBinding<ActivityLoginBinding>(),
    Observer<LoginViewModel.LoginUiState> {

    @Inject
    lateinit var mViewModel: LoginViewModel

    @Inject
    lateinit var encryptedPreferences: EncryptedPreferences

    @Inject
    lateinit var session: Session

    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        get() = { ActivityLoginBinding.inflate(layoutInflater) }

    override fun setupView() {

        mViewModel.uiState.observe(this, this)

        doSignUp()
        doLogin()
    }

    override fun onChanged(state: LoginViewModel.LoginUiState?) {
        when (state) {
            is LoginViewModel.LoginUiState.Success -> {
                saveStateUser(state.body)
            }
            is LoginViewModel.LoginUiState.Loading -> {
               startLoadingView()
            }
            is LoginViewModel.LoginUiState.Failed -> {
                stopLoadingView()
                handleFailure(state.failure)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun doLogin() {
        binding.btnLogin.setOnClickListener {
            setLogin()
        }
    }

    private fun setLogin() {

        var isEmpty = false

        with(binding) {

            if (etEmail.text.isNullOrEmpty()) {
                isEmpty = true
            }
            if (etPassword.text.isNullOrEmpty()) {
                isEmpty = true
            }

            if (isEmpty) {
                Toast.makeText(
                    this@LoginActivity,
                    "Make sure all fields are filled",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                mViewModel.doLogin(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
            }

        }
    }

    private fun doSignUp() {
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun handleFailure(failure: Failure) {
        Toast.makeText(
            this,
            failure.throwable.message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun saveStateUser(user: User) {
        encryptedPreferences.encryptedToken = user.token
        user.token = ""
        session.isLogin = true
        session.user = user
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finishAffinity()
    }

    private fun startLoadingView() {
        with(binding) {
            grpLogin.gone()
            cvLottieLoading.visible()
        }
    }

    private fun stopLoadingView() {
        with(binding) {
            grpLogin.visible()
            cvLottieLoading.gone()
        }
    }
}