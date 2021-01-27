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
import com.ssho.fromustoeu.R
import com.ssho.fromustoeu.databinding.FragmentConversionResultListBinding
import com.ssho.fromustoeu.databinding.ListItemConversionResultBinding

private const val TAG = "ConvertListFragment"

class ConversionResultListFragment : Fragment() {

    companion object {
        private const val ARG_PARENT_STATE = "parent_view_state"

        fun newInstance(parentViewState: MainViewState): ConversionResultListFragment {

            return ConversionResultListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARENT_STATE, parentViewState)
                }
            }
        }
    }

    private val fragmentViewModel: CBListFragmentViewModel by lazy {
        ViewModelProvider(
            this,
            CBListFragmentViewModelFactory(parentViewState.conversionResultUiList)
        ).get(CBListFragmentViewModel::class.java)
    }

    private lateinit var fragmentBinding: FragmentConversionResultListBinding
    private lateinit var parentViewState: MainViewState


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewState = arguments?.getSerializable(ARG_PARENT_STATE) as MainViewState
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