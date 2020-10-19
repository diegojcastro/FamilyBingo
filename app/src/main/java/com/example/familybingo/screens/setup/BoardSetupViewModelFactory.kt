package com.example.familybingo.screens.setup

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.familybingo.database.BingoDatabaseDao

class BoardSetupViewModelFactory(
    private val dataSource: BingoDatabaseDao,
    private val application: Application,
    private val myBoardTitle: String) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoardSetupViewModel::class.java)) {
            return BoardSetupViewModel(dataSource, application, myBoardTitle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}