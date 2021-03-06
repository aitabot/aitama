package com.example.aitama.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.aitama.R
import com.example.aitama.adapters.AssetDetailListener
import com.example.aitama.adapters.AssetListAdapter
import com.example.aitama.databinding.PortfolioFragmentBinding
import com.example.aitama.dataclasses.AssetDto
import com.example.aitama.repositories.DataRepository
import com.example.aitama.util.sumAssetValue
import com.example.aitama.util.sumTransactions
import com.example.aitama.viewmodel.PortfolioViewModel
import com.example.aitama.viewmodel.PortfolioViewModelFactory
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.abs


class PortfolioFragment : Fragment() {


    private lateinit var viewModel: PortfolioViewModel
    private lateinit var binding: PortfolioFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /* Inflate the layout */
        binding = DataBindingUtil.inflate(inflater, R.layout.portfolio_fragment, container, false)

        /* Create the adapter for the RecyclerView */
        val adapter = AssetListAdapter(AssetDetailListener { symbol ->
            viewModel.onAssetDetailClicked(symbol)
        })
        binding.assetList.adapter = adapter

        /* Set up ViewModel with Repository */
        val dataRepository = DataRepository.getInstance(requireNotNull(this.activity).application)
        val viewModelFactory = PortfolioViewModelFactory(dataRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PortfolioViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        /* Observe the combined Asset Details list */
        viewModel.assetDtos.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it.sortedBy { dto -> dto.asset.name })
                updatePriceData()

                /** PieChart BEGIN */
                val distribution = calculateDistribution(it)
                val pieDataEntries = ArrayList<PieEntry>()
                val labelData = ArrayList<String>()

                var index = 0
                for (asset in distribution) {
                    pieDataEntries.add(PieEntry(asset.value.toFloat(), asset.key))
                    labelData.add(asset.key)
                    index++
                }


                val pieDataSet = PieDataSet(pieDataEntries, "")
                pieDataSet.setColors(
                    intArrayOf(
                        R.color.purple_200,
                        R.color.teal_200,
                        R.color.pink,
                        R.color.orange
                    ), context
                )
                pieDataSet.valueTextSize = 14f
                pieDataSet.getValueTextColor(R.color.black)

                val pieData = PieData(pieDataSet)
                // pieData.setValueTextColor(R.color.black)
                val formatter: ValueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "%.2f".format(value) + "%"
                    }
                }
                pieData.setValueFormatter(formatter)
                binding.pieChart.data = pieData
                binding.pieChart.description.isEnabled = false
                //binding.pieChart.legend.isEnabled = false
                binding.pieChart.legend.xOffset = 24f
                binding.pieChart.setEntryLabelColor(R.color.black)
                binding.pieChart.isRotationEnabled = false
                binding.pieChart.invalidate() //refresh
                /** PieChart END */

            }
        })


        viewModel.navigateToAssetDetail.observe(viewLifecycleOwner, Observer { assetDto ->
            assetDto?.let {
                this.findNavController().navigate(
                    PortfolioFragmentDirections
                        .actionPortfolioFragmentToDetailFragment(assetDto)
                )
                viewModel.onAssetDetailNavigated()
            }
        })
        return binding.root
    }

    private fun calculateDistribution(it: List<AssetDto>): Map<String, Double> {
        val distribution = HashMap<String, Double>()
        var sum = 0.0
        for (asset in it) {
            distribution[asset.asset.name] = sumAssetValue(asset)
            sum += distribution[asset.asset.name]!!
        }
        for (asset in distribution) {
            asset.setValue(asset.value / sum * 100)
        }

        return distribution.filter { entry -> entry.value != -0.0 }
    }


    private fun updatePriceData() {
        viewModel.checkPriceActuality(requireContext())
    }


}