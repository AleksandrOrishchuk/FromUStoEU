package com.ssho.fromustoeu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ssho.fromustoeu.databinding.FragmentNoValueBinding

//todo этот фрагмент можно вообще без байндинга сделать =)
class NoValueFragment : Fragment() {
    companion object {
        fun newInstance(): NoValueFragment {
            return NoValueFragment()
        }
    }

    private lateinit var fragmentBinding: FragmentNoValueBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_no_value,
            container,
            false
        )
        fragmentBinding.lifecycleOwner = viewLifecycleOwner

        return fragmentBinding.root
    }
}