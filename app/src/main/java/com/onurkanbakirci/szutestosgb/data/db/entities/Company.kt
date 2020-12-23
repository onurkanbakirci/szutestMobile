package com.onurkanbakirci.szutestosgb.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companies")
class Company (

       @PrimaryKey(autoGenerate = true)
       @ColumnInfo(name="id")
       var uID:Int?=null,

       @ColumnInfo(name = "name")
       var name : String ? =null
)
{
}