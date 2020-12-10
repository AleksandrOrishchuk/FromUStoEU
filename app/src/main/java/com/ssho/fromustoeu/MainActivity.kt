package com.ssho.fromustoeu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ssho.fromustoeu.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding.apply {
            viewModel = mainViewModel
            lifecycleOwner = this@MainActivity
        }

        Log.d(TAG, "Binding applied.")

        mainViewModel.mainViewStateLiveData.observe(this) { mainViewState ->

            val fragment =
                    if (mainViewState.isValueProvided)
                        ConvertBucketListFragment.newInstance(mainViewState)
                    else
                        NoValueFragment.newInstance()

            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
        }

        mainViewModel.isSoftKeyboardFocused.observe(this) { hasFocus ->
            if (!hasFocus)
                closeSoftKeyboard(this, mainBinding.valueEditText)
        }
    }

}