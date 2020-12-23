package com.onurkanbakirci.szutestosgb.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answer")
class Answer (

    @PrimaryKey
    @ColumnInfo(name="id")
    var uID:Int?=null,

    @ColumnInfo(name="category_name")
    var category_name:String ? = null,

    @ColumnInfo(name="question")
    var question:String?=null,

    @ColumnInfo(name="radio_state")
    var radio_state:String?=null,

    @ColumnInfo(name="comment_line")
    var comment_line : String?=null

){
}