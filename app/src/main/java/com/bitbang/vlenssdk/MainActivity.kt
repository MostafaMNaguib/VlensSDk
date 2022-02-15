package com.bitbang.vlenssdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bitbang.first_sdk.ui.BaseActivity
import com.bitbang.vlenssdk.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /*val step1Fragment = Step1Fragment()
        val fragmentManager = this.supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.navHostFragmentContainer,step1Fragment).commit()*/

        binding.button.setOnClickListener {
            startActivity(Intent(this,BaseActivity::class.java))

        }
    }
}