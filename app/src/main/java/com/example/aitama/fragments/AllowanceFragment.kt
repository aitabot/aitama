package com.example.aitama.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aitama.R
import com.example.aitama.databinding.AllowanceFragmentBinding
import com.example.aitama.repositories.DataRepository
import com.example.aitama.viewmodel.AllowanceViewModel
import com.example.aitama.viewmodel.AllowanceViewModelFactory

class AllowanceFragment : Fragment() {

    private lateinit var binding: AllowanceFragmentBinding
    private lateinit var viewModel: AllowanceViewModel
    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.allowance_fragment, container, false)

        /* Get Preferences */
        pref = requireActivity().getPreferences(Context.MODE_PRIVATE)

        /* Set up ViewModel with Repository */
        val dataRepository = DataRepository.getInstance(requireNotNull(this.activity).application)
        val viewModelFactory = AllowanceViewModelFactory(dataRepository, pref)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AllowanceViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.allowanceSwitch.observe(viewLifecycleOwner, {
            updateAllowanceVisibility()
        })

        /* Change the visibility of the allowance card */
        binding.allowanceSwitch.setOnClickListener {
            setAllowanceVisibility()
        }

        binding.confirmAllowance.setOnClickListener {
            viewModel.setAllowance()
        }

        viewModel.assetTransactions.observe(viewLifecycleOwner, {
            viewModel.updateRemainingAllowance()
        })

        viewModel.amountEditText.observe(viewLifecycleOwner, { string ->
            val amount = viewModel.amountEditText.value.toString().toDoubleOrNull()
            binding.confirmAllowance.isEnabled = !(amount == null || amount <= 0.0)
        })

        return binding.root
    }


    private fun updateAllowanceVisibility() {
        binding.allowanceSettings.isVisible = binding.allowanceSwitch.isChecked
    }

    private fun setAllowanceVisibility() {
        updateAllowanceVisibility()
        if (!binding.allowanceSwitch.isChecked) {
            viewModel.resetAllowance()
        }
    }

}