package com.sainivik.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sainivik.`interface`.OptionClickListener
import com.sainivik.exoplayersample.R
import com.sainivik.exoplayersample.databinding.ItemSelectOptionsBinding
import com.sainivik.model.LanguageModel


class SelectLanguageAdapter(
    private var list: ArrayList<LanguageModel>,
    private var listener: OptionClickListener
) : RecyclerView.Adapter<SelectLanguageAdapter.MyViewHolder>() {
    private var activity: Activity? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DataBindingUtil.inflate<ItemSelectOptionsBinding>(
            LayoutInflater.from(parent.context), R.layout.item_select_options, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.model = list[position]
        holder.binding.llMain.setOnClickListener {
            listener.click(position, list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class MyViewHolder(private val mBinding: ItemSelectOptionsBinding) :
        ViewHolder(mBinding.root) {
        val binding = mBinding

    }


}