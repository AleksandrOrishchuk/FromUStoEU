package com.ssho.fromustoeu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ssho.fromustoeu.databinding.FragmentCurrencyListBinding

class CurrencyListFragment : Fragment() {

    companion object {
        private const val ARG_PARENT_STATE = "parent_view_state"

        fun newInstance(parentViewState: MainViewState): CurrencyListFragment {
            val argsBundle: Bundle = Bundle().apply {
                putSerializable(ARG_PARENT_STATE, parentViewState)
            }

            return CurrencyListFragment().apply {
                arguments = argsBundle
            }
        }
    }

    private lateinit var fragmentBinding : FragmentCurrencyListBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        fragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_currency_list,
                container,
                false
        )
        fragmentBinding.lifecycleOwner = viewLifecycleOwner

        return fragmentBinding.root
    }
}