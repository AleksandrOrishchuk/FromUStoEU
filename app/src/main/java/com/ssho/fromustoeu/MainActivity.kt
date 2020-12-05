package com.ssho.fromustoeu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ssho.fromustoeu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val appStateViewModel: AppStateViewModel by lazy {
        ViewModelProvider(this).get(AppStateViewModel::class.java)
    }

//    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.apply {
            viewModel = appStateViewModel
            lifecycleOwner = this@MainActivity
            valueEditText.setText(appStateViewModel.currentValueText)
        }

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null)
            appStateViewModel.loadInitialAppState()

        appStateViewModel.appStateLiveData.observe(this) { appState ->
            val fragment = ConvertBucketListFragment.newInstance(appState)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
        }
    }

}