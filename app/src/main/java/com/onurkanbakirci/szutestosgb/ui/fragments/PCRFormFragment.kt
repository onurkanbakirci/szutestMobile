package com.onurkanbakirci.szutestosgb.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.databinding.PcrFormFragmentBinding

class PCRFormFragment :Fragment() {

    private var mPcrFormViewModel : PCRFormFragmentViewModel? =null

    private var mPcrFormFragmentBinding : PcrFormFragmentBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPcrFormViewModel =
            ViewModelProviders.of(requireActivity(),PCRFormViewModelFactory(requireContext())).get(PCRFormFragmentViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mPcrFormFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.pcr_form_fragment,container,false)
        mPcrFormFragmentBinding?.pcrFormFragmentViewModel = mPcrFormViewModel
        return mPcrFormFragmentBinding?.root
    }

}