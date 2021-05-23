package com.example.aitama.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.aitama.MainActivity
import com.example.aitama.R
import com.example.aitama.databinding.TransactionFragmentBinding

class TransactionFragment() : Fragment() {

    private lateinit var binding: TransactionFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.transaction_fragment, container, false)
        val args = TransactionFragmentArgs.fromBundle(requireArguments())
        binding.assetDto = args.assetDto
        binding.transactionTypeInput = args.transactionType

        (requireActivity() as MainActivity).supportActionBar?.title =
            "${binding.transactionTypeInput?.toString()} ${binding.assetDto?.asset?.name}"

        return binding.root
    }

}
