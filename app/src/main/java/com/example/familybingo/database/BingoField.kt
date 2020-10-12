package com.example.familybingo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_bingo_fields_table")
data class BingoField (
    @ColumnInfo(name = "field_text")
    var text: String = "Your entry text goes here.",

    @ColumnInfo(name = "location_index")
    var location: Byte = 0,     // convert to Int if Byte is problematic

    @ColumnInfo(name = "marking")
    var marking: Byte = 0,     // Switched to this from two booleans for correct/missed

    @ColumnInfo(name = "parent_board")
    var parentBoardName: String = "placeholderParent"
)
{
    @PrimaryKey(autoGenerate = true)
    var fieldID: Long = 0L
}