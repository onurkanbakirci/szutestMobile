package com.onurkanbakirci.szutestosgb.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.databinding.PcrFragmentBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.onurkanbakirci.szutestosgb.ui.recyleviewadapters.PCRAdapter
import kotlinx.android.synthetic.main.pcr_fragment.*
import kotlinx.android.synthetic.main.recent_fragment.*

class PCRFragment :Fragment() {

    private var mPcrViewModel : PCRViewModel? =null

    private var mPcrFragmentBinding : PcrFragmentBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPcrViewModel =
            ViewModelProviders.of(requireActivity(),PCRViewModelFactory(requireContext())).get(PCRViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mPcrFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.pcr_fragment,container,false)
        mPcrFragmentBinding?.pcrViewModel = mPcrViewModel
        mPcrViewModel?.getPCR()
        return mPcrFragmentBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPcrViewModel?.mPCR?.observe(viewLifecycleOwner, Observer { pcr ->
            pcr_recycle_view.also {
                it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                it.setHasFixedSize(true)
                it.adapter=
                    PCRAdapter(pcr,requireContext())
                if(pcr.size.equals(0)){
                    mPcrViewModel?.pcrNotFoundVisibility?.set(0)
                }else{
                    mPcrViewModel?.pcrNotFoundVisibility?.set(8)
                }
            }
        })
    }
}


