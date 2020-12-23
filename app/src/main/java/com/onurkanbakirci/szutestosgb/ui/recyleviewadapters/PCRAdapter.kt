package com.onurkanbakirci.szutestosgb.ui.recyleviewadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.data.db.entities.PCR
import com.onurkanbakirci.szutestosgb.databinding.ItemOfPcrBinding
import com.onurkanbakirci.szutestosgb.ui.fragments.PCRViewModel

class PCRAdapter (private val mPCR : List<PCR>, val mContext: Context): RecyclerView.Adapter<PCRAdapter.PCRViewHolder>() {

    inner class PCRViewHolder(val iteOfPcrBinding: ItemOfPcrBinding) :
        RecyclerView.ViewHolder(iteOfPcrBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PCRViewHolder {

        val mPCRFragmentViewModel = PCRViewModel(mContext)

        val pcrViewHolder = PCRViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_of_pcr,
                parent,
                false
            )
        )
        pcrViewHolder.iteOfPcrBinding.pcrFragmentViewModel =
            mPCRFragmentViewModel
        return pcrViewHolder
    }

    override fun getItemCount(): Int {
        return mPCR.size
    }

    override fun onBindViewHolder(holder: PCRViewHolder, position: Int) {
        holder.iteOfPcrBinding.pcr= mPCR[position]
    }
}