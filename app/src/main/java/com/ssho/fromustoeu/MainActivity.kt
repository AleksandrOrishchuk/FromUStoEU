package com.ssho.fromustoeu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
            mainViewState.apply {
                val fragment = when {
                    isValueProvided && appTab == TAB_HOME -> ConvertBucketListFragment.newInstance(mainViewState)
                    isValueProvided && appTab == TAB_CURRENCY -> CurrencyListFragment.newInstance(mainViewState)
                    else -> NoValueFragment.newInstance()
                }
                //todo вынести в центральный класс навигатор
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit()
            }
        }

        mainViewModel.calculatorIntentLiveData.observe(this) { intent ->
            if (resolveActivityFor(this, intent) != null) {
                val chooserIntent = Intent.createChooser(intent, "Choose calculator:")
                startActivity(chooserIntent)
            }
            else {
                val possibleIntent = getIntentBasedOnAppName(this, "calcul")
                if (possibleIntent != null)
                    startActivity(possibleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                else
                    Toast.makeText(this,
                            R.string.no_default_application_found,
                            Toast.LENGTH_LONG
                    ).show()
            }
        }

        mainViewModel.isSoftKeyboardFocused.observe(this) { hasFocus ->
            if (!hasFocus)
                closeSoftKeyboard(this, mainBinding.valueEditText)
        }
    }

}