package com.elanyudho.storyapp.ui.addstory

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.elanyudho.core.abstraction.BaseActivityBinding
import com.elanyudho.core.exception.Failure
import com.elanyudho.storyapp.R
import com.elanyudho.storyapp.databinding.ActivityAddStoryBinding
import com.elanyudho.storyapp.utils.extensions.gone
import com.elanyudho.storyapp.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddStoryActivity : BaseActivityBinding<ActivityAddStoryBinding>(), Observer<AddStoryViewModel.AddStoryUiState> {

    @Inject
    lateinit var mViewModel: AddStoryViewModel

    private var getFile: File? = null

    override val bindingInflater: (LayoutInflater) -> ActivityAddStoryBinding
        get() = { ActivityAddStoryBinding.inflate(layoutInflater) }

    private var launcherIntentCamera: ActivityResultLauncher<Intent>? = null
    private var launcherIntentGallery: ActivityResultLauncher<Intent>? = null

    private lateinit var currentPhotoPath: String

    override fun setupView() {
        initViewModel()
        setHeaderAction()

        //intent camera
        //intent camera with full size
        takePhotoFromCamera()
        setLauncherIntentCamera()

        //intent gallery
        takePhotoFromGallery()
        setLauncherIntentGallery()

        postStory()
    }

    override fun onChanged(state: AddStoryViewModel.AddStoryUiState?) {
        when (state) {
            is AddStoryViewModel.AddStoryUiState.SuccessUpload -> {
                setResult(Activity.RESULT_OK)
                Toast.makeText(this@AddStoryActivity, getString(R.string.success_updates_story), Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
            is AddStoryViewModel.AddStoryUiState.Loading -> {
                startLoadingView()
            }
            is AddStoryViewModel.AddStoryUiState.FailedUpload -> {
                stopLoadingView()
                handleFailure(state.failure)
            }
        }
    }

    private fun initViewModel() {
        mViewModel.uiState.observe(this, this)
    }

    private fun setLauncherIntentCamera() {
        launcherIntentCamera = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                val photoFile = File(currentPhotoPath)
                getFile = photoFile
                val result = BitmapFactory.decodeFile(getFile?.path)

                binding.ivStory.setImageBitmap(result)
            }
        }
    }

    private fun setLauncherIntentGallery() {
        launcherIntentGallery = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImg: Uri = result.data?.data as Uri
                val photoFile = uriToFile(selectedImg, this@AddStoryActivity)
                getFile = photoFile
                binding.ivStory.setImageURI(selectedImg)
            }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.elanyudho.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera?.launch(intent)
        }
    }

    private fun takePhotoFromCamera() {
        binding.btnCamera.setOnClickListener {
            startTakePhoto()
            binding.ivStory.visible()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery?.launch(chooser)
    }

    private fun takePhotoFromGallery() {
        binding.btnGallery.setOnClickListener {
            startGallery()
            binding.ivStory.visible()
        }
    }

    private fun setHeaderAction() {
        with(binding) {
            headerDetail.tvTitleHeader.text = getString(R.string.add_story)
            headerDetail.btnBack.setOnClickListener { onBackPressed() }
        }
    }

    private fun getTimeStamp():  String{
        return SimpleDateFormat(
            FILENAME_FORMAT,
            Locale.US
        ).format(System.currentTimeMillis())

    }

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(getTimeStamp(), ".jpg", storageDir)
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private fun setDataAddStory() {
        if (getFile != null) {
            val photoFile = reduceFileImage(getFile as File)

            val description = binding.etDescNote.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = photoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                photoFile.name,
                requestImageFile
            )

            mViewModel.postStory(imageMultipart, description)
        } else {
            Toast.makeText(this@AddStoryActivity, getString(R.string.no_image_added), Toast.LENGTH_SHORT).show()
        }
    }

    private fun postStory() {
        binding.btnUpload.setOnClickListener {
            setDataAddStory()
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun startLoadingView() {
        binding.grpAddStory.gone()
        binding.cvLottieLoading.visible()
    }

    private fun stopLoadingView() {
        binding.grpAddStory.visible()
        binding.cvLottieLoading.gone()
    }

    private fun handleFailure(failure: Failure) {
        Toast.makeText(
            this,
            failure.throwable.message,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val FILENAME_FORMAT = "hh:mm"
    }
}
