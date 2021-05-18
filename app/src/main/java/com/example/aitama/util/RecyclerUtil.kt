package com.example.aitama.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aitama.R
import com.example.aitama.dataclasses.AssetDetails


class AssetAdapter : RecyclerView.Adapter<AssetAdapter.ViewHolder>() {

    var data = listOf<AssetDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val assetName: TextView = itemView.findViewById(R.id.asset_name)
        val assetSymbol: TextView = itemView.findViewById(R.id.asset_symbol)
        val assetAmount: TextView = itemView.findViewById(R.id.asset_amount)
        val marketValueSum: TextView = itemView.findViewById(R.id.market_value_sum)
        val marketValuePercentage: TextView = itemView.findViewById(R.id.market_value_percentage)


        fun bind(item: AssetDetails) {

            val price = item.assetTransactions?.sumOf { it ->
                it.price.toDouble()
            }

            val amount = item.assetTransactions?.sumOf { it ->
                it.amount.toDouble()
            }

            assetName.text = item.asset.name
            assetSymbol.text = item.asset.symbol
            itemView.resources
            assetAmount.text =
                String.format(itemView.context.resources.getString(R.string.asset_amount), amount.toString())

            // todo hardcoded, muss verglichen werden mit aktuellem preis
            marketValuePercentage.text = "100%"
            // todo currently wrong, zeigt den summierten 'Einkaufspreis'
            marketValueSum.text = price.toString()


        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.asset_item_view, parent, false)
                return ViewHolder(view)
            }
        }

    }


}
