package com.example.aitama.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aitama.R
import com.example.aitama.databinding.AllowanceFragmentBinding
import com.example.aitama.util.formatDollar
import com.example.aitama.viewmodel.AllowanceViewModel

class Allowance : Fragment() {

    private lateinit var binding: AllowanceFragmentBinding
    private lateinit var viewModel: AllowanceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // todo move data to viewmodel?
        // todo find out why remaining allowance is not being calculated correctly

        viewModel = ViewModelProvider(this).get(AllowanceViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.allowance_fragment, container, false)
        val pref = activity?.getPreferences(Context.MODE_PRIVATE)
        updateData(pref)
        setVisibility()

        binding.allowanceSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            checkUncheckAllowance(pref)
            setVisibility()
        }

        binding.confirmAllowance.setOnClickListener {
            setAllowance(pref)
            updateData(pref)
        }
        binding.setAllowanceEditText.addTextChangedListener(textWatcher)
        return binding.root
    }

    private fun updateData(pref: SharedPreferences?) {

        binding.allowanceSwitch.isChecked =
            pref?.getString(getString(R.string.allowance_switch), "true").toBoolean()

        binding.currentAllowanceText.text = formatDollar(
            pref?.getString(
                getString(R.string.set_allowance_edit_text),
                "0"
            )?.toDouble()
        )
        binding.remainingAllowanceText.text = formatDollar(
            calculateRemainingAllowance(pref)
        )
    }

    private fun calculateRemainingAllowance(pref: SharedPreferences?): Double? {

        val currentAllowance = pref?.getString(
            getString(R.string.current_allowance_text),
            "0"
        )?.toDouble()

        val spentAllowance = pref?.getString(
            getString(R.string.spent_allowance),
            "0"
        )?.toDouble()

        return currentAllowance?.minus(spentAllowance!!)

    }


    private fun checkUncheckAllowance(pref: SharedPreferences?) {

        with(pref?.edit()) {

            this?.putString(
                getString(R.string.allowance_switch),
                binding.allowanceSwitch.isChecked.toString()
            )

            this?.apply()
        }

    }


    private fun setVisibility() {
        binding.allowanceSettings.isVisible = binding.allowanceSwitch.isChecked
    }

    private fun setAllowance(pref: SharedPreferences?) {

        with(pref?.edit()) {

            this?.putString(
                getString(R.string.current_allowance_text),
                binding.setAllowanceEditText.text.toString()
            )
            this?.commit()
        }
        binding.setAllowanceEditText.setText("")

        Toast.makeText(context, "Settings saved!", Toast.LENGTH_LONG).show()

    }


    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

            binding.setAllowanceEditText.let {

                val amount = binding.setAllowanceEditText.text.toString().toDoubleOrNull()
                binding.confirmAllowance.isEnabled = !(amount == null || amount <= 0.0)

            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }


}