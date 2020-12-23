package com.onurkanbakirci.szutestosgb.ui.recyleviewadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.data.db.entities.Company
import com.onurkanbakirci.szutestosgb.databinding.CompanyFragmentBinding
import com.onurkanbakirci.szutestosgb.databinding.ItemOfCategoriesBinding
import com.onurkanbakirci.szutestosgb.databinding.ItemOfCompanyBinding
import com.onurkanbakirci.szutestosgb.ui.fragments.CompanyFragmentViewModel

class CompanyAdapter (private val mCompany : List<Company>, val mContext: Context): RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {

    inner class CompanyViewHolder( val  itemOfCompanyFragmentBinding: ItemOfCompanyBinding):
        RecyclerView.ViewHolder(itemOfCompanyFragmentBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CompanyViewHolder{

        val mCompanyFragmentViewModel = CompanyFragmentViewModel(mContext)

        val categoriesViewHolder = CompanyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_of_company,
                parent,
                false
            )
        )
        categoriesViewHolder.itemOfCompanyFragmentBinding.companyFragmentViewModel = mCompanyFragmentViewModel
        return categoriesViewHolder
    }

    override fun getItemCount(): Int {
        return mCompany.size
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.itemOfCompanyFragmentBinding.companies = mCompany[position]
    }
}