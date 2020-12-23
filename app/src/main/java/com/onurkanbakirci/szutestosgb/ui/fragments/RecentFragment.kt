package com.onurkanbakirci.szutestosgb.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.databinding.RecentFragmentBinding
import com.onurkanbakirci.szutestosgb.ui.recyleviewadapters.FileAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.recent_fragment.*

class RecentFragment: Fragment() {

    private var mRecentViewModel : RecentFragmentViewModel? =null

    private var mRecentFragmentBinding : RecentFragmentBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRecentViewModel =
            ViewModelProviders.of(requireActivity(),RecentViewModelFactory(requireContext())).get(RecentFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRecentFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.recent_fragment,container,false)
        mRecentFragmentBinding?.recentFragmentViewModel = mRecentViewModel
        return mRecentFragmentBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var divider = DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL)
        divider.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.layer)!!)
        //mRecentViewModel?.mFile?.removeObservers(this)
        mRecentViewModel?.mFile?.observe(viewLifecycleOwner, Observer { files ->
            files_recycle_view.also {
                it.layoutManager = GridLayoutManager(requireContext(),2)
                it.addItemDecoration(divider)
                it.setHasFixedSize(true)
                it.adapter=
                    FileAdapter(files,requireContext())
                if(files.size.equals(0)){
                    mRecentViewModel?.notFoundVisibility?.set(0)
                }else{
                    mRecentViewModel?.notFoundVisibility?.set(8)
                }
            }
        })
    }
}