package com.ssho.fromustoeu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssho.fromustoeu.databinding.FragmentConvertBucketListBinding
import com.ssho.fromustoeu.databinding.ListItemConvertBucketBinding

private const val TAG = "ConvertListFragment"

class ConvertBucketListFragment : Fragment() {

    companion object {
        private const val ARG_PARENT_STATE = "parent_view_state"
        private const val ARG_VALUE = "current_value"

        fun newInstance(parentViewState: MainViewState,
                        currentValue: Double): ConvertBucketListFragment {
            val argsBundle: Bundle = Bundle().apply {
                putSerializable(ARG_PARENT_STATE, parentViewState)
                putDouble(ARG_VALUE, currentValue)
            }

            return ConvertBucketListFragment().apply {
                arguments = argsBundle
            }
        }
    }


    private val fragmentViewModel: CBListFragmentViewModel by lazy {
        ViewModelProvider(
                this,
                CBListFragmentViewModelFactory(parentViewState)
        ).get(CBListFragmentViewModel::class.java)
    }

    private lateinit var fragmentBinding: FragmentConvertBucketListBinding
    private lateinit var parentViewState: MainViewState
    private var currentValue: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewState = arguments?.getSerializable(ARG_PARENT_STATE) as MainViewState
        currentValue = arguments?.getDouble(ARG_VALUE) ?: 0.0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_convert_bucket_list,
                container,
                false
        )
        fragmentBinding.lifecycleOwner = viewLifecycleOwner
        fragmentBinding.viewModel = fragmentViewModel

        return fragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "Value received in ListFragment: $currentValue")

        fragmentViewModel.convertBucketsLiveData.observe(viewLifecycleOwner) { convertBuckets ->
            fragmentBinding.apply {

                convertRecyclerView.apply {
                    adapter = ConvertBucketAdapter(convertBuckets.toTypedArray())
                    layoutManager = GridLayoutManager(context, 2)

                    Log.d(TAG, "Recycler view List received. List size: ${convertBuckets.size} items")
                }
            }
        }
    }


    private inner class ConvertBucketHolder(private val bucketBinding: ListItemConvertBucketBinding)
        : RecyclerView.ViewHolder(bucketBinding.root) {

        fun bind(convertBucket: ConvertBucket) {
            val sourceMeasureUnitNameResId = resources.getIdentifier(convertBucket.measureTypeFrom,
                    "string",
                    requireContext().applicationInfo.packageName
            )

            val targetMeasureUnitNameResId = resources.getIdentifier(convertBucket.measureTypeTo,
                    "string",
                    requireContext().applicationInfo.packageName
            )

            bucketBinding.apply {
                viewModel?.apply {
                    setConvertBucket(
                            convertBucket,
                            sourceMeasureUnitNameResId,
                            targetMeasureUnitNameResId)
                }
            }
        }
    }


    private inner class ConvertBucketAdapter(private val convertBuckets: Array<ConvertBucket>)
        : RecyclerView.Adapter<ConvertBucketHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConvertBucketHolder {
            val bucketBinding = ListItemConvertBucketBinding.inflate(
                    LayoutInflater.from(requireContext()),
                    parent,
                    false
            )
            bucketBinding.viewModel = ConvertBucketViewModel(currentValue)
            bucketBinding.lifecycleOwner = viewLifecycleOwner

            return ConvertBucketHolder(bucketBinding)
        }

        override fun onBindViewHolder(holder: ConvertBucketHolder, position: Int) {
            val convertBucket = convertBuckets[position]
            holder.bind(convertBucket)
        }

        override fun getItemCount(): Int = convertBuckets.size

    }
}