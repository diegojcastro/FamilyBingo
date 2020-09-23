package com.example.familybingo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BingoField::class], version = 1, exportSchema = false)
abstract class BingoDatabase : RoomDatabase() {

    abstract val bingoDatabaseDao: BingoDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: BingoDatabase? = null

        fun getInstance(context: Context): BingoDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BingoDatabase::class.java,
                        "bingo_boards_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }

}