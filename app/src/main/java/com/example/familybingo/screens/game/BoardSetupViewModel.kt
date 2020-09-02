package com.example.familybingo.screens.game

import android.util.Log
import androidx.lifecycle.ViewModel

class BoardSetupViewModel : ViewModel() {

    data class BoardEntry(
        var text: String,
        val location: Int,
        val correct: Boolean,
        val missed: Boolean)

    // Make a board with the default entries
    private val bingoBoard: MutableList<BoardEntry> = mutableListOf(
        BoardEntry(text = "Gene smiles",
            location = 11, correct = false, missed = false),
        BoardEntry(text = "Darlene says 'whatever'",
            location = 12, correct = false, missed = false),
        BoardEntry(text = "Grandma talks about Dr. Oz",
            location = 13, correct = false, missed = false),
        BoardEntry(text = "Zeke is late",
            location = 14, correct = false, missed = false),
        BoardEntry(text = "Somebody does something",
            location = 15, correct = false, missed = false),
        BoardEntry(text = "DaVinci is a dog",
            location = 21, correct = false, missed = false),
        BoardEntry(text = "Phone rings",
            location = 22, correct = false, missed = false),
        BoardEntry(text = "Greg says something entitled",
            location = 23, correct = false, missed = false),
        BoardEntry(text = "Mat is misunderstood",
            location = 24, correct = false, missed = false),
        BoardEntry(text = "Colleen says hiiiii",
            location = 25, correct = false, missed = false),
        BoardEntry(text = "Entry 31",
            location = 31, correct = false, missed = false),
        BoardEntry(text = "Entry 32 is going to be longer so I can test with a longer string, yep",
            location = 32, correct = false, missed = false),
        BoardEntry(text = "Hello",
            location = 33, correct = false, missed = false),
        BoardEntry(text = "One",
            location = 34, correct = false, missed = false),
        BoardEntry(text = "Two",
            location = 35, correct = false, missed = false),
        BoardEntry(text = "Repeat entries",
            location = 41, correct = false, missed = false),
        BoardEntry(text = "One",
            location = 42, correct = false, missed = false),
        BoardEntry(text = "Two",
            location = 43, correct = false, missed = false),
        BoardEntry(text = "One",
            location = 44, correct = false, missed = false),
        BoardEntry(text = "Two",
            location = 45, correct = false, missed = false),
        BoardEntry(text = "Three",
            location = 51, correct = false, missed = false),
        BoardEntry(text = "Hello",
            location = 52, correct = false, missed = false),
        BoardEntry(text = "Hello",
            location = 53, correct = false, missed = false),
        BoardEntry(text = "Hello",
            location = 54, correct = false, missed = false),
        BoardEntry(text = "Hello",
            location = 55, correct = false, missed = false)
    )


    //Delete this later
    init {
        Log.i("BoardSetupViewModel", "Board Setup ViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("BoardSetupViewModel", "BoardSetupViewModel destroyed!")
    }

    fun editTextEntry(index : Int, newText : String) {
        if (index in 0..24) {
            bingoBoard[index].text = newText
            Log.i("BoardSetupViewModel", "Changed text in index $index")
        }
    }


}