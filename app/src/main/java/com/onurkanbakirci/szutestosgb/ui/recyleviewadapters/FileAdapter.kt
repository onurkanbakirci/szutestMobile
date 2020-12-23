package com.onurkanbakirci.szutestosgb.ui.recyleviewadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.data.db.entities.Files
import com.onurkanbakirci.szutestosgb.databinding.ItemOfFileBinding
import com.onurkanbakirci.szutestosgb.ui.fragments.RecentFragmentViewModel

class FileAdapter (private val mFile : List<Files>, val mContext: Context): RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    inner class FileViewHolder(val itemItemOfFileBinding: ItemOfFileBinding) :
        RecyclerView.ViewHolder(itemItemOfFileBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {

        val mRecentFragmentViewModel = RecentFragmentViewModel(mContext)

        val categoriesViewHolder = FileViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_of_file,
                parent,
                false
            )
        )
        categoriesViewHolder.itemItemOfFileBinding.recentFragmentViewModel =
            mRecentFragmentViewModel
        return categoriesViewHolder
    }

    override fun getItemCount(): Int {
        return mFile.size
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.itemItemOfFileBinding.file = mFile[position]
    }
}