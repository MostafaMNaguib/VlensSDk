package com.bitbang.first_sdk.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bitbang.first_sdk.R
import com.bitbang.first_sdk.databinding.ActivityBaseBinding
import com.bitbang.first_sdk.utils.ServiceStepTypes
import java.math.RoundingMode.valueOf

class BaseActivity(): AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding
    private lateinit var navController: NavController
    private val tAG = BaseActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)

        Log.e(tAG, "onCreate: this" )

        try {
            val extras = intent.extras
            Log.e(tAG, "onCreate: try ${extras?.get("Name")}" )

        }
        catch (e: Exception){
            Log.e(tAG, "onCreate:Exception ${e.toString()}" )
        }

        navController = navHostFragment?.findNavController()!!
        navController.setGraph(R.navigation.nav_graph)
        navController.navigate(R.id.cameraFragment)

        val list = ArrayList<Int>()
        list.add(1)
        list.add(2)

        if (list[0] == ServiceStepTypes.OcrCardFrontSide.value)
            Log.e(tAG, "onCreate:Enum is true ${ServiceStepTypes.OcrCardFrontSide.value} " )

        Log.e(tAG, "onCreate:Enum ${ServiceStepTypes.OcrCardFrontSide.value} " )

    }


}