package com.appsbydiego.familybingo.screens.game

import android.app.Application
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.appsbydiego.familybingo.R
import com.appsbydiego.familybingo.database.BingoDatabaseDao
import com.appsbydiego.familybingo.database.BingoField
import com.appsbydiego.familybingo.database.BoardHolder
import kotlinx.coroutines.launch

class GameViewModel(
    val database: BingoDatabaseDao,
    application: Application,
    val boardTitle: String) : AndroidViewModel(application)  {

    val BG_UNMARKED = R.drawable.bingoboard_fieldbackground_bordered
    val BG_CHECKED = R.drawable.bingoboard_fieldbackground_checked
    val BG_MISSED = R.drawable.bingoboard_fieldbackground_missed

    private val titleHolder = MutableLiveData<BoardHolder?>()
    private val thisBoardEntries = MutableLiveData<List<BingoField>?>()

    private val _selectedFieldText = MutableLiveData<String>()
    val selectedFieldText: LiveData<String>
        get() = _selectedFieldText

    private val _selectedFieldIndex = MutableLiveData<Int>()
    val selectedFieldIndex: LiveData<Int>
        get() = _selectedFieldIndex

    private val _selectedView = MutableLiveData<TextView>()
    val selectedView: LiveData<TextView>
        get() = _selectedView

    private val _markFieldDialog = MutableLiveData<Boolean>()
    val markFieldDialog: LiveData<Boolean>
        get() = _markFieldDialog

    private val _gameScore = MutableLiveData<Int>()
    val gameScore: LiveData<Int>
        get() = _gameScore

//    init {
//        initializeTitleAndData()
//    }

    private fun initializeTitleAndData() {
        _selectedFieldIndex.value = 0
        _selectedFieldText.value = ""
        _markFieldDialog.value = false
        _gameScore.value = 0
//        Log.i("GameViewModel", "init1 complete. SelectedFieldIndex: ${selectedFieldIndex.value}. SelectedFieldText: ${selectedFieldText.value}. MarkFieldDialog: ${markFieldDialog.value}")

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
//        Log.i("GameViewModel", "Running database.selectHolder(boardTitle), function getBoardHolder().")
        val title = database.selectHolder(boardTitle)
        return title
    }
    // DATABASE 2
    private suspend fun getEntries() : List<BingoField>? {
//        Log.i("GameViewModel", "Running database.getFromParent(boardTitle), function getEntries().")
        val entries = database.getFromParent(boardTitle)
        return entries
    }
    // DATABASE 3
    private suspend fun getEntryAtIndex(parent: String, index: Byte): BingoField? {
//        Log.i("GameViewModel", "Running database.getEntryAtIndex($parent, $index)")
        val thisEntry = database.getEntryAtIndex(parent, index)
        return thisEntry
    }

    // GENERIC DATABASE FUNCTIONS
    private suspend fun update(title: BoardHolder) {
        database.update(title)
    }
    private suspend fun update(field: BingoField) {
        database.update(field)
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
//            Log.i("GameViewModel", "Start viewModelScope.launch on init block.")
            titleHolder.value = getBoardHolder()
            // If the line above freezes it, the problem is most likely the observer on GameFragment
            val title = titleHolder.value?.title
//            Log.i("GameViewModel", "Read titleHolder correctly, title text: $title")

            val score = titleHolder.value?.score
            _gameScore.value = score
//            Log.i("GameViewModel", "Read score from titleHolder correctly, score text: $title")


            thisBoardEntries.value = getEntries()
            val tracingError = thisBoardEntries.value
//            Log.i("GameViewModel", "On second init block, I have thisBoardEntries.value set to: $tracingError")

            if (thisBoardEntries.value?.isEmpty()!!) {
//                Log.i(
//                    "GameViewModel",
//                    "allEntries.value is EMPTY, seemingly: ${thisBoardEntries.value}, should be empty"
//                )
            } else {
//                Log.i(
//                    "GameViewModel",
//                    "allEntries.value is NOT empty, seemingly: ${thisBoardEntries.value}, should be size 25"
//                )
                _bingoBoard.value = thisBoardEntries.value
                //           board = _bingoBoard.value as MutableList<BingoField>
                var tempScore = 0
                for (i in _bingoBoard.value!!) {
                    if (i.marking == BG_CHECKED) tempScore += 1
                }

                val holder = getBoardHolder()
//                Log.i("GameViewModel", "Grabbed holder on init2, its value was originally: $holder")

                if (holder != null) {
                    holder.lastOpened = System.currentTimeMillis()
                    holder.playStatus = "Playing Game"
                    holder.score = tempScore
                    _gameScore.value = tempScore
                    update(holder)
//                    Log.i("GameViewModel", "Updated holder, its value is now: $holder")
                    titleHolder.value = holder
                }
            }
        }
    }

    fun markField(loc: Byte, marking: Byte) {

    }

    fun openGameDialog(index: Int, view: TextView) {
        _selectedFieldIndex.value = index
        _selectedFieldText.value = _bingoBoard.value?.get(index)?.text
        _selectedView.value = view
        _markFieldDialog.value = true
    }

    fun openGameDialog(index: Int) {
        _selectedFieldIndex.value = index
        _markFieldDialog.value = true
    }

    fun closeGameDialog() {
        _markFieldDialog.value = false
    }

    // Outdated, kept for reference, didn't modify field color/text.
