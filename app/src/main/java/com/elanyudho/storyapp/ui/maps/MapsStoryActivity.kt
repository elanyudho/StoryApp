package com.elanyudho.storyapp.ui.maps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.elanyudho.core.abstraction.BaseActivityBinding
import com.elanyudho.core.dispatcher.DispatcherProvider
import com.elanyudho.core.exception.Failure
import com.elanyudho.storyapp.R
import com.elanyudho.storyapp.databinding.ActivityMapsStoryBinding
import com.elanyudho.storyapp.domain.model.Story
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


@AndroidEntryPoint
class MapsStoryActivity : BaseActivityBinding<ActivityMapsStoryBinding>(), Observer<MapsStoryViewModel.MapsStoryUiState>, OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var dataStories: (data: List<Story>) -> Unit

    private var bitmapDescriptor: BitmapDescriptor? = null

    private val boundsBuilder = LatLngBounds.Builder()

    @Inject
    lateinit var mViewModel: MapsStoryViewModel

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    override val bindingInflater: (LayoutInflater) -> ActivityMapsStoryBinding
        get() = { ActivityMapsStoryBinding.inflate(layoutInflater) }

    override fun setupView() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initData()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onChanged(state: MapsStoryViewModel.MapsStoryUiState?) {
        when (state) {
            is MapsStoryViewModel.MapsStoryUiState.StoriesLoaded -> {
                dataStories.invoke(state.stories)
            }
            is MapsStoryViewModel.MapsStoryUiState.Loading -> {

            }
            is MapsStoryViewModel.MapsStoryUiState.FailedLoadStories -> {
                handleFailure(state.failure)
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .icon(vectorToBitmap(R.drawable.ic_chevron_right, Color.parseColor("#3DDC84")))
                .title("Marker in Sydney")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        addManyMarker { dataStories ->
            for (story in dataStories) {
                lifecycleScope.launch(dispatcherProvider.default) {
                    bitmapDescriptor = getBitmapFromURL(story.imageUrl)

                    withContext(dispatcherProvider.main) {
                        val latLng = LatLng(story.lat ?: 0.0, story.long ?: 0.0)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(story.username)
                                .icon(bitmapDescriptor)
                        )
                    }
                }
            }
            setViewCamera(mMap, dataStories)
        }
        //setting
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }

    /*private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            //Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }*/

    private fun initData() {
        mViewModel.uiState.observe(this, this)
        mViewModel.getStoriesLocation()
    }

    private fun addManyMarker(dataStories: (dataStories: List<Story>) -> Unit) {
        this.dataStories = dataStories
    }

    private fun handleFailure(failure: Failure) {
        Toast.makeText(this, failure.throwable.message, Toast.LENGTH_SHORT).show()
    }

    private fun getBitmapFromURL(src: String?): BitmapDescriptor? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            val bitmapImg = BitmapFactory.decodeStream(input)
            input.close()
            val resized = Bitmap.createScaledBitmap(bitmapImg, 56, 56, true);
            BitmapDescriptorFactory.fromBitmap(resized)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    private fun setViewCamera(mMap: GoogleMap?, list: List<Story>) {
        for (story in list) {
            val latLng = story.lat?.let { story.long?.let { it1 -> LatLng(it, it1) } }
            if (latLng != null) {
                boundsBuilder.include(latLng)
            }
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap?.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                (resources.displayMetrics.widthPixels*0.10).toInt()
            )
        )
    }

}