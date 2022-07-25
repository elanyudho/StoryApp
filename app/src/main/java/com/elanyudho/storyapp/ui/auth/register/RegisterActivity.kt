package com.elanyudho.storyapp.ui.auth.register

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.elanyudho.core.abstraction.BaseActivityBinding
import com.elanyudho.core.exception.Failure
import com.elanyudho.storyapp.databinding.ActivityRegisterBinding
import com.elanyudho.storyapp.ui.auth.login.LoginActivity
import com.elanyudho.storyapp.utils.extensions.gone
import com.elanyudho.storyapp.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : BaseActivityBinding<ActivityRegisterBinding>(),
    Observer<RegisterViewModel.RegisterUiState> {

    @Inject
    lateinit var mViewModel: RegisterViewModel

    override val bindingInflater: (LayoutInflater) -> ActivityRegisterBinding
        get() = { ActivityRegisterBinding.inflate(layoutInflater) }

    override fun setupView() {
        mViewModel.uiState.observe(this, this)

        doBackPage()
        doRegister()

    }

    override fun onChanged(state: RegisterViewModel.RegisterUiState?) {

        when (state) {
            is RegisterViewModel.RegisterUiState.Success -> {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
            is RegisterViewModel.RegisterUiState.Loading -> {
                startLoadingView()
            }
            is RegisterViewModel.RegisterUiState.Failed -> {
                stopLoadingView()
                handleFailure(state.failure)
            }
        }

    }

    private fun doRegister() {
        binding.btnRegister.setOnClickListener {
            setRegister()
        }
    }

    private fun setRegister() {

        var isEmpty = false

        with(binding) {

            if (etUsername.text.isNullOrEmpty()) {
                isEmpty = true
            }

            if (etEmail.text.isNullOrEmpty()) {
                isEmpty = true
            }
            if (etPassword.text.isNullOrEmpty()) {
                isEmpty = true
            }

            if (isEmpty) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Make sure all fields are filled",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                mViewModel.doRegister(
                    etUsername.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
            }

        }
    }

    private fun doBackPage() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
            hideKeyboard(this)
        }
    }

    private fun handleFailure(failure: Failure) {
        Toast.makeText(
            this,
            failure.throwable.message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun startLoadingView() {
        with(binding) {
            grpRegister.gone()
            cvLottieLoading.visible()
        }
    }

    private fun stopLoadingView() {
        with(binding) {
            grpRegister.visible()
            cvLottieLoading.gone()
        }
    }

}

