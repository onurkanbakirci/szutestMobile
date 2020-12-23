package com.onurkanbakirci.szutestosgb.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.databinding.CategoryFragmentBinding
import com.onurkanbakirci.szutestosgb.ui.recyleviewadapters.CategoryAdapter
import kotlinx.android.synthetic.main.category_fragment.*

class CategoryFragment : Fragment() {

    private var mCategoryViewModel : CategoryFragmentViewModel? =null

    private var mCategoryFragmentBinding : CategoryFragmentBinding? = null

    private var args :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        args = arguments!!.getString("nameOfCompany")

        activity?.title = "Kategori Seçiniz"
        mCategoryViewModel =
            ViewModelProviders.of(requireActivity(),CategoryFragmentViewModelFactory(requireContext(),args!!)).get(CategoryFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mCategoryFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.category_fragment,container,false)
        mCategoryFragmentBinding?.categoryFragmentViewModel = mCategoryViewModel
        mCategoryViewModel?.clearCache()
        mCategoryViewModel?.fetchCategories()
        return mCategoryFragmentBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //mCategoryViewModel?.mCategory?.removeObservers(this)
        mCategoryViewModel?.mCategory?.observe(viewLifecycleOwner, Observer { categories ->
            inputs_of_category_recycleview.also {
                it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                it.setHasFixedSize(true)
                it.adapter=
                    CategoryAdapter(categories,requireContext(),args!!)
            }
            mCategoryViewModel?.progressOfCategory?.set(8)
        })
    }
}