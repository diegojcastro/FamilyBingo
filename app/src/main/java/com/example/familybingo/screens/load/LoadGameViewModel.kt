package com.example.familybingo.screens.load

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familybingo.database.BingoDatabaseDao
import com.example.familybingo.database.BoardHolder
import kotlinx.coroutines.launch

class LoadGameViewModel(
    val database: BingoDatabaseDao,
    application: Application) : AndroidViewModel(application)  {

    val allTitles = MutableLiveData<List<BoardHolder>?>()

    init {
        initializeAllTitles()

    }

    private fun initializeAllTitles() {
        viewModelScope.launch {
            allTitles.value = getAllTitles()
        }
    }

    private suspend fun getAllTitles() : List<BoardHolder>? {
        val titles = database.getAllBoards()
        return titles
    }

    fun callDatabaseDeletionWithTitle(title : String) {
        viewModelScope.launch{
            deleteAllDataWithTitle(title)
            // Will this execute before the data is deleted?
            allTitles.value = getAllTitles()
        }
    }

    private suspend fun deleteAllDataWithTitle(title : String) {
        database.removeBoard(title)
        database.removeHolder(title)
        Log.i("LoadGameViewModel", "Ran database deletion on key: $title")


    }


}