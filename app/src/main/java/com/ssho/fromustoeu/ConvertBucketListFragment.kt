package com.ssho.fromustoeu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssho.fromustoeu.databinding.FragmentConvertBucketListBinding
import com.ssho.fromustoeu.databinding.ListItemConvertBucketBinding

private const val TAG = "ConvertBucketListFragment"

class ConvertBucketListFragment : Fragment() {

    companion object {
        private const val ARG_STATE = "app_state"
        fun newInstance(appState: AppState): ConvertBucketListFragment {
            val argsBundle: Bundle = Bundle().apply {
                putSerializable(ARG_STATE, appState)
            }
            return ConvertBucketListFragment().apply {
                arguments = argsBundle
            }
        }
    }

//    private val fragmentViewModel: CBListFragmentViewModel by lazy {
//        ViewModelProvider(this).get(CBListFragmentViewModel::class.java)
//    }

    private lateinit var binding: FragmentConvertBucketListBinding
    private lateinit var appState: AppState


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appState = arguments?.getSerializable(ARG_STATE) as AppState

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_convert_bucket_list, container, false)
        binding.apply {
//            viewModel = fragmentViewModel
            convertBucketRecyclerView.layoutManager = GridLayoutManager(context, 2)
            convertBucketRecyclerView.adapter = ConvertBucketAdapter(getConversionTypesList())
        }

        return binding.root
    }

    private fun getConversionTypesList(): Array<String> {
        val convertFrom: Int = appState.convertFrom
        return when (appState.appTab) {
            TAB_HOME -> {
                if (convertFrom == CONVERT_FROM_US)
                    resources.getStringArray(R.array.home_tab_conversions_from_us)
                else
                    resources.getStringArray(R.array.home_tab_conversions_from_eu)
            }
            else -> emptyArray()
        }
    }


    private inner class ConvertBucketHolder(private val binding: ListItemConvertBucketBinding)
        : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewModel = ConvertBucketViewModel(appState.inputValue)
        }

        fun bind(convertToType: String) {
            binding.viewModel!!.convertToType = convertToType
        }
    }

    private inner class ConvertBucketAdapter(private val conversionTypeList: Array<String>)
        : RecyclerView.Adapter<ConvertBucketHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConvertBucketHolder {
            val binding = ListItemConvertBucketBinding
                .inflate(LayoutInflater.from(requireContext()), parent, false)

            return ConvertBucketHolder(binding)
        }

        override fun onBindViewHolder(holder: ConvertBucketHolder, position: Int) {
            val convertToType = conversionTypeList[position]
            holder.bind(convertToType)
        }

        override fun getItemCount(): Int {
            return conversionTypeList.size
        }
    }
}