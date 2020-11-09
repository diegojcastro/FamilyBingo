package com.appsbydiego.familybingo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appsbydiego.familybingo.R

@Entity(tableName = "all_bingo_fields_table")
data class BingoField (
    @ColumnInfo(name = "field_text")
    var text: String = "Your entry text goes here.",

    @ColumnInfo(name = "location_index")
    var location: Byte = 0,     // convert to Int if Byte is problematic

    @ColumnInfo(name = "marking")
    var marking: Int = R.drawable.bingoboard_fieldbackground_bordered,     // was byte, switched to int

    @ColumnInfo(name = "parent_board")
    var parentBoardName: String = "placeholderParent"
)
{
    @PrimaryKey(autoGenerate = true)
    var fieldID: Long = 0L

}