package com.ssho.fromustoeu

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssho.fromustoeu.databinding.ActivityMainBinding
import com.ssho.fromustoeu.dependency_injection.provideMainViewModelFactory
import com.ssho.fromustoeu.ui.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this, provideMainViewModelFactory()).get(MainViewModel::class.java)
    }

    private lateinit var mainBinding: ActivityMainBinding

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.tab_calculator) {
                startCalculatorActivity()

                return@OnNavigationItemSelectedListener false
            }

            return@OnNavigationItemSelectedListener mainViewModel.onChangeAppTab(item.itemId)
        }

    private val valueWatcher = object : TextWatcher {
        override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
        ) {
            mainViewModel.onSourceValueChanged(charSequence)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //nothing
        }

        override fun afterTextChanged(s: Editable?) {
            //nothing
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        Log.d(TAG, "Binding applied.")

        refreshUI()

        mainViewModel.mainViewState.observe(this) { viewState ->
            applyViewState(viewState)
        }
    }

    private fun refreshUI() {
        mainBinding.apply {

            errorRetryButton.setOnClickListener {
                mainViewModel.onForceRefreshUI()
            }

            measureSystemToggleSwitch.setOnClickListener {
                mainViewModel.onChangeMeasureSystemClick()
            }

            bottomNavigation.selectedItemId = mainViewModel.appTab
            bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

            valueEditText.apply {
                setText(mainViewModel.sourceValueEditText)
                addTextChangedListener(valueWatcher)
                setOnFocusChangeListener { view, hasFocus ->
                    if (!hasFocus)
                        closeSoftKeyboard(context, view)
                }
            }
        }
    }

    private fun applyViewState(viewState: MainViewState) {
        mainBinding.apply {
            loadingProgressBar.isVisible = viewState is MainViewState.Loading
            errorMessageContainer.isVisible = viewState is MainViewState.Error
            fragmentContainer.isVisible =
                    viewState is MainViewState.Result || viewState is MainViewState.NoSourceValue
        }

        val fragment = when (viewState) {
            is MainViewState.Result -> ConversionResultListFragment
                    .newInstance(viewState.conversionResultUiList)
            is MainViewState.NoSourceValue -> NoValueFragment.newInstance()
            else -> ConversionResultListFragment.newInstance(emptyList())
        }
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        Log.d(TAG, "Fragment replaced")
    }

    private fun startCalculatorActivity() {
        val intent = Intent.makeMainSelectorActivity(
                Intent.ACTION_MAIN,
                Intent.CATEGORY_APP_CALCULATOR
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        if (resolveActivityFor(this, intent) != null) {
            val chooserIntent = Intent.createChooser(intent, "Choose calculator:")
            startActivity(chooserIntent)
        }
        else {
            val possibleIntent = getIntentBasedOnAppName(this, "calcul")
            if (possibleIntent != null)
                startActivity(possibleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            else
                showLongToast(this, R.string.no_default_application_found)
        }
    }
}