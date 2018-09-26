package com.yuricfurusho.newwordsample

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by Yuri Furusho on 25/09/18.
 */
@Dao
interface WordDao {

    @Query("SELECT * from word_table ORDER BY word ASC")
    fun getAllWords(): List<Word>

    @Insert
    fun insert(word: Word)

    @Query("DELETE FROM word_table")
    fun deleteAll()
}