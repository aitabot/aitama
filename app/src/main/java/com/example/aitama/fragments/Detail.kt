package com.example.aitama.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aitama.R
import com.example.aitama.adapters.TransactionListAdapter
import com.example.aitama.databinding.DetailFragmentBinding
import com.example.aitama.repositories.DataRepository
import com.example.aitama.viewmodel.DetailViewModel
import com.example.aitama.viewmodel.DetailViewModelFactory

class Detail : Fragment() {

    companion object {
        fun newInstance() = Detail()
    }

    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: DetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* Inflate the layout */
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_fragment, container, false)
        val args = DetailArgs.fromBundle(requireArguments())

        /* Create the adapter for the RecyclerView */
        val adapter = TransactionListAdapter()
        /* The following line connects the adapter to the RecyclerView */
        binding.transactionList.adapter = adapter

        /* Set up ViewModel with Repository */
        val dataRepository = DataRepository.getInstance(requireNotNull(this.activity).application)
        val viewModelFactory = DetailViewModelFactory(dataRepository, args.assetSymbol)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.transactionList.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root

    }

}