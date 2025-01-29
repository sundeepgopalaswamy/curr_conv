package com.sundeep.demo.currconv.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sundeep.demo.currconv.data.models.ConversionPairModel
import com.sundeep.demo.currconv.databinding.CurrencyBinding
import java.util.Locale

class ConversionAdapter(
    private val context: Context,
    private val conversions: List<ConversionPairModel>
) : RecyclerView.Adapter<ConversionAdapter.ConversionViewHolder>() {
    inner class ConversionViewHolder(var view: CurrencyBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversionViewHolder {
        val binding = CurrencyBinding.inflate(LayoutInflater.from(context), parent, false)
        return ConversionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return conversions.size
    }

    override fun onBindViewHolder(holder: ConversionViewHolder, position: Int) {
        val view = holder.view
        view.name.text = conversions[position].toCurrency.name
        view.rate.text =
            String.format(locale = Locale.getDefault(), "%.4f", conversions[position].rate)
        Glide.with(context).load(conversions[position].toCurrency.flagUrl).into(view.flag)
    }
}