package com.example.familybingo.screens.load

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.familybingo.database.BingoDatabaseDao
import com.example.familybingo.screens.setup.BoardSetupViewModel

class LoadGameViewModelFactory(
    private val dataSource: BingoDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoadGameViewModel::class.java)) {
                return LoadGameViewModel(dataSource, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}