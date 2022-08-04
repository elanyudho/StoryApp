package com.elanyudho.storyapp.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.elanyudho.core.abstraction.BaseActivityBinding
import com.elanyudho.storyapp.R
import com.elanyudho.storyapp.data.pref.Session
import com.elanyudho.storyapp.databinding.ActivityMainBinding
import com.elanyudho.storyapp.ui.addstory.AddStoryActivity
import com.elanyudho.storyapp.ui.detail.DetailActivity
import com.elanyudho.storyapp.ui.main.adapter.LoadingStateAdapter
import com.elanyudho.storyapp.ui.main.adapter.PagingStoriesAdapter
import com.elanyudho.storyapp.ui.maps.MapsStoryActivity
import com.elanyudho.storyapp.ui.settings.PreferenceFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivityBinding<ActivityMainBinding>(){

    @Inject
    lateinit var mViewModel: MainViewModel

    @Inject
    lateinit var session: Session

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    private val pagingStoriesAdapter = PagingStoriesAdapter()

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = { ActivityMainBinding.inflate(layoutInflater) }

    override fun setupView() {
        setPagingStoriesAction()
        initData()
        setResultLauncher()
        setHeaderAction()
        setFABAction()

    }

    private fun initData() {
        mViewModel.getStoriesPaging().observe(this) {
            pagingStoriesAdapter.submitData(lifecycle, it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setHeaderAction() {
        with(binding) {
            headerMainStories.tvUsername.text = "Hi, ${session.user?.username}"
            headerMainStories.btnMaps.setOnClickListener {
                startActivity(Intent(this@MainActivity, MapsStoryActivity::class.java))
            }
            headerMainStories.btnSetting.setOnClickListener {

                val mPrefFragment = PreferenceFragment()
                val mFragmentManager = supportFragmentManager
                val mFragmentTransaction = mFragmentManager.beginTransaction()
                mFragmentTransaction.add(
                    R.id.frameLayout,
                    mPrefFragment,
                    PreferenceFragment::class.java.simpleName
                ).addToBackStack(null).commit()
            }
        }
    }

    private fun setPagingStoriesAction() {
        with(binding.rvStory) {
            adapter = pagingStoriesAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    pagingStoriesAdapter.retry()
                }
            )
            setHasFixedSize(false)

        }

        pagingStoriesAdapter.setOnClickData { data, optionCompat ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_STORY, data)
            startActivity(intent, optionCompat.toBundle())
        }
    }

    private fun setResultLauncher() {
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                recreate()
            }
        }
    }

    private fun setFABAction() {
        binding.fabAddStory.setOnClickListener {
            resultLauncher?.launch(Intent(this@MainActivity, AddStoryActivity::class.java))
        }
    }

}