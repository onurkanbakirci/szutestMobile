package com.onurkanbakirci.szutestosgb.data.repository

import com.onurkanbakirci.szutestosgb.data.db.entities.Company
import com.onurkanbakirci.szutestosgb.data.network.IAPI
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyRepository {

    fun getCompany(token : String): Observable<List<Company>> {
        return Observable.create{ emitter ->

            var mList = arrayListOf<Company>()

            IAPI().getCompanies(token).enqueue(object : Callback<List<Company>> {
                override fun onFailure(call: Call<List<Company>>, t: Throwable) {
                    emitter.onError(t)
                }
                override fun onResponse(
                    call: Call<List<Company>>,
                    response: Response<List<Company>>
                ) {
                    mList.addAll(response.body()!!)
                    emitter.onNext(mList)
                }
            })
        }
    }
}