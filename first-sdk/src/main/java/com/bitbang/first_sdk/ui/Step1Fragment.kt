package com.bitbang.first_sdk.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bitbang.first_sdk.databinding.FragmentStep1Binding
import com.bitbang.first_sdk.fastBlur
import com.bitbang.first_sdk.getBase64
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Mode
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class Step1Fragment : Fragment() {

    private val tAG = Step1Fragment::class.java.simpleName
    private lateinit var binding : FragmentStep1Binding
    private lateinit var mContext: Context
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStep1Binding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = binding.root.context
        navController = Navigation.findNavController(binding.root)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                mContext as Activity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }


//        binding.imageCaptureButton.setOnClickListener { takePhoto() }
        cameraExecutor = Executors.newSingleThreadExecutor()

//        binding.videoCaptureButton.setOnClickListener {
////            navController.navigate(R.id.cameraFragment)
//        }

    }


    private fun startCamera() {

        binding.cameraView.setLifecycleOwner(viewLifecycleOwner)
        binding.cameraView.mode = Mode.PICTURE


        val file = File(requireActivity().filesDir, "frontSide.jpeg")
        binding.cameraView.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                    result.toFile(file) {
                        it?.toUri()?.let { uri ->
//                            binding.frontSideImageView.setImageURI(uri)
                            val imageString = getBase64(uri,mContext)
                            binding.frontSideImageView.setImageBitmap(
                                fastBlur(
                                uri,mContext,2.0F,4
                            )
                            )
//                            Log.e(tAG, "$imageString" )
                        }
                    }
            }
        })



    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        binding.cameraView.takePicture()

    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            mContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(mContext,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                (mContext as Activity).finish()
            }
        }
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

}