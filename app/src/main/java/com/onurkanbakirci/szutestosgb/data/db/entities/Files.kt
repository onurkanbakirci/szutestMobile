package com.onurkanbakirci.szutestosgb.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "files")
class Files(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var uID:Int ? =null,

    @ColumnInfo(name = "file_name")
    var title:String?=null,

    @ColumnInfo(name = "file_date")
    var date:String?=null,

    @ColumnInfo(name = "user_id")
    var userID:Int?=null,

    @ColumnInfo(name = "registerer_email")
    var email:String ? =null,

    @ColumnInfo(name = "succcess")
    var success:Float ? =  null

)