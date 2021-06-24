package com.example.aitama.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.aitama.MainActivity
import com.example.aitama.R
import com.example.aitama.databinding.TransactionFragmentBinding
import com.example.aitama.repositories.DataRepository
import com.example.aitama.util.TransactionType
import com.example.aitama.util.sumAssetAmount
import com.example.aitama.viewmodel.TransactionViewModel
import com.example.aitama.viewmodel.TransactionViewModelFactory

class TransactionFragment() : Fragment() {

    private lateinit var binding: TransactionFragmentBinding
    private lateinit var viewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        /* Set up Data binding */
        binding = DataBindingUtil.inflate(inflater, R.layout.transaction_fragment, container, false)

        /* Get preferences */
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)

        /* Receive Arguments*/
        val args = TransactionFragmentArgs.fromBundle(requireArguments())

        /* Set up ViewModel with Repository */
        val dataRepository = DataRepository.getInstance(requireNotNull(this.activity).application)
        val viewModelFactory = TransactionViewModelFactory(
            dataRepository = dataRepository,
            preferences = pref,
            symbol = args.symbol,
            name = args.name,
            assetType = args.assetType,
            transactionType = args.transactionType
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(TransactionViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        /* Add arguments to the binding*/
        binding.transactionTypeInput = args.transactionType


        /* Rename the fragment title*/
        (requireActivity() as MainActivity).supportActionBar?.title =
            "${args.transactionType} ${args.name}"


        viewModel.transactions.observe(viewLifecycleOwner, {
            viewModel.loadAllowance()
        })

        viewModel.assetDto.observe(viewLifecycleOwner, {
            it?.let {
                viewModel.updateTransactionPrice()
                Log.d("assetDto", viewModel.assetDto.value.toString())
                Log.d("assetPrice", viewModel.transactionPrice.value.toString())
            }
        })


        viewModel.transactionAmount.observe(viewLifecycleOwner, {

            viewModel.assetDto.value?.let {

                viewModel.updateTransactionPrice()
                viewModel.updateRemainingAllowanceAfterTransaction(args.transactionType)
                val amount = viewModel.transactionAmount.value?.toDoubleOrNull()
                val allowance = viewModel.remainingAllowance.value?.toDoubleOrNull()
                val transactionPrice = viewModel.transactionPrice.value?.toDoubleOrNull()

                amount?.let {

                    allowance?.let {

                        transactionPrice?.let {

                            if (binding.transactionTypeInput == TransactionType.BUY) {

                                binding.confirm.isEnabled =
                                    !(amount <= 0.0 || transactionPrice > allowance)

                            } else if (binding.transactionTypeInput == TransactionType.SELL) {

                                viewModel.transactionAmount.value.let {

                                    if (viewModel.transactionAmount.value.toString().isNotEmpty()) {
                                        val transactionAmount =
                                            viewModel.transactionAmount.value?.toDouble()
                                        val currentAmount =
                                            sumAssetAmount(viewModel.assetDto.value?.assetTransactions)
                                        if (transactionAmount != null) {
                                            binding.confirm.isEnabled =
                                                transactionAmount <= currentAmount
                                        }
                                    } else {
                                        binding.confirm.isEnabled = false
                                    }
                                }

                            }

                        }


                    }
                }
            }
        })


        binding.confirm.setOnClickListener {

            viewModel.confirmTransaction(
                assetDto = viewModel.assetDto.value,
                transactionType = args.transactionType,
                assetAmount = viewModel.transactionAmount.value?.toDouble()
            )

            this.findNavController().navigate(
                TransactionFragmentDirections.actionTransactionFragmentToDetailFragment(args.symbol)
            )

        }

        binding.cancel.setOnClickListener {
            this.findNavController().navigate(
                TransactionFragmentDirections.actionTransactionFragmentToDetailFragment(args.symbol)
            )
        }

        return binding.root
    }

}
