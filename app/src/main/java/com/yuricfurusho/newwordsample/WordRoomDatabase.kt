package com.yuricfurusho.newwordsample

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.launch

/**
 * Created by Yuri Furusho on 25/09/18.
 */
@Database(entities = arrayOf(Word::class), version = 1)
public abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): WordRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordRoomDatabase::class.java,
                        "Word_database"
                )
                        .addCallback(WordDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class WordDatabaseCallback(
            private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.wordDao())
                }
            }
        }

        fun populateDatabase(wordDao: WordDao) {
            wordDao.deleteAll()

            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)
        }
    }
}