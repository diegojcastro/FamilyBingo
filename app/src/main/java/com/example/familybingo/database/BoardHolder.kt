package com.example.familybingo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_parent_board_names_table")
data class BoardHolder (
    @ColumnInfo(name = "board_title")
    var title: String = "Placeholder Title",

    @ColumnInfo(name = "last_opened")
    var lastOpened: Long = 0L,

    @ColumnInfo(name = "play_status")
    var playStatus: String = "Setup",

    @ColumnInfo(name = "score")
    var score: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var boardID: Long = 0L
}