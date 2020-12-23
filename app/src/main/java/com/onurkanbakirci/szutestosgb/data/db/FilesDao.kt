package com.onurkanbakirci.szutestosgb.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onurkanbakirci.szutestosgb.data.db.entities.Files
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface FilesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun InsertFile(file: Files) : Completable

    @Query("SELECT * FROM files WHERE user_id=:userID")
    fun GetAllFile(userID:Int) : Observable<List<Files>>

    @Query("DELETE FROM files WHERE file_name=:fileName")
    fun DeleteFileByName(fileName:String) : Completable

    @Query("DELETE FROM files")
    fun DeleteAllFiles() : Completable
}