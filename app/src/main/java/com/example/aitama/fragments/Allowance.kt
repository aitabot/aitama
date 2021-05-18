package com.example.aitama.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aitama.R
import com.example.aitama.viewmodel.AllowanceViewModel

class Allowance : Fragment() {

    companion object {
        fun newInstance() = Allowance()
    }

    private lateinit var viewModel: AllowanceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.allowance_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AllowanceViewModel::class.java)
        // TODO: Use the ViewModel
    }

}