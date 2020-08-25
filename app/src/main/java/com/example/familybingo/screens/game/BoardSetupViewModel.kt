package com.example.familybingo.screens.game

import android.util.Log
import androidx.lifecycle.ViewModel

class BoardSetupViewModel : ViewModel() {
    //Delete this later
    init {
        Log.i("BoardSetupViewModel", "Board Setup ViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("BoardSetupViewModel", "BoardSetupViewModel destroyed!")
    }
}