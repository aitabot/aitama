package com.example.aitama.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aitama.databinding.ListItemTransactionBinding
import com.example.aitama.dataclasses.AssetTransaction


class TransactionListDiffCallback : DiffUtil.ItemCallback<AssetTransaction>() {

    override fun areItemsTheSame(oldItem: AssetTransaction, newItem: AssetTransaction): Boolean {
        return oldItem.transactionId == newItem.transactionId
    }

    override fun areContentsTheSame(oldItem: AssetTransaction, newItem: AssetTransaction): Boolean {
        return oldItem == newItem
    }

}


class TransactionListAdapter() :
    ListAdapter<AssetTransaction, TransactionListAdapter.ViewHolder>(TransactionListDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: AssetTransaction) {
            binding.assetTransaction = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTransactionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }


}

