package com.onurkanbakirci.szutestosgb.data.repository

import com.onurkanbakirci.szutestosgb.data.db.entities.Category
import com.onurkanbakirci.szutestosgb.data.network.IAPI
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryRepository {

    fun getCategories(token : String): Observable<List<Category>> {
        return Observable.create{emitter ->

            var mList = arrayListOf<Category>()

            IAPI().getCategories(token).enqueue(object : Callback<List<Category>> {
                override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                    emitter.onError(t)
                }

                override fun onResponse(
                    call: Call<List<Category>>,
                    response: Response<List<Category>>
                ) {
                    mList.addAll(response.body()!!)
                    emitter.onNext(mList)
                }
            })
        }
    }
}