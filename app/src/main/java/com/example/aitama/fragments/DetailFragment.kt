package com.example.aitama.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.aitama.MainActivity
import com.example.aitama.R
import com.example.aitama.adapters.TransactionListAdapter
import com.example.aitama.databinding.DetailFragmentBinding
import com.example.aitama.repositories.DataRepository
import com.example.aitama.util.TransactionType
import com.example.aitama.viewmodel.DetailViewModel
import com.example.aitama.viewmodel.DetailViewModelFactory
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDateTime
import kotlin.collections.ArrayList


class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: DetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* Inflate the layout */
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_fragment, container, false)
        val args = DetailFragmentArgs.fromBundle(requireArguments())

        /* Create the adapter for the RecyclerView */
        val adapter = TransactionListAdapter()
        /* The following line connects the adapter to the RecyclerView */
        binding.transactionList.adapter = adapter

        /* Set up ViewModel with Repository */
        val dataRepository = DataRepository.getInstance(requireNotNull(this.activity).application)
        val viewModelFactory = DetailViewModelFactory(dataRepository, args.assetSymbol)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        viewModel.assetDto.observe(viewLifecycleOwner, {
            it?.let {

                /* Submit transactions to the adapter to update the recycler view*/
                adapter.submitList(it.assetTransactions.sortedByDescending { transaction -> transaction.date })

                /* Change the title of the fragment to the Asset name*/
                (requireActivity() as MainActivity).supportActionBar?.title =
                    viewModel.assetDto.value?.asset?.name


                val listData = ArrayList<Entry>()
                val dateData = ArrayList<String>()
                var today = LocalDateTime.now().minusDays(1)
                val len = it.assetPrices.size - 1
                val numberOfElementsToShow = 6
                val displayableAssetPrices = it.assetPrices.subList(0, numberOfElementsToShow).reversed()
                for (i in displayableAssetPrices.indices) {
                    listData.add(Entry(i.toFloat(), displayableAssetPrices[i].price))
                    dateData.add(today.dayOfMonth.toString() + "-" + today.month.toString())
                    today = today.minusDays(1)
                }
                var dateDataList = dateData.reversed()

                val lineDataSet = LineDataSet(listData, it.asset.name)
                lineDataSet.setDrawHighlightIndicators(false)
                lineDataSet.color = ContextCompat.getColor(requireContext(), R.color.black)
                lineDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.black)
                val lineData = LineData(lineDataSet)
                binding.lineChart.data = lineData
                binding.lineChart.axisRight.isEnabled = false
                binding.lineChart.description.isEnabled = false
                binding.lineChart.legend.isEnabled = false
                binding.lineChart.setDrawBorders(false)

                val formatter: ValueFormatter = object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase): String {
                        return dateDataList[value.toInt()]
                    }
                }
                binding.lineChart.xAxis.granularity = 1f
                binding.lineChart.xAxis.valueFormatter = formatter


                binding.lineChart.invalidate() //refresh

            }
        })

        binding.buyButton.setOnClickListener {

            viewModel.assetDto.value!!.let {

                this.findNavController()
                    .navigate(
                        DetailFragmentDirections.actionDetailFragmentToTransactionFragment(
                            it.asset.symbol,
                            TransactionType.BUY,
                            it.asset.name,
                            it.asset.type
                        )
                    )
            }

        }

        binding.sellButton.setOnClickListener {

            viewModel.assetDto.let {
                this.findNavController()
                    .navigate(
                        DetailFragmentDirections.actionDetailFragmentToTransactionFragment(
                            viewModel.assetDto.value?.asset!!.symbol,
                            TransactionType.SELL,
                            viewModel.assetDto.value?.asset!!.name,
                            viewModel.assetDto.value?.asset!!.type,
                        )
                    )
            }

        }


        return binding.root

    }


}