package com.onurkanbakirci.szutestosgb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.onurkanbakirci.szutestosgb.data.db.entities.Answer
import com.onurkanbakirci.szutestosgb.data.db.entities.Files
import com.onurkanbakirci.szutestosgb.data.db.entities.PCR


@Database(entities = [Answer::class,Files::class,PCR::class], version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun answerDao(): AnswerDao
    abstract fun fileDao(): FilesDao
    abstract fun pcrDao(): PCRDao

    companion object {

        @Volatile
        var INSTANCE: AppDatabase? = null
        const val DB_NAME="szutestosgb.db"

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}