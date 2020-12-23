package com.onurkanbakirci.szutestosgb.ui.recyleviewadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.data.db.entities.Category
import com.onurkanbakirci.szutestosgb.databinding.ItemOfCategoriesBinding
import com.onurkanbakirci.szutestosgb.ui.fragments.CategoryFragmentViewModel

class CategoryAdapter (private val mCategory : List<Category>, val mContext: Context,var company:String): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    inner class CategoryViewHolder( val  itemOfCategoriesBinding: ItemOfCategoriesBinding ):
        RecyclerView.ViewHolder(itemOfCategoriesBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CategoryViewHolder{

        val mInputsFragmentViewModel = CategoryFragmentViewModel(mContext,company)

        val categoriesViewHolder = CategoryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_of_categories,
                parent,
                false
            )
        )
        categoriesViewHolder.itemOfCategoriesBinding.categoryFragmentViewModel = mInputsFragmentViewModel
        return categoriesViewHolder
    }

    override fun getItemCount(): Int {
        return mCategory.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.itemOfCategoriesBinding.category = mCategory[position]
    }
}