package com.example.aitama.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aitama.databinding.ListItemAssetBinding
import com.example.aitama.dataclasses.AssetDetail


class AssetDetailDiffCallback : DiffUtil.ItemCallback<AssetDetail>() {

    override fun areItemsTheSame(oldItem: AssetDetail, newItem: AssetDetail): Boolean {
        return oldItem.asset.symbol == newItem.asset.symbol
    }

    override fun areContentsTheSame(oldItem: AssetDetail, newItem: AssetDetail): Boolean {
        return oldItem == newItem
    }

}


class AssetDetailAdapter :
    ListAdapter<AssetDetail, AssetDetailAdapter.ViewHolder>(AssetDetailDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemAssetBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: AssetDetail) {

            binding.assetDetail = item
            binding.executePendingBindings()


//            val price = item.assetTransactions?.sumOf { it ->
//                it.price.toDouble()
//            }
//
//            val amount = item.assetTransactions?.sumOf { it ->
//                it.amount.toDouble()
//            }
//
//            binding.assetName.text = item.asset.name
//            binding.assetSymbol.text = item.asset.symbol
//            itemView.resources
//            binding.assetAmount.text =
//                String.format(
//                    itemView.context.resources.getString(R.string.asset_amount),
//                    amount.toString()
//                )
//
//            // todo hardcoded, muss verglichen werden mit aktuellem preis
//            binding.marketValuePercentage.text = "100%"
//            // todo currently wrong, zeigt den summierten 'Einkaufspreis'
//            binding.marketValueSum.text = price.toString()


        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAssetBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }


}
