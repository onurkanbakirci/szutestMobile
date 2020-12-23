package com.onurkanbakirci.szutestosgb.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onurkanbakirci.szutestosgb.data.db.entities.PCR
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface PCRDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun InsertPCR(pcr: PCR) : Completable

    @Query("SELECT * FROM pcr WHERE user_id =:userID")
    fun GetAllPCR(userID : Int) : Observable<List<PCR>>

    @Query("DELETE FROM pcr WHERE pcr_date =:date")
    fun DeletePCRByDate(date:String) : Completable

    @Query("DELETE FROM pcr")
    fun DeleteAllPCR() : Completable
}