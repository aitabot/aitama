package com.example.aitama.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.aitama.R
import com.example.aitama.adapters.AssetDetailListener
import com.example.aitama.adapters.AssetListAdapter
import com.example.aitama.databinding.PortfolioFragmentBinding
import com.example.aitama.repositories.DataRepository
import com.example.aitama.viewmodel.PortfolioViewModel
import com.example.aitama.viewmodel.PortfolioViewModelFactory

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


    private fun updatePriceData() {
        viewModel.checkPriceActuality(requireContext())
    }


}