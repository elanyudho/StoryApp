package com.elanyudho.storyapp.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elanyudho.core.abstraction.BaseActivityBinding
import com.elanyudho.core.exception.Failure
import com.elanyudho.movrefapplication.utils.pagination.RecyclerviewPaginator
import com.elanyudho.storyapp.R
import com.elanyudho.storyapp.data.pref.Session
import com.elanyudho.storyapp.databinding.ActivityMainBinding
import com.elanyudho.storyapp.ui.addstory.AddStoryActivity
import com.elanyudho.storyapp.ui.detail.DetailActivity
import com.elanyudho.storyapp.ui.main.adapter.StoriesAdapter
import com.elanyudho.storyapp.ui.maps.MapsStoryActivity
import com.elanyudho.storyapp.ui.settings.PreferenceFragment
import com.elanyudho.storyapp.utils.extensions.gone
import com.elanyudho.storyapp.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivityBinding<ActivityMainBinding>(),
    Observer<MainViewModel.MainUiState> {

    @Inject
    lateinit var mViewModel: MainViewModel

    @Inject
    lateinit var session: Session

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    private val storiesAdapter: StoriesAdapter by lazy { StoriesAdapter() }

    private var paginator: RecyclerviewPaginator? = null

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = { ActivityMainBinding.inflate(layoutInflater) }

    override fun setupView() {
        initData()
        setResultLauncher()
        setStoriesPagination()
        setHeaderAction()
        setStoriesAction()
        setFABAction()

    }

    override fun onChanged(state: MainViewModel.MainUiState?) {
        when (state) {
            is MainViewModel.MainUiState.StoriesLoaded -> {
                stopLoading()
                if (state.stories.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.empty_data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                storiesAdapter.appendList(state.stories)
            }
            is MainViewModel.MainUiState.InitialLoading -> {
                startInitialLoading()
            }
            is MainViewModel.MainUiState.PagingLoading -> {
                startPagingLoading()
            }
            is MainViewModel.MainUiState.FailedLoadStories -> {
                stopLoading()
                handleFailure(state.failure)
            }
        }
    }

    private fun initData() {
        mViewModel.uiState.observe(this, this)
        mViewModel.getStories(1)
    }

    private fun startInitialLoading() {
        binding.progressBar.visible()
        binding.rvStory.gone()
    }

    private fun stopLoading() {
        binding.progressBar.gone()
        binding.rvStory.visible()
    }

    private fun startPagingLoading() {
        binding.progressBar.visible()
        binding.rvStory.visible()
    }

    private fun handleFailure(failure: Failure) {
        Toast.makeText(this, failure.throwable.message, Toast.LENGTH_SHORT).show()
    }

    private fun setStoriesPagination() {
        paginator =
            RecyclerviewPaginator(binding.rvStory.layoutManager as LinearLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            mViewModel.getStories(page)
        }
        paginator?.let { binding.rvStory.addOnScrollListener(it) }
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

    private fun setStoriesAction() {
        with(binding.rvStory) {
            adapter = storiesAdapter
            setHasFixedSize(true)
        }

        storiesAdapter.setOnClickData { data, optionCompat ->
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
                storiesAdapter.clearList()
                mViewModel.getStories(1)
            }
        }
    }

    private fun setFABAction() {
        binding.fabAddStory.setOnClickListener {
            resultLauncher?.launch(Intent(this@MainActivity, AddStoryActivity::class.java))
        }
    }

}