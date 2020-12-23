package com.onurkanbakirci.szutestosgb.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "categories")
class Category (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    @SerializedName("id")
    var uID:Int?=null,

    @ColumnInfo(name = "name")
    var name : String ? =null,

    var isChecked :Boolean = false

)
{
}