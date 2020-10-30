package com.example.familybingo.screens.game

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.familybingo.database.BingoDatabaseDao
import com.example.familybingo.database.BingoField
import com.example.familybingo.database.BoardHolder
import kotlinx.coroutines.launch

class GameViewModel(
    val database: BingoDatabaseDao,
    application: Application,
    val boardTitle: String) : AndroidViewModel(application)  {

    private val titleHolder = MutableLiveData<BoardHolder?>()
    private val thisBoardEntries = MutableLiveData<List<BingoField>?>()

    private val _selectedFieldText = MutableLiveData<String>()
    val selectedFieldText: LiveData<String>
        get() = _selectedFieldText

    private val _selectedFieldIndex = MutableLiveData<Int>()
    val selectedFieldIndex: LiveData<Int>
        get() = _selectedFieldIndex

    private val _markFieldDialog = MutableLiveData<Boolean>()
    val markFieldDialog: LiveData<Boolean>
        get() = _markFieldDialog

//    init {
//        initializeTitleAndData()
//    }

    private fun initializeTitleAndData() {
        _selectedFieldIndex.value = 0
        _selectedFieldText.value = ""
        _markFieldDialog.value = false
        Log.i("GameViewModel", "init1 complete. SelectedFieldIndex: ${selectedFieldIndex.value}. SelectedFieldText: ${selectedFieldText.value}. MarkFieldDialog: ${markFieldDialog.value}")

        // Commented out because it's explicitly written in the init block below
//        viewModelScope.launch {
//            Log.i("GameViewModel", "Init part 2: viewModelScope.launch started.")
//            titleHolder.value = getBoardHolder()
//            Log.i("GameViewModel", "Initialized titleHolder with value ${titleHolder.value}")
//            thisBoardEntries.value = getEntries()
//            Log.i("GameViewModel", "Initialized thisBoardEntries with value ${thisBoardEntries.value}")
//
//        }
    }

    // DATABASE FUNCTIONS
    // DATABASE 1
    private suspend fun getBoardHolder() : BoardHolder? {
        Log.i("GameViewModel", "Running database.selectHolder(boardTitle), function getBoardHolder().")
        val title = database.selectHolder(boardTitle)
        return title
    }
    // DATABASE 2
    private suspend fun getEntries() : List<BingoField>? {
        Log.i("GameViewModel", "Running database.getFromParent(boardTitle), function getEntries().")
        val entries = database.getFromParent(boardTitle)
        return entries
    }

    // GENERIC DATABASE FUNCTIONS
    private suspend fun update(title: BoardHolder) {
        database.update(title)
    }


    // Make a board with the default entries
    private val _bingoBoard = MutableLiveData<List<BingoField>>()
    val bingoBoard: LiveData<List<BingoField>>
        get() = _bingoBoard

//    private var board : MutableList<BingoField> = mutableListOf()

    // Maybe this one should go way up instead? Where the previous init block was commented out.
    init {
        initializeTitleAndData()
        viewModelScope.launch {
            Log.i("GameViewModel", "Start viewModelScope.launch on init block.")
            titleHolder.value = getBoardHolder()
            // If the line above freezes it, the problem is most likely the observer on GameFragment
            val title = titleHolder.value?.title
            Log.i("GameViewModel", "Read titleHolder correctly, title text: $title")

            thisBoardEntries.value = getEntries()
            val tracingError = thisBoardEntries.value
            Log.i("GameViewModel", "On second init block, I have thisBoardEntries.value set to: $tracingError")

            if (thisBoardEntries.value?.isEmpty()!!) {
                Log.i(
                    "GameViewModel",
                    "allEntries.value is EMPTY, seemingly: ${thisBoardEntries.value}, should be empty"
                )
            } else {
                Log.i(
                    "GameViewModel",
                    "allEntries.value is NOT empty, seemingly: ${thisBoardEntries.value}, should be size 25"
                )
                _bingoBoard.value = thisBoardEntries.value
                //           board = _bingoBoard.value as MutableList<BingoField>

                val holder = getBoardHolder()
                Log.i("GameViewModel", "Grabbed holder on init2, its value was originally: $holder")

                if (holder != null) {
                    holder.lastOpened = System.currentTimeMillis()
                    holder.playStatus = "Playing Game"
                    update(holder)
                    Log.i("GameViewModel", "Updated holder, its value is now: $holder")
                }
            }
        }
    }

    fun markField(loc: Byte, marking: Byte) {

    }

    fun openGameDialog(index: Int) {
        _selectedFieldIndex.value = index
        _markFieldDialog.value = true
    }

    fun closeGameDialog() {
        _markFieldDialog.value = false
    }

    fun markFieldMissed(index: Int) {
        _bingoBoard.value!![index].marking = -1
    }

    fun markFieldChecked(index: Int) {
        _bingoBoard.value!![index].marking = 1
    }


}