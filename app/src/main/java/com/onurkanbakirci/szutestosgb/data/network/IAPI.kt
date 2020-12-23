package com.onurkanbakirci.szutestosgb.data.network

import com.onurkanbakirci.szutestosgb.data.db.entities.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface IAPI {

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("email") email : String,@Field("password") password : String) : Call<User>

    @GET("categories")
    fun getCategories(@Header("authorization") auth : String) : Call<List<Category>>

    @FormUrlEncoded
    @POST("questions")
    fun getQuestions(@Header("authorization") auth : String,@Field("ids") ids :String ) : Call<List<Question>>

    @GET("companies")
    fun getCompanies(@Header("authorization") auth : String) : Call<List<Company>>

    @Multipart
    @POST("file")
    fun saveFile(@Header("authorization") auth : String,@Part file: MultipartBody.Part,@Part ("success") success : Float) : Call<Upload>

    @FormUrlEncoded
    @POST("pcr")
    fun savePCR(@Header("authorization") auth : String,@Field("userID") id :Int,
                                                            @Field("name") name :String ,
                                                            @Field("surname") surname :String ,
                                                            @Field("firm_name") firmName:String,
                                                            @Field("identity_number") identityNumber :String,
                                                            @Field("born_date") bornDate :String ,
                                                            @Field("address") address :String,
                                                            @Field("phone_number") phoneNumber :String,
                                                            @Field("anticor") anticor :String,
                                                            @Field("foreign") foreign :String,
                                                            @Field("nationality") nationality :String,
                                                            @Field("flight_inf") fligthInf :String
                                                ) : Call<Upload>

    companion object{
        operator fun invoke():IAPI{
            return Retrofit.Builder()
                //.baseUrl("http://192.168.1.114:8000/api/")
                 .baseUrl("http://yusufozyasar.site/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IAPI::class.java)
        }
    }
}