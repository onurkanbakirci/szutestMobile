package com.onurkanbakirci.szutestosgb.data.db

import androidx.room.*
import com.onurkanbakirci.szutestosgb.data.db.entities.Answer
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


@Dao
interface AnswerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun InsertAnswer(answer: Answer) : Completable

    @Query("SELECT * FROM answer")
    fun GetAllAnswer() : Observable<List<Answer>>

    @Query("SELECT * FROM answer WHERE question=:question")
    fun GetAnswerByQuestion(question : String) : Single<Answer>

    @Query("DELETE FROM answer")
    fun DeleteAllAnswer() : Completable

}