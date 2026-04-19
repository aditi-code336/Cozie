package com.example.project.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.data.CozieDatabase
import com.example.project.data.SmallWin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArcadeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = CozieDatabase.getDatabase(application)
    private val cozieDao = database.cozieDao()

    private val _bingoState = MutableStateFlow<Set<String>>(emptySet())
    val bingoState: StateFlow<Set<String>> = _bingoState.asStateFlow()

    init {
        // In a real app, we'd fetch the current day's progress from the DB
        // For now, initializing with empty
    }

    fun toggleBingoItem(item: String) {
        val current = _bingoState.value.toMutableSet()
        if (current.contains(item)) {
            current.remove(item)
        } else {
            current.add(item)
            // Save to database when checked
            viewModelScope.launch {
                cozieDao.insertSmallWin(SmallWin(winType = item, timestamp = System.currentTimeMillis()))
            }
        }
        _bingoState.value = current
    }
}
