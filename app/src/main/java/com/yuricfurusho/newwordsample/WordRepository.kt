package com.yuricfurusho.newwordsample

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread

/**
 * Created by Yuri Furusho on 27/09/18.
 */
class WordRepository(private val wordDao: WordDao) {

    val allWords: LiveData<List<Word>> = wordDao.getAllWords()
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}