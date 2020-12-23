package com.onurkanbakirci.szutestosgb.data.db.entities

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "questions")
class Question (

    @SerializedName("question")
    var description:String?=null,

    @SerializedName("category_id")
    var category_id : String ? =null,

    @SerializedName("name")
    var category_name : String ? = null

)