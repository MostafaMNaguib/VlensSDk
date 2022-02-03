package com.bitbang.vlenssdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bitbang.first_sdk.Step1Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val step1Fragment = Step1Fragment()
        val fragmentManager = this.supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.navHostFragmentContainer,step1Fragment).commit()


    }
}