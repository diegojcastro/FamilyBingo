package com.example.familybingo.screens.setup

import android.app.Application
import android.util.Log
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.familybingo.BR
import com.example.familybingo.database.BingoDatabaseDao
import com.example.familybingo.database.BingoField
import kotlinx.coroutines.launch

class BoardSetupViewModel(
    val database: BingoDatabaseDao,
    application: Application,
    val boardTitle: String
) : AndroidViewModel(application)  {

    // This observer stuff was necessary for two-way binding of sorts
    // Where the view model value for a text field updates as I change
    // the edit text in the UI fragment.
    var mObserver = Observer()
    fun getObserver(): Observer? {
        return mObserver
    }
    fun tryToGetObserver(): Observer {
        return mObserver
    }
    class Observer : BaseObservable() {
        private var fieldText = ""
        @Bindable
        fun getFieldText(): String {
            return fieldText
        }
        fun setFieldText(myNewText : String) {
            if(fieldText != myNewText) {
                fieldText = myNewText
                notifyPropertyChanged(BR.fieldText)
            }
        }
    }

    private var bingoEntries = MutableLiveData<List<BingoField>>()

    private val allEntries = database.getAllFields()

    private var oneEntry = MutableLiveData<BingoField?>()

    init {
        initializeOneEntry()
    }

    private fun initializeOneEntry() {
        viewModelScope.launch {
            oneEntry.value = getLatest()
        }
    }

    private suspend fun getLatest(): BingoField? {
        val latest = database.getLastEntry()
        return latest

    }

    fun onSecondButton() {
        viewModelScope.launch {
            val newField = BingoField()
            insert(newField)
            oneEntry.value = getLatest()
            Log.i("BoardSetupViewModel", "Inserted new field, index: ${oneEntry.value?.fieldID}.")

        }
    }


    private suspend fun insert(field: BingoField) {
        database.insert(field)
    }

    fun onThirdButton() {
        viewModelScope.launch {
            val oldField = oneEntry.value ?: return@launch
            oldField.text = "Complete."
            Log.i("BoardSetupViewModel", "Last entry ID is ${oldField.fieldID}.")
            Log.i("BoardSetupViewModel", "Last entry text is ${oldField.text}.")

            update(oldField)
        }
    }

    private suspend fun update(field: BingoField) {
        database.update(field)
    }


    fun onClearAll() {
        viewModelScope.launch {
            clear()
            oneEntry.value = null
        }
    }

    suspend fun clear() {
        database.clearEverything()
    }

    val allFieldsID = ""

    fun appendAllIDs() {

    }

    // TODO My current problem: This keeps printing size = null
    // I need to figure out why getAllFields() is not giving a proper list to allEntries2
    fun checkDatabaseSize() {
        val allEntries2 = database.getAllFields()
        val size = allEntries2.value
        Log.i("BoardSetupViewModel", "allEntries is of size = $size")

    }

    data class BoardEntry(
        var text: String,
        val location: Int,
        val correct: Boolean,
        val missed: Boolean)
    //The _board stuff is backing properties from 5.02 on LifeData in Kotlin tutorial
    // Set up MutableLiveData containers that will update on init block
    // The current _word
    private val _boardEntries = MutableLiveData<MutableList<BoardEntry>>()
    val boardEntries: LiveData<MutableList<BoardEntry>>
        get() = _boardEntries
    private val _editFieldVisible = MutableLiveData<Int>()
    val editFieldVisible: LiveData<Int>
        get() = _editFieldVisible
    private val _currentEditField = MutableLiveData<Int>()
    val currentEditField: LiveData<Int>
        get() = _currentEditField



    // Make a board with the default entries
    private val bingoBoard: MutableList<BoardEntry> = mutableListOf(
        BoardEntry(text = "This one prints everything",
            location = 11, correct = false, missed = false),
        BoardEntry(text = "Make an entry",
            location = 12, correct = false, missed = false),
        BoardEntry(text = "Get the last entry and print?",
            location = 13, correct = false, missed = false),
        BoardEntry(text = "Zeke is late",
            location = 14, correct = false, missed = false),
        BoardEntry(text = "CLEAR EVERYTHING. Nuke it.",
            location = 15, correct = false, missed = false),
        BoardEntry(text = "Sleep track START",
            location = 21, correct = false, missed = false),
        BoardEntry(text = "Sleep track END",
            location = 22, correct = false, missed = false),
        BoardEntry(text = "Nothing of interest",
            location = 23, correct = false, missed = false),
        BoardEntry(text = "Matt is misunderstood",
            location = 24, correct = false, missed = false),
        BoardEntry(text = "Colleen says hiiiii",
            location = 25, correct = false, missed = false),
        BoardEntry(text = "Check DB size",
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
        //this bit is from trying to swap to the Database BingoField rather than BoardEntry
        //initializeBingoFields()
        //createBoardOnLoad()



        Log.i("BoardSetupViewModel", "Board Setup ViewModel created!")
        _boardEntries.value = bingoBoard
        _editFieldVisible.value = View.GONE
    }

    private fun initializeBingoFields() {
        viewModelScope.launch {
            bingoEntries.value = getEntriesFromParent()
        }
    }

    // Possibly change the return type to return LiveData<List<BingoField>> instead
    // Originally had private suspend fun, I got rid of suspend
    private fun getEntriesFromParent(): List<BingoField>? {
        val entries = database.getFromParent(boardTitle)
        val entriesList = entries.value
        return entriesList
    }

    private fun createBoardOnLoad() {
        viewModelScope.launch {
            for (i in 0..24) {
                val newBingoField = BingoField()
                newBingoField.parentBoardName = boardTitle
                newBingoField.location = i.toByte()
                newBingoField.text = "Index: $i -- replace me!"
                insert(newBingoField)
            }

            bingoEntries.value = getEntriesFromParent()
            // This is where tonight.value = getTonightFromDatabase() on the tutorial
            // For me it would be something like bingoEntries = ...
        }
    }




    override fun onCleared() {
        super.onCleared()
        Log.i("BoardSetupViewModel", "BoardSetupViewModel destroyed!")
    }

    fun showEditField(index: Int) {
        _currentEditField.value = index
        _editFieldVisible.value = View.VISIBLE
        //_editFieldText.value = bingoBoard[index].text
        mObserver.setFieldText(bingoBoard[index].text)
        Log.i("BoardSetupViewModel", "ViewModel should have made edit popup visible")
    }


    fun editTextEntry(index : Int, newText : String) {
        if (index in 0..24) {
            Log.i("BoardSetupViewModel", "mObserver field text is "+mObserver.getFieldText())
            Log.i("BoardSetupViewModel", "Original text on field "+index.toString()+" is "+bingoBoard[index].text+" and I'm trying to set it to "+newText)
            bingoBoard[index].text = newText
            Log.i("BoardSetupViewModel", "Changed text in index $index to $newText")
            _boardEntries.value = bingoBoard
            // Added this line to see if LiveData updates now
        }
        _editFieldVisible.value = View.GONE
    }

    fun debugShowDatabaseContents() {
        Log.i("BoardSetupViewModel", "Attempting database debug.")
        val x = bingoEntries.value?.size
        Log.i("BoardSetupViewModel", "bingoEntries is of size $x")
        for (x in 0..24) {
            Log.i("BoardSetupViewModel", "Database index $x has value "+bingoEntries.value?.get(x)?.text)
        }
    }

    fun debugMakeEntry() {
        viewModelScope.launch {
            Log.i("BoardSetupViewModel", "Attempting to make an entry in database.")
            val myNewBingoField = BingoField()
            myNewBingoField.parentBoardName = "diegoTest"
            myNewBingoField.location = 11
            myNewBingoField.text = "Test text here"
            insert(myNewBingoField)
            oneEntry.value = getLatest()
            Log.i("BoardSetupViewModel", "Inserted entry index ${myNewBingoField.location} with text ${myNewBingoField.text}")
            Log.i("BoardSetupViewModel", "Now I'll try to read it.")
        }
    }


    fun debugReadEntry() {
        val lastEntry = BingoField()
        Log.i("BoardSetupViewModel", "Last entry text is ${lastEntry?.text}.")
        Log.i("BoardSetupViewModel", "Last entry ID is ${lastEntry?.fieldID}.")
        Log.i("BoardSetupViewModel", "Last entry location is ${lastEntry?.location}.")
    }

    // TODO make keyboard disappear after editTextEntry
    // TODO Coroutines step in Coroutines and Room 6.2 android tutorial
    // TODO from above, I'm on Step 2 after modifying gradle
    // TODO add game (where you check the boxes) viewmodel + viewmodelfactory + fragment



}