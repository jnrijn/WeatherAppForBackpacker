package com.example.weatherappforbackpacker.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappforbackpacker.BR
import com.example.weatherappforbackpacker.R
import com.example.weatherappforbackpacker.databinding.ActivityMainBinding
import com.example.weatherappforbackpacker.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val dataBinding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        dataBinding.setVariable(BR.vm, viewModel)
    }
}