//    fun markFieldMissed(index: Int) {
//        _bingoBoard.value!![index].marking = -1
//        _bingoBoard.value = _bingoBoard.value
//        viewModelScope.launch {
//            val markedField = getEntryAtIndex(boardTitle, convertIndexToLocation(index))
//            Log.i("GameViewModel", "I think markedField is $markedField")
//            if (markedField != null) {
//                markedField.marking = -1
//                update(markedField)
//                Log.i("GameViewModel", "Updated field with -1 marking on DB: $markedField")
//            }
//        }
//    }
//    fun markFieldChecked(index: Int) {
//        _bingoBoard.value!![index].marking = 1
//        _bingoBoard.value = _bingoBoard.value
//        viewModelScope.launch {
//            val markedField = getEntryAtIndex(boardTitle, convertIndexToLocation(index))
//            Log.i("GameViewModel", "I think markedField is $markedField")
//            if (markedField != null) {
//                markedField.marking = 1
//                update(markedField)
//                Log.i("GameViewModel", "Updated field with 1 marking on DB: $markedField")
//            }
//        }
//    }

    fun markFieldMissed(index: Int, view: TextView) {
        if (_bingoBoard.value!![index].marking == BG_CHECKED) {
            var tempScore = _gameScore.value
            tempScore = tempScore?.minus(1)
            _gameScore.value = tempScore
        }
        _bingoBoard.value!![index].marking = BG_MISSED
        // Refresh LiveData on GameFragment observer
        _bingoBoard.value = _bingoBoard.value
        //observer on above data does the stuff below, previously hardcoded
        //view.setBackgroundResource(BG_MISSED)
//        Log.i("GameViewModel", "Set background to $BG_MISSED")
//        val color = getColor(getApplication(), R.color.white_text_color)
//        view.setTextColor(color)

        viewModelScope.launch {
            val markedField = getEntryAtIndex(boardTitle, convertIndexToLocation(index))
//            Log.i("GameViewModel", "I think markedField is $markedField")
            if (markedField != null) {
                markedField.marking = BG_MISSED
                update(markedField)
//                Log.i("GameViewModel", "Updated field with $BG_MISSED marking on DB: $markedField")
            }
            titleHolder.value!!.score = _gameScore.value!!
            update(titleHolder.value!!)
//            Log.i("GameViewModel", "Updated holder with score value of ${_gameScore.value}, holder: ${titleHolder.value}")
        }
    }

    fun markFieldChecked(index: Int, view: TextView) {
        if (_bingoBoard.value!![index].marking != BG_CHECKED) {
            var tempScore = _gameScore.value
            tempScore = tempScore?.plus(1)
            _gameScore.value = tempScore
        }
        _bingoBoard.value!![index].marking = BG_CHECKED
        // Trying to refresh LiveData on GameFragment since it has an observer
        _bingoBoard.value = _bingoBoard.value
        //observer on above data does the stuff below, previously hardcoded
        //view.setBackgroundResource(BG_CHECKED)
//        Log.i("GameViewModel", "Set background to $BG_CHECKED")
//        val color = getColor(getApplication(), R.color.white_text_color)
//        view.setTextColor(color)

        viewModelScope.launch {
            val markedField = getEntryAtIndex(boardTitle, convertIndexToLocation(index))
//            Log.i("GameViewModel", "I think markedField is $markedField")
            if (markedField != null) {
                markedField.marking = BG_CHECKED
                update(markedField)
//                Log.i("GameViewModel", "Updated field with $BG_CHECKED marking on DB: $markedField")
            }
            titleHolder.value!!.score = _gameScore.value!!
            update(titleHolder.value!!)
//            Log.i("GameViewModel", "Updated holder with score value of ${_gameScore.value}, holder: ${titleHolder.value}")

        }
    }

    fun markFieldDefault(index: Int, view: TextView) {
        if (_bingoBoard.value!![index].marking == BG_CHECKED) {
            var tempScore = _gameScore.value
            tempScore = tempScore?.minus(1)
            _gameScore.value = tempScore
        }
        _bingoBoard.value!![index].marking = BG_UNMARKED
        // Refresh LiveData on GameFragment observer
        _bingoBoard.value = _bingoBoard.value
//        Log.i("GameViewModel", "Set background to $BG_UNMARKED")
        viewModelScope.launch {
            val markedField = getEntryAtIndex(boardTitle, convertIndexToLocation(index))
//            Log.i("GameViewModel", "I think markedField is $markedField")
            if (markedField != null) {
                markedField.marking = BG_UNMARKED
                update(markedField)
//                Log.i("GameViewModel", "Updated field with $BG_UNMARKED marking on DB: $markedField")
            }
            titleHolder.value!!.score = _gameScore.value!!
            update(titleHolder.value!!)
//            Log.i("GameViewModel", "Updated holder with score value of ${_gameScore.value}, holder: ${titleHolder.value}")

        }

    }

    fun debugPrintEntries() {
//        Log.i("GameViewModel", "DebugPrintEntries function: ${_bingoBoard.value}")
    }

    private fun convertIndexToLocation(index: Int): Byte {
        val locX = index / 5 + 1
        val locY = index % 5 + 1
        val loc = (locX*10 + locY).toByte()
        return loc
    }


}