package com.elanyudho.storyapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.elanyudho.core.abstraction.BaseFragmentBinding
import com.elanyudho.storyapp.R
import com.elanyudho.storyapp.data.pref.EncryptedPreferences
import com.elanyudho.storyapp.data.pref.Session
import com.elanyudho.storyapp.databinding.FragmentPreferenceBinding
import com.elanyudho.storyapp.domain.model.User
import com.elanyudho.storyapp.ui.auth.login.LoginActivity
import com.elanyudho.storyapp.utils.extensions.setStatusBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PreferenceFragment : BaseFragmentBinding<FragmentPreferenceBinding>() {

    @Inject
    lateinit var session: Session

    @Inject
    lateinit var encryptedPreferences: EncryptedPreferences

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPreferenceBinding
        get() = { layoutInflater, viewGroup, b ->
            FragmentPreferenceBinding.inflate(
                layoutInflater,
                viewGroup,
                b
            )
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireContext().setStatusBar(R.color.blue_dodger, activity)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupView() {
        callOnceWhenCreated {
            setHeaderAction()
            setSettingLanguage()
            doLogout()

            //prevent click activity through fragment
            //Note: Dicoding reviewer, please give me best practice to prevent
            // I can touch and click activity even though the fragment show
            //This line is my way to handle this problem
            view?.setOnTouchListener { _, _ -> return@setOnTouchListener true}
        }
    }

    private fun setHeaderAction() {
        binding.headerSetting.btnBack.setOnClickListener {
            val mFragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val mFragmentTransaction = mFragmentManager.beginTransaction()
            mFragmentTransaction.remove(PreferenceFragment())
            mFragmentTransaction.commit()
            mFragmentManager.popBackStack()
        }

        binding.headerSetting.tvTitleHeader.setText(R.string.settings)
    }

    private fun doLogout() {
        binding.rowLogout.setOnClickListener {
            session.isLogin = false
            session.user = User()
            encryptedPreferences.clear()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun setSettingLanguage() {
        binding.rowLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

}