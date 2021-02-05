package com.ssho.fromustoeu.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssho.fromustoeu.R
import com.ssho.fromustoeu.databinding.FragmentConversionResultListBinding
import com.ssho.fromustoeu.databinding.ListItemConversionResultBinding

private const val TAG = "ConvertListFragment"

class ConversionResultListFragment : Fragment() {

    companion object {
        private const val CONVERSION_RESULT_LIST = "conversion_result_list"

        fun newInstance(conversionResultList: List<ConversionResultUi>): ConversionResultListFragment {

            return ConversionResultListFragment().apply {
                arguments = Bundle().apply {
                    putString(CONVERSION_RESULT_LIST, Gson().toJson(conversionResultList))
                }
            }
        }
    }

    private val fragmentViewModel: CResultListFragmentViewModel by lazy {
        ViewModelProvider(
            this,
            CResultListFragmentViewModelFactory(conversionResultList)
        ).get(CResultListFragmentViewModel::class.java)
    }

    private lateinit var fragmentBinding: FragmentConversionResultListBinding
    private lateinit var conversionResultList: List<ConversionResultUi>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        conversionResultList =
                Gson().fromJson(
                        arguments?.getString(CONVERSION_RESULT_LIST),
                        object : TypeToken<List<ConversionResultUi>>() {}.type
                )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_conversion_result_list,
            container,
            false
        )
        fragmentBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = fragmentViewModel

            convertRecyclerView.layoutManager = GridLayoutManager(context, 2)
            convertRecyclerView.adapter = ConversionResultAdapter()
        }

        return fragmentBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentBinding.convertRecyclerView.clearOnScrollListeners()
        Log.d(TAG, "Scroll listeners cleared")
    }


    internal inner class ConversionResultHolder(
        private val conversionResultBinding: ListItemConversionResultBinding
    ) : RecyclerView.ViewHolder(conversionResultBinding.root) {

        fun bind(conversionResultUi: ConversionResultUi) {
            conversionResultBinding.viewModel?.mapConversionResult(conversionResultUi)
        }
    }


    internal inner class ConversionResultAdapter : ListAdapter<ConversionResultUi, ConversionResultHolder>(DiffUtilCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversionResultHolder {
            val conversionResultBinding = ListItemConversionResultBinding.inflate(
                LayoutInflater.from(requireContext()),
                parent,
                false
            )
            conversionResultBinding.viewModel = ConversionResultViewModel()
            conversionResultBinding.lifecycleOwner = viewLifecycleOwner

            return ConversionResultHolder(conversionResultBinding)
        }

        override fun onBindViewHolder(holder: ConversionResultHolder, position: Int) {
            val conversionResultUi = getItem(position)
            holder.bind(conversionResultUi)
        }

        override fun getItemCount(): Int = this.currentList.size
    }

    private inner class DiffUtilCallback : DiffUtil.ItemCallback<ConversionResultUi>() {
        override fun areItemsTheSame(oldItem: ConversionResultUi, newItem: ConversionResultUi): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: ConversionResultUi, newItem: ConversionResultUi): Boolean {
            return false
        }

    }
}