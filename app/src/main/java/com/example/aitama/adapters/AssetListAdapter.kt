package com.example.aitama.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aitama.databinding.ListItemAssetOverviewBinding
import com.example.aitama.dataclasses.AssetDto


class AssetDetailDiffCallback : DiffUtil.ItemCallback<AssetDto>() {

    override fun areItemsTheSame(oldItem: AssetDto, newItem: AssetDto): Boolean {
        return oldItem.asset.symbol == newItem.asset.symbol
    }

    override fun areContentsTheSame(oldItem: AssetDto, newItem: AssetDto): Boolean {
        return oldItem == newItem
    }

}


class AssetListAdapter(val clickListener: AssetDetailListener) :
    ListAdapter<AssetDto, AssetListAdapter.ViewHolder>(AssetDetailDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemAssetOverviewBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: AssetDto, clickListener: AssetDetailListener) {
            binding.clickListener = clickListener
            binding.assetDto = item
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAssetOverviewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }


}

class AssetDetailListener(val clickListener: (symbol: String) -> Unit) {

    fun onClick(assetDto: AssetDto) = clickListener(assetDto.asset.symbol)

}


