package com.onurkanbakirci.szutestosgb.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.databinding.CompanyFragmentBinding
import com.onurkanbakirci.szutestosgb.ui.recyleviewadapters.CompanyAdapter
import kotlinx.android.synthetic.main.company_fragment.*

class CompanyFragment :Fragment() {

    private var mCompanyViewModel : CompanyFragmentViewModel? =null

    private var mCompanyFragmentBinding : CompanyFragmentBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getActivity()?.setTitle("Firma Seçiniz")

        mCompanyViewModel =
            ViewModelProviders.of(requireActivity(),CompanyFragmentViewModelFactory(requireContext())).get(CompanyFragmentViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mCompanyFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.company_fragment,container,false)
        mCompanyFragmentBinding?.companyFragmentViewModel = mCompanyViewModel
        mCompanyViewModel?.getCompanies()
        return mCompanyFragmentBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mCompanyViewModel?.mCompany?.observe(viewLifecycleOwner, Observer { companies ->
            item_of_company_recycleview.also {
                it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                it.setHasFixedSize(true)
                it.adapter=
                    CompanyAdapter(companies,requireContext())
            }
            mCompanyViewModel?.progressOfCompany?.set(8)
        })
    }
}