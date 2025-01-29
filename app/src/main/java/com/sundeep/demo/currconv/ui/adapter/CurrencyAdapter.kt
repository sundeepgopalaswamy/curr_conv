package com.sundeep.demo.currconv.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.sundeep.demo.currconv.data.models.CurrencyModel
import com.sundeep.demo.currconv.databinding.CurrencyBinding

class CurrencyAdapter(context: Context, private val currencies: List<CurrencyModel>) :
    ArrayAdapter<CurrencyModel>(context, 0, currencies) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = CurrencyBinding.inflate(LayoutInflater.from(context), parent, false)
        val currency = currencies[position]
        binding.name.text = currency.name
        binding.rate.text = ""
        Glide.with(context).load(currency.flagUrl).into(binding.flag)
        return binding.root
    }
}