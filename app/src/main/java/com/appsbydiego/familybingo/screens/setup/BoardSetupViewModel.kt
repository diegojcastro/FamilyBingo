package com.appsbydiego.familybingo.screens.setup

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.appsbydiego.familybingo.R
import com.appsbydiego.familybingo.database.BingoDatabaseDao
import com.appsbydiego.familybingo.database.BingoField
import com.appsbydiego.familybingo.database.BoardHolder
import kotlinx.coroutines.launch

class BoardSetupViewModel(
    val database: BingoDatabaseDao,
    application: Application,
    val boardTitle: String
) : AndroidViewModel(application)  {

    // This observer stuff was necessary for two-way binding of sorts
    // Where the view model value for a text field updates as I change
    // the edit text in the UI fragment.
//    var mObserver = Observer()
//    fun getObserver(): Observer? {
//        return mObserver
//    }
//    fun tryToGetObserver(): Observer {
//        return mObserver
//    }
//    class Observer : BaseObservable() {
//        private var fieldText = ""
//        @Bindable
//        fun getFieldText(): String {
//            return fieldText
//        }
//        fun setFieldText(myNewText : String) {
//            if(fieldText != myNewText) {
//                fieldText = myNewText
//                notifyPropertyChanged(BR.fieldText)
//            }
//        }
//    }

  //  private var bingoEntries = MutableLiveData<List<BingoField>>()

    private val tempEntry = MutableLiveData<BingoField?>()
    private val oneEntry = MutableLiveData<BingoField?>()
    private val allEntries = MutableLiveData<List<BingoField>?>()



    init {
        initializeOneEntry()
    }

    private fun initializeOneEntry() {
        viewModelScope.launch {
            tempEntry.value = getLatest()
            oneEntry.value = getLatest()
            allEntries.value = getEntriesFromParent()   //new part
        }
    }

    // These directly talk to the database, so must be private suspend fun
    // DATABASE 1
    private suspend fun getLatest(): BingoField? {
        val latest = database.getLastEntry()
        return latest
    }
    // DATABASE 2
    // new fun to mimic the one above
    private suspend fun getListOfEntries(): List<BingoField>? {
        val bingoList = database.getAllFieldsNotLive()
        return bingoList
    }
    // DATABASE 3
    // New attempt to read at index, following examples above
    private suspend fun getEntryAtIndex(parent: String, index: Byte): BingoField? {
        val thisEntry = database.getEntryAtIndex(parent, index)
        return thisEntry
    }
    // DATABASE 4
    // Hopefully will use this to initialize from database rather than hard-coded values.
    private suspend fun getEntriesFromParent(): List<BingoField>? {
        val entries = database.getFromParent(boardTitle)
        return entries
    }
    // DATABASE 5 HOLDER
    private suspend fun getHolderByTitle(title: String): BoardHolder? {
        val holder = database.selectHolder(title)
        return holder
    }
    // GENERIC DATABASE FUNCTIONS
    private suspend fun insert(field: BingoField) {
        database.insert(field)
    }
    private suspend fun update(field: BingoField) {
        database.update(field)
    }
    // GENERIC DATABASE FUNCTIONS for HOLDER
    private suspend fun insert(title: BoardHolder) {
        database.insert(title)
    }
    private suspend fun update(title: BoardHolder) {
        database.update(title)
    }
    suspend fun clear() {
        database.clearEverything()
        database.clearAllHolders()
        //Log.i("BoardSetupViewModel", "Nuked it. Everything deleted from DB Fields+Holders.")

    }

    fun onSecondButton() {
        viewModelScope.launch {
            val newField = BingoField()
            newField.parentBoardName = boardTitle
            insert(newField)
            oneEntry.value = getLatest()
            val startText = oneEntry.value?.text
            //Log.i("BoardSetupViewModel", "Inserted new field, index: ${oneEntry.value?.fieldID}.")
            //Log.i("BoardSetupViewModel", "Starting text on ${oneEntry.value?.fieldID} field is $startText.")

        }
    }
    // Database Insert initially went here


    fun onThirdButton() {
        viewModelScope.launch {
            val oldField = oneEntry.value ?: return@launch
//            oldField.location = 7
//            oldField.text = "Complete, location set to 7"
            //Log.i("BoardSetupViewModel", "Last entry ID is ${oldField.fieldID}.")
            //Log.i("BoardSetupViewModel", "Last entry text is '${oldField.text}'.")

            update(oldField)
        }
    }
    // Database update initially went here



    fun onClearAll() {
        viewModelScope.launch {
            clear()
            oneEntry.value = null
        }
    }
    // Database clearEverything initially went here




    fun checkDatabaseSize() {
        viewModelScope.launch {
            allEntries.value = getListOfEntries()
            val size = allEntries.value?.size
            val sizeNum = size.toString().toInt()
            val info = allEntries.value?.get(0)?.fieldID
            val info2 = allEntries.value?.get(sizeNum-1)?.fieldID
            val locInfo = allEntries.value?.get(0)?.location
//            Log.i("BoardSetupViewModel", "allEntries is of size = $size")
//            Log.i("BoardSetupViewModel", "The highest element in allEntries has fieldID = $info and location = $locInfo")
//            Log.i("BoardSetupViewModel", "The lowest element in allEntries has fieldID = $info2")
        }
    }

    fun checkThisBoardSizeOnDB() {
        viewModelScope.launch {
            allEntries.value = getEntriesFromParent()
            val size = allEntries.value?.size
            val sizeNum = size.toString().toInt()
            val info = allEntries.value?.get(0)?.fieldID
            val info2 = allEntries.value?.get(sizeNum-1)?.fieldID
            val locInfo = allEntries.value?.get(0)?.location
            val parentInfo = allEntries.value?.get(0)?.parentBoardName
//            Log.i("BoardSetupViewModel", "allEntries is of size = $size")
//            Log.i("BoardSetupViewModel", "The highest element in allEntries has fieldID = $info and location = $locInfo")
//            Log.i("BoardSetupViewModel", "The lowest element in allEntries has fieldID = $info2")
//            Log.i("BoardSetupViewModel", "The parent board is thought to be = $parentInfo")
        }
    }


    private val _editFieldVisible = MutableLiveData<Boolean>()
    val editFieldVisible: LiveData<Boolean>
        get() = _editFieldVisible

    private val _currentEditField = MutableLiveData<Int>()
    val currentEditField: LiveData<Int>
        get() = _currentEditField

    private val _navigateToGameFragment = MutableLiveData<Boolean>()
    val navigateToGameFragment: LiveData<Boolean>
        get() = _navigateToGameFragment


    // Make a board with the default entries - NEW with BingoField instead of BoardEntry
    private val _newBingoBoard = MutableLiveData<List<BingoField>>()
    val newBingoBoard: LiveData<List<BingoField>>
        get() = _newBingoBoard

    private var bingoBoard2: MutableList<BingoField> = mutableListOf(
        BingoField("Placeholder",11,0,"placeholderParent")
    )


    //Delete this later
    init {
        _navigateToGameFragment.value = false
        viewModelScope.launch {
            //this bit is from trying to swap to the Database BingoField rather than BoardEntry
            allEntries.value = getEntriesFromParent()

            if (allEntries.value?.isEmpty()!!) {
//                Log.i("BoardSetupViewModel",
//                    "allEntries.value is seemingly: ${allEntries.value}, should be empty"
//                )
                createBoardOnLoad()
            }
            else {
//                Log.i("BoardSetupViewModel",
//                    "allEntries.value is seemingly: ${allEntries.value}, should be size 25"
//                )
                _newBingoBoard.value = allEntries.value
                bingoBoard2 = _newBingoBoard.value as MutableList<BingoField>

                val holder = getHolderByTitle(boardTitle)
                if (holder != null) {
                    holder.lastOpened = System.currentTimeMillis()
                    update(holder)
                }


            }
        }

//        Log.i("BoardSetupViewModel", "Board Setup ViewModel created!")
        _editFieldVisible.value = false
    }



    private fun createBoardOnLoad() {
        viewModelScope.launch {
            for (i in 0..24) {
                val indexedText = "Index: ${convertIndexToLocation(i)} -- replace me!"
                val newBingoField = BingoField(indexedText, convertIndexToLocation(i), R.drawable.bingoboard_fieldbackground_bordered, boardTitle)
                insert(newBingoField)
                oneEntry.value = getLatest()
                val startText = oneEntry.value?.text
//                Log.i("BoardSetupViewModel", "Inserted new field, index: ${oneEntry.value?.fieldID}, text: $startText.")
            }
            val newDate = System.currentTimeMillis()
            val newBoardHolder = BoardHolder(boardTitle, newDate, "Setup", 0)
            insert(newBoardHolder)
            val latestHolder = getHolderByTitle(boardTitle)
//            Log.i("BoardSetupViewModel", "Inserted new BoardHolder, ID: ${latestHolder?.boardID}, title: ${latestHolder?.title}")


            _newBingoBoard.value = getEntriesFromParent()
  //          bingoEntries.value = getEntriesFromParent()
            bingoBoard2 = _newBingoBoard.value as MutableList<BingoField>
            //_newBingoBoard.value = bingoBoard2
            // This is where tonight.value = getTonightFromDatabase() on the tutorial
            // For me it would be something like bingoEntries = ...
            // DONE. Leaving comment for reference.
        }
    }





    override fun onCleared() {
        super.onCleared()
//        Log.i("BoardSetupViewModel", "BoardSetupViewModel destroyed!")
    }

    fun showEditField(index: Int) {
        _currentEditField.value = index
        _editFieldVisible.value = true
        //_editFieldText.value = bingoBoard[index].text
//        mObserver.setFieldText(bingoBoard2[index].text)
//        Log.i("BoardSetupViewModel", "ViewModel should have made edit popup visible")
    }


    fun editTextEntry(index : Int, newText : String) {
        if (index in 0..24) {
//            Log.i("BoardSetupViewModel", "mObserver field text is "+mObserver.getFieldText())
//            Log.i("BoardSetupViewModel", "Original text on field "+index.toString()+" is "+bingoBoard2[index].text+" and I'm trying to set it to "+newText)
            bingoBoard2[index].text = newText
//            Log.i("BoardSetupViewModel", "Changed text in field $index to "+bingoBoard2[index].text)
            _newBingoBoard.value = bingoBoard2
//            Log.i("BoardSetupViewModel", "Equivalency test. NewText = $newText | and _newBingoBoard.value[index].text = "+ _newBingoBoard.value!![index].text)
            viewModelScope.launch {
                tempEntry.value = getEntryAtIndex(boardTitle, convertIndexToLocation(index)) ?: return@launch
                val testingLocOfEntry = tempEntry.value!!.location
                val tempEntryID = tempEntry.value!!.fieldID
//                Log.i("BoardSetupViewModel", "Before calling update, fieldID=$tempEntryID, fieldLocation=$testingLocOfEntry")
                tempEntry.value!!.text = newText
                update(tempEntry.value!!)
                //trying to change from testingLocOfEntry to convertIndexToLocation(index)
                oneEntry.value = getEntryAtIndex(boardTitle, testingLocOfEntry)

//                Log.i("BoardSetupViewModel", "After updating, the new entry text on DB is "+oneEntry.value!!.text)
            }
            // Added this line to see if LiveData updates now
        }
        _editFieldVisible.value = false
    }

    fun finishedEditingEntry() {
        _editFieldVisible.value = false
    }


    fun doneWithSetup() {
        _navigateToGameFragment.value = true
    }


    // Old code to test DB manipulation
//    fun debugMakeEntry() {
//        viewModelScope.launch {
//            Log.i("BoardSetupViewModel", "Attempting to make an entry in database.")
//            val myNewBingoField = BingoField()
//            myNewBingoField.parentBoardName = "diegoTest"
//            myNewBingoField.location = 11
//            myNewBingoField.text = "Test text here"
//            insert(myNewBingoField)
//            oneEntry.value = getLatest()
//            Log.i("BoardSetupViewModel", "Inserted entry index ${myNewBingoField.location} with text ${myNewBingoField.text}")
//            Log.i("BoardSetupViewModel", "Now I'll try to read it.")
//        }
//    }
//
//
//    fun debugReadEntry() {
//        val lastEntry = oneEntry.value
//        Log.i("BoardSetupViewModel", "Last entry text is ${lastEntry?.text}.")
//        Log.i("BoardSetupViewModel", "Last entry ID is ${lastEntry?.fieldID}.")
//        Log.i("BoardSetupViewModel", "Last entry location is ${lastEntry?.location}.")
//        Log.i("BoardSetupViewModel", "Last entry parent is ${lastEntry?.parentBoardName}.")
//    }
//
//    // This worked. Tested and read properly.
//    fun debugReadEntryAtIndex() {
//        viewModelScope.launch {
//            Log.i("BoardSetupViewModel", "Attempting to read DiegoTest entry loc 11.")
//            oneEntry.value = getEntryAtIndex("diegoTest", 11)
//            val myEntry = oneEntry.value
//            Log.i("BoardSetupViewModel", "Test entry text is ${myEntry?.text}.")
//            Log.i("BoardSetupViewModel", "Test entry ID is ${myEntry?.fieldID}.")
//            Log.i("BoardSetupViewModel", "Test entry location is ${myEntry?.location}.")
//            Log.i("BoardSetupViewModel", "Test entry parent is ${myEntry?.parentBoardName}.")
//        }
//    }


    private fun convertIndexToLocation(index: Int): Byte {
        val locX = index / 5 + 1
        val locY = index % 5 + 1
        val loc = (locX*10 + locY).toByte()
        return loc
    }


}