package com.bitbang.first_sdk.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bitbang.first_sdk.R
import com.bitbang.first_sdk.databinding.FragmentCameraBinding
import com.google.android.material.snackbar.Snackbar
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Mode
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private lateinit var navController: NavController

    private var side = -1 // Front -> 0, back -> 1

    private val tAG = CameraFragment::class.java.simpleName
    private lateinit var mContext: Context

    private lateinit var frontSideFile: File
    private lateinit var backSideFile: File

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = binding.root.context
        navController = Navigation.findNavController(binding.root)
//        viewModel = (activity ).viewModel


        frontSideFile = File(requireActivity().filesDir, "frontSide.jpeg")
        backSideFile = File(requireActivity().filesDir, "backSide.jpeg")

//        viewModel = (activity as ProcessActivity).viewModel

        binding.cameraView.setLifecycleOwner(viewLifecycleOwner)
        binding.cameraView.mode = Mode.PICTURE
        binding.cameraView.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                if (side == 0) {
                    result.toFile(frontSideFile) {
                        it?.toUri()?.let { uri ->
//                            binding.frontSideImageView.setImageURI(uri)
                            val imageString = getBase64(uri)
                            Log.e(tAG, "onPictureTaken:$side " )
                            binding.captureFrontSideButton.visibility = GONE
                            binding.frontSideImageGroup.visibility = VISIBLE
                            binding.backSideImageGroup.visibility = GONE
                            binding.cameraGroup.visibility = GONE


                        }
                    }
                }
                else if (side == 1) {
                    result.toFile(backSideFile) {
                        it?.toUri()?.let { uri ->
                            binding.backSideImageView.setImageURI(uri)
                            val imageString = getBase64(uri)
                            binding.captureBackSideButton.visibility = GONE
                            binding.frontSideImageGroup.visibility = GONE
                            binding.backSideImageGroup.visibility = VISIBLE
                            binding.cameraGroup.visibility = GONE
                        }
                    }
                }
            }
        })

        // Front side picture functionalities
        binding.captureFrontSideButton.setOnClickListener {
            captureFrontSide()
        }
        binding.recaptureFrontSideButton.setOnClickListener {
            binding.frontSideImageView.setImageURI(null)
            binding.cameraGroup.visibility = VISIBLE
            binding.captureFrontSideButton.visibility = VISIBLE
            binding.frontSideImageGroup.visibility = GONE
//            viewModel.postAction(ProcessViewAction.ReCaptureFrontSideImage)
        }
        binding.proceedToBackSideButton.setOnClickListener {

            val timestampMilliseconds = System.currentTimeMillis()

            val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.US)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
            val stringDate = simpleDateFormat.format(Date(timestampMilliseconds))

            binding.captureBackSideButton.visibility = VISIBLE
            binding.backSideImageTitleTextView.visibility = VISIBLE
            binding.frontSideImageTitleTextView.visibility = GONE
            binding.frontSideImageGroup.visibility = GONE
            binding.cameraGroup.visibility = VISIBLE

//            viewModel.sendStatusToSlack(stringDate,"Send front side")
        }

        // Back side picture functionalities
        binding.captureBackSideButton.setOnClickListener {
            captureBackSide()
        }
        binding.recaptureBackSideButton.setOnClickListener {
            binding.backSideImageView.setImageURI(null)
            binding.cameraGroup.visibility = VISIBLE
            binding.captureBackSideButton.visibility = VISIBLE
            binding.backSideImageGroup.visibility = GONE

//            viewModel.postAction(ProcessViewAction.ReCaptureBackSideImage)
        }
        binding.proceedToVideoButton.setOnClickListener {
            val timestampMilliseconds = System.currentTimeMillis()
            val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.US)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
            val stringDate = simpleDateFormat.format(Date(timestampMilliseconds))
            navController.navigate(R.id.videoFragment)
//            viewModel.sendStatusToSlack(stringDate,"Send Back side")
        }


    }

    private fun captureFrontSide() {
        side = 0
        binding.cameraView.takePicture()
    }

    private fun captureBackSide() {
        side = 1
        binding.cameraView.takePicture()
    }

    private fun getBase64(imageUri: Uri): String {
        val imageStream = binding.root.context.contentResolver.openInputStream(imageUri)
        val selectedImage = BitmapFactory.decodeStream(imageStream)

        val bitmap = rotateImage(selectedImage, imageStream!!)
        binding.frontSideImageView.setImageBitmap(bitmap)

        val byteArrayOutputStream = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun rotateImage(bitmap: Bitmap, inputStream: InputStream): Bitmap {
        val exifInterface = ExifInterface(inputStream)
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        val matrix = Matrix()
        when (orientation){
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                Log.e(tAG,"ROTATION 90")
                matrix.setRotate(90f)}
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                Log.e(tAG,"ROTATION 180")
                matrix.setRotate(180f)}
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                Log.e(tAG,"ROTATION 270")
                matrix.setRotate(270f)}
            else -> {
                Log.e(tAG,"ROTATION else")}
//                matrix.setRotate(90f)}
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}