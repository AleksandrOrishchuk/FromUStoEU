package com.ssho.fromustoeu

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
import com.ssho.fromustoeu.converters.Converter
import com.ssho.fromustoeu.converters.CurrencyConverter
import com.ssho.fromustoeu.converters.MeasureConverter
import com.ssho.fromustoeu.databinding.FragmentConvertBucketListBinding
import com.ssho.fromustoeu.databinding.ListItemConvertBucketBinding
import com.ssho.fromustoeu.dependency_injection.currencyConverter
import com.ssho.fromustoeu.dependency_injection.measureConverter

private const val TAG = "ConvertListFragment"

class ConvertBucketListFragment : Fragment() {

    companion object {
        private const val ARG_PARENT_STATE = "parent_view_state"

        fun newInstance(parentViewState: MainViewState): ConvertBucketListFragment {

            return ConvertBucketListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARENT_STATE, parentViewState)
                }
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
        fragmentBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = fragmentViewModel

            convertRecyclerView.layoutManager = GridLayoutManager(context, 2)
            convertRecyclerView.adapter = ConvertBucketAdapter(measureConverter, currencyConverter)
        }

        return fragmentBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentBinding.convertRecyclerView.clearOnScrollListeners()
        Log.d(TAG, "Scroll listeners cleared")
    }


    internal inner class ConvertBucketHolder(
        private val bucketBinding: ListItemConvertBucketBinding
    ) : RecyclerView.ViewHolder(bucketBinding.root) {

        fun bind(convertBucket: ConvertBucket) {
            bucketBinding.viewModel?.setConvertBucketAndUpdateViewState(convertBucket)
        }
    }

    //можно заюзать ListAdapter, он вроде поудобнее.
    // Список айтемов в конструктор передавать плохо, потому что при любых изменениях тебе
    // приходится пересоздавать адаптер. Вместо этого надо делать сеттер и вызывать метод
    // notifyDataSetChanged, а адаптер создавать единожды.
    //DONE
    internal inner class ConvertBucketAdapter(
            private val measureConverter: MeasureConverter,
            private val currencyConverter: CurrencyConverter
            ) : ListAdapter<ConvertBucket, ConvertBucketHolder>(DiffUtilCallback()) {

        private var converter: Converter = measureConverter

        override fun onCurrentListChanged(previousList: MutableList<ConvertBucket>, currentList: MutableList<ConvertBucket>) {
            super.onCurrentListChanged(previousList, currentList)
            Log.d(TAG, "New list submitted, size ${currentList.size}")

            if (currentList.isEmpty())
                return

            converter = when {
//                previousList.isNotEmpty() && currentList[0] is CurrencyBucket -> currencyConverter
//                previousList.isEmpty() && currentList[0] is CurrencyBucket -> currencyConverter
                currentList[0] is CurrencyBucket -> currencyConverter
                else -> measureConverter
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConvertBucketHolder {
            val bucketBinding = ListItemConvertBucketBinding.inflate(
                LayoutInflater.from(requireContext()),
                parent,
                false
            )
            bucketBinding.viewModel = ConvertBucketViewModel(converter)
            bucketBinding.lifecycleOwner = viewLifecycleOwner

            return ConvertBucketHolder(bucketBinding)
        }

        override fun onBindViewHolder(holder: ConvertBucketHolder, position: Int) {
            val convertBucket = getItem(position)
            holder.bind(convertBucket)
        }

        override fun getItemCount(): Int = this.currentList.size
    }

    private inner class DiffUtilCallback : DiffUtil.ItemCallback<ConvertBucket>() {
        override fun areItemsTheSame(oldItem: ConvertBucket, newItem: ConvertBucket): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: ConvertBucket, newItem: ConvertBucket): Boolean {
            return false
        }
    }
}