package com.onurkanbakirci.szutestosgb.data.repository

import com.onurkanbakirci.szutestosgb.data.db.entities.Question
import com.onurkanbakirci.szutestosgb.data.network.IAPI
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionRepository {

    fun getQuestionsByIds(token:String, ids: String): Observable<List<Question>> {
        return Observable.create{emitter ->

            var mList = arrayListOf<Question>()

            IAPI().getQuestions(token,ids).enqueue(object : Callback<List<Question>> {
                override fun onFailure(call: Call<List<Question>>, t: Throwable) {
                    emitter.onError(t)
                }
                override fun onResponse(
                    call: Call<List<Question>>,
                    response: Response<List<Question>>
                ) {
                    mList.addAll(response.body()!!)
                    emitter.onNext(mList)
                }
            })
        }
    }
}