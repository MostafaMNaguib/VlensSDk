package com.bitbang.first_sdk.ui.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bitbang.first_sdk.databinding.FragmentVideoBinding
import com.google.android.material.snackbar.Snackbar
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Mode
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class VideoFragment : Fragment() {

    private lateinit var binding: FragmentVideoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.cameraView.setLifecycleOwner(viewLifecycleOwner)
        binding.cameraView.mode = Mode.VIDEO

        binding.cameraView.addCameraListener(object : CameraListener() {
            override fun onVideoRecordingStart() {

            }
            override fun onVideoRecordingEnd() {
                binding.cameraView.flash = Flash.OFF
            }

            override fun onVideoTaken(result: VideoResult) {
                val uri = result.file.toUri()

                binding.videoView.setVideoURI(uri)
                binding.videoView.start()


            }
        })

        binding.recordVideoButton.setOnClickListener {
            recordVideo()
        }
        binding.reRecordVideoButton.setOnClickListener {

        }
        binding.proceedButton.setOnClickListener {


            val timestampMilliseconds = System.currentTimeMillis()
            val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.US)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
            val stringDate = simpleDateFormat.format(Date(timestampMilliseconds))


        }
        binding.videoView.setOnClickListener {
            binding.videoView.start()
        }

    }

    private fun recordVideo() {
        binding.cameraView.flash = Flash.TORCH
        binding.cameraView.takeVideo(File(requireActivity().filesDir, "video.mp4"), 3000)
    }

}