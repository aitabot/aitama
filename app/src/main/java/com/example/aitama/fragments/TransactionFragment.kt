package com.example.aitama.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    ): View? {
        /* Set up Data binding */
        binding = DataBindingUtil.inflate(inflater, R.layout.transaction_fragment, container, false)

        /* Receive Arguments, add arguments to the binding*/
        val args = TransactionFragmentArgs.fromBundle(requireArguments())
        binding.assetDto = args.assetDto
        binding.transactionTypeInput = args.transactionType

        /* Set up ViewModel with Repository */
        val dataRepository = DataRepository.getInstance(requireNotNull(this.activity).application)
        val viewModelFactory = TransactionViewModelFactory(dataRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TransactionViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner

        /* Rename the fragment title*/
        (requireActivity() as MainActivity).supportActionBar?.title =
            "${binding.transactionTypeInput?.toString()} ${binding.assetDto?.asset?.name}"

        binding.transactionAmount.addTextChangedListener(textWatcher)


        binding.confirm.setOnClickListener {

            viewModel.confirmTransaction(
                assetDto = binding.assetDto,
                transactionType = args.transactionType,
                assetAmount = binding.assetAmount?.toDouble()
            )

            this.findNavController().navigate(
                TransactionFragmentDirections.actionTransactionFragmentToDetailFragment(binding.assetDto?.asset?.symbol!!)
            )

        }

        binding.cancel.setOnClickListener {
            this.findNavController().navigate(
                TransactionFragmentDirections.actionTransactionFragmentToDetailFragment(binding.assetDto?.asset?.symbol!!)
            )
        }

        return binding.root
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

            /* This part disables the confirm button in the SELL transaction
            in case the chosen amount exceeds the amount of assets in the portfolio. */
            if (binding.transactionTypeInput == TransactionType.SELL) {

                binding.assetAmount?.let {

                    if (binding.assetAmount.toString().isNotEmpty()) {

                        val transactionAmount = binding.assetAmount?.toDouble()
                        val currentAmount = sumAssetAmount(binding.assetDto?.assetTransactions)
                        if (transactionAmount != null) {
                            binding.confirm.isEnabled = transactionAmount <= currentAmount
                        }

                    }
                }
            } else if (binding.transactionTypeInput == TransactionType.BUY) {

                /* This will make sure that a valid amount is entered and will otherwise disable the confirm button */

                binding.assetAmount?.let {
                    val amount = binding.assetAmount?.toDoubleOrNull()
                    binding.confirm.isEnabled = !(amount == null || amount <= 0.0)
                }

            }


        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

}
