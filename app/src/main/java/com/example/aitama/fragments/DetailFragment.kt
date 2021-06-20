package com.example.aitama.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

            }
        })

        binding.buyButton.setOnClickListener {

            viewModel.assetDto.value!!.let {

                Toast.makeText(context, it.asset.symbol, Toast.LENGTH_LONG).show()

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