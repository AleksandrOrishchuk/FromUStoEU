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

        fun newInstance(parentViewState: MainViewState): ConvertBucketListFragment {
            val argsBundle: Bundle = Bundle().apply {
                putSerializable(ARG_PARENT_STATE, parentViewState)
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

        fragmentViewModel.fragmentViewState.observe(viewLifecycleOwner) { viewState ->
            fragmentBinding.apply {

                convertRecyclerView.apply {
                    val convertBuckets = viewState.convertBucketsForRecycler
                    adapter = ConvertBucketAdapter(convertBuckets)
                    layoutManager = GridLayoutManager(context, 2)

                    Log.d(TAG, "Recycler view List received. List size: ${convertBuckets.size} items")
                }
            }
        }

        fragmentViewModel.isRecyclerViewScrolling.observe(viewLifecycleOwner) { isScrolling ->
            if (isScrolling)
                closeSoftKeyboard(requireContext(), fragmentBinding.convertRecyclerView)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentBinding.convertRecyclerView.clearOnScrollListeners()
    }


    private inner class ConvertBucketHolder(private val bucketBinding: ListItemConvertBucketBinding)
        : RecyclerView.ViewHolder(bucketBinding.root) {

        fun bind(convertBucket: ConvertBucket) {
            bucketBinding.viewModel?.apply {
                setConvertBucket(convertBucket)
            }
        }
    }


    private inner class ConvertBucketAdapter(private val convertBuckets: List<ConvertBucket>)
        : RecyclerView.Adapter<ConvertBucketHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConvertBucketHolder {
            val bucketBinding = ListItemConvertBucketBinding.inflate(
                    LayoutInflater.from(requireContext()),
                    parent,
                    false
            )
            bucketBinding.viewModel = ConvertBucketViewModel()
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