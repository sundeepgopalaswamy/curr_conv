package com.sundeep.demo.currconv.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sundeep.demo.currconv.R
import com.sundeep.demo.currconv.databinding.FragmentMainBinding
import com.sundeep.demo.currconv.ui.adapter.ConversionAdapter
import com.sundeep.demo.currconv.ui.adapter.CurrencyAdapter
import com.sundeep.demo.currconv.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_main, container, false)
        binding.conversionList.layoutManager = LinearLayoutManager(requireContext())
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.currencySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.updateCurrency(viewModel.allCurrencies.value[position])
                    Timber.i("onItemSelected: selected $position")
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.conversions.collect { data ->
                    Timber.i("conversions populated with " + data.size)
                    if (data.isNotEmpty()) {
                        binding.conversionList.adapter = ConversionAdapter(requireContext(), data)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataloaded.collect { dataAvailable ->
                    if (dataAvailable) {
                        val curCurrency = viewModel.curCurrency.value
                        viewModel.allCurrencies.value.forEachIndexed { index, currencyModel ->
                            if (currencyModel == curCurrency) {
                                binding.currencySpinner.adapter =
                                    CurrencyAdapter(requireContext(), viewModel.allCurrencies.value)
                                binding.currencySpinner.setSelection(index)
                                Timber.i("DataLoaded:selected ${curCurrency.name}")
                            }
                        }
                    }
                }
            }
        }
    }
}