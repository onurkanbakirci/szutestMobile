package com.onurkanbakirci.szutestosgb.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PCR")
class PCR (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var uID:Int ? =null,

    @ColumnInfo(name = "pcr_name")
    var title:String?=null,

    @ColumnInfo(name = "pcr_date")
    var date:String?=null,

    @ColumnInfo(name = "user_id")
    var user_id:Int?=null,

    @ColumnInfo(name = "username")
    var userName:String?=null

)