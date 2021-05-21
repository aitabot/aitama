package com.example.aitama.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aitama.R
import com.example.aitama.dao.AitamaDatabase
import com.example.aitama.databinding.PortfolioFragmentBinding
import com.example.aitama.repositories.DataRepository
import com.example.aitama.util.AssetDetailAdapter
import com.example.aitama.viewmodel.PortfolioViewModel
import com.example.aitama.viewmodel.PortfolioViewModelFactory

class Portfolio : Fragment() {

    companion object {
        fun newInstance() = Portfolio()
    }

    private lateinit var viewModel: PortfolioViewModel
    private lateinit var binding: PortfolioFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.portfolio_fragment, container, false)
        val adapter = AssetDetailAdapter()
        binding.assetList.adapter = adapter

        /* Set up ViewModel with Repository */
        val application = requireNotNull(this.activity).application
        val assetDao = AitamaDatabase.getInstance(application).assetDao
        val assetTransactionDao = AitamaDatabase.getInstance(application).assetTransactionDao
        val dataRepository = DataRepository.getInstance(assetDao, assetTransactionDao)
        val viewModelFactory = PortfolioViewModelFactory(dataRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PortfolioViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner

        /* Observe the combined Asset Details list */
        viewModel.combinedLiveData.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root

    }

}