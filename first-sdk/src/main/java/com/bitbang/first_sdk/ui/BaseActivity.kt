package com.bitbang.first_sdk.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bitbang.first_sdk.R
import com.bitbang.first_sdk.databinding.ActivityBaseBinding

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

        navController = navHostFragment?.findNavController()!!
        navController.setGraph(R.navigation.nav_graph)
        navController.navigate(R.id.step1Fragment)

    }


}