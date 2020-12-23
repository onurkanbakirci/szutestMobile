package com.onurkanbakirci.szutestosgb.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.databinding.QuestionsFragmentBinding
import com.onurkanbakirci.szutestosgb.ui.recyleviewadapters.QuestionsAdapter
import kotlinx.android.synthetic.main.questions_fragment.*

class QuestionsFragment : Fragment() {

    private var mQuestionViewModel : QuestionsViewModel? =null

    private var mQuestionFragmentBinding : QuestionsFragmentBinding? = null

    private var ids:ArrayList<Int> ? =null
    private var companyName:String ? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getActivity()?.setTitle("Sorular")

        ids = arguments!!.getIntegerArrayList("idsOfCat")
        companyName = arguments!!.getString("nameOfCompany")

        mQuestionViewModel =
            ViewModelProviders.of(requireActivity(),QuestionsViewModelFactory(requireContext(),ids!!,companyName!!)).get(QuestionsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mQuestionFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.questions_fragment,container,false)
        mQuestionFragmentBinding?.questionsViewModel = mQuestionViewModel
        mQuestionViewModel?.getQuestions()
        return mQuestionFragmentBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mQuestionViewModel?.mQuestions?.observe(viewLifecycleOwner, Observer { questions ->
                questions_recycle_view.also {
                it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                it.setHasFixedSize(true)
                it.adapter=
                    QuestionsAdapter(questions,requireContext(),ids!!,companyName!!)
            }
            mQuestionViewModel?.progressOfQuestion?.set(8)
        })
    }
}