package com.example.familybingo.screens.load

import android.app.Application
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


}