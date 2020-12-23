package com.onurkanbakirci.szutestosgb.data.db.entities

import com.google.gson.annotations.SerializedName


class User(

    @SerializedName("id")
    var uID : Int ? =null,

    var status : Int ? =null,

    var name : String ? =null,

    var email : String ? =null,

    var password:String?=null,

    var token : String ? =null
)