package com.example.familybingo.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BingoDatabaseDao {
    @Insert
    suspend fun insert(field: BingoField)

    @Update
    suspend fun update(field: BingoField)

    @Query("SELECT * from all_bingo_fields_table WHERE fieldID = :key")
    suspend fun get(key: Long): BingoField?

    @Query("DELETE FROM all_bingo_fields_table WHERE parent_board = :key")
    suspend fun removeBoard(key: String)

    @Query("SELECT * from all_bingo_fields_table WHERE parent_board = :key ORDER BY location_index")
    fun getFromParent(key: String): LiveData<List<BingoField>>

    @Query("SELECT * FROM all_bingo_fields_table ORDER BY fieldID DESC LIMIT 1")
    suspend fun getLastEntry(): BingoField?

    @Query("SELECT * FROM all_bingo_fields_table ORDER BY fieldID DESC")
    fun getAllFields(): LiveData<List<BingoField>>

    @Query("DELETE FROM all_bingo_fields_table")
    suspend fun clearEverything()


}