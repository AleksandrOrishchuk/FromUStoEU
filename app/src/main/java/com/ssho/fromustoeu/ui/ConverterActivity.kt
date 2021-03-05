package com.ssho.fromustoeu.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssho.fromustoeu.R
import com.ssho.fromustoeu.databinding.ActivityMainBinding
import com.ssho.fromustoeu.dependency_injection.ConverterModule.provideMainViewModelFactory
import com.ssho.fromustoeu.ui.*

private const val TAG = "MainActivity"

class ConverterActivity : AppCompatActivity() {

    private val converterViewModel: ConverterViewModel by lazy {
        ViewModelProvider(this, provideMainViewModelFactory()).get(ConverterViewModel::class.java)
    }

    private lateinit var mainBinding: ActivityMainBinding

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->

            return@OnNavigationItemSelectedListener when (item.itemId) {
                R.id.tab_calculator -> {
                    startCalculatorActivity()
                    false
                }
                R.id.tab_currency -> converterViewModel.onChangeAppTab(AppTab.TAB_CURRENCY)
                else -> converterViewModel.onChangeAppTab(AppTab.TAB_HOME)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        Log.d(TAG, "Binding applied.")

        mainBinding.apply {

            errorRetryButton.setOnClickListener {
                converterViewModel.onForceRefreshUI()
            }

            measureSystemToggleSwitch.setOnClickListener {
                converterViewModel.onChangeMeasureSystemClick()
            }

            valueEditText.setText(converterViewModel.converterUiInputData.sourceValue)

            bottomNavigation.selectedItemId = when (converterViewModel.converterUiInputData.appTab) {
                AppTab.TAB_HOME -> R.id.tab_home
                AppTab.TAB_CURRENCY -> R.id.tab_currency
            }

            bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        }

        converterViewModel.converterViewState.observe(this) { viewState ->
            applyViewState(viewState)
        }

    }

    override fun onStart() {
        super.onStart()

        mainBinding.apply {

            valueEditText.doOnTextChanged { text, _, _, _ -> converterViewModel.onSourceValueChanged(text) }

            valueEditText.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus)
                    closeSoftKeyboard(this@ConverterActivity, view)
            }

        }
    }

    private fun applyViewState(viewState: ConverterViewState) {
        mainBinding.apply {
            loadingProgressBar.isVisible = viewState is ConverterViewState.Loading
            errorMessageContainer.isVisible = viewState is ConverterViewState.Error
            fragmentContainer.isVisible =
                    viewState is ConverterViewState.Result || viewState is ConverterViewState.NoValueProvided
        }

        val fragment = when (viewState) {
            is ConverterViewState.Result -> ConversionResultListFragment
                    .newInstance(viewState.conversionResultUiList)
            is ConverterViewState.NoValueProvided -> NoValueFragment.newInstance()
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
