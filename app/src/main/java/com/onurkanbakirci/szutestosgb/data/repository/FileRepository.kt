package com.onurkanbakirci.szutestosgb.data.repository

import com.onurkanbakirci.szutestosgb.data.db.entities.Upload
import com.onurkanbakirci.szutestosgb.data.network.IAPI
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FileRepository {

    fun uploadFile(token : String,file:MultipartBody.Part,success:Float): Upload {

        var uploadResponse = Upload()

        IAPI().saveFile(token,file,success).enqueue(object : Callback<Upload> {
            override fun onFailure(call: Call<Upload>, t: Throwable) {
            }
            override fun onResponse(
                call: Call<Upload>,
                response: Response<Upload>
            ) {
                if(response.isSuccessful){
                    uploadResponse = response.body()!!
                }else{
                    uploadResponse = response.body()!!
                }
            }
        })
        return uploadResponse
    }
}