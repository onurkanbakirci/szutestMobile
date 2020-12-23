package com.onurkanbakirci.szutestosgb.ui.recyleviewadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.data.db.entities.Question
import com.onurkanbakirci.szutestosgb.databinding.ItemOfQuestionBinding
import com.onurkanbakirci.szutestosgb.ui.fragments.QuestionsViewModel


class QuestionsAdapter (private val mQuestions : List<Question>, val mContext: Context,val ids:ArrayList<Int>,val compName:String): RecyclerView.Adapter<QuestionsAdapter.QquestionsViewHolder>() {

    inner class QquestionsViewHolder( val  itemOfQuestionBinding: ItemOfQuestionBinding):
        RecyclerView.ViewHolder(itemOfQuestionBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : QquestionsViewHolder{

        val mQuestionsFragmentViewModel = QuestionsViewModel(mContext,ids,compName)

        val categoriesViewHolder = QquestionsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_of_question,
                parent,
                false
            )
        )
        categoriesViewHolder.itemOfQuestionBinding.questionsViewModel = mQuestionsFragmentViewModel
        return categoriesViewHolder
    }

    override fun getItemCount(): Int {
        return mQuestions.size
    }

    override fun onBindViewHolder(holder: QquestionsViewHolder, position: Int) {
        holder.itemOfQuestionBinding.question = mQuestions[position]
        holder.itemOfQuestionBinding.pos = position
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}