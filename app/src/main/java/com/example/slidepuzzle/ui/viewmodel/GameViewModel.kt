package com.example.slidepuzzle.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.slidepuzzle.logic.PuzzleEngine
import com.example.slidepuzzle.logic.PuzzleTile
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private var engine: PuzzleEngine? = null
    
    private val _tiles = MutableStateFlow<List<PuzzleTile>>(emptyList())
    val tiles: StateFlow<List<PuzzleTile>> = _tiles.asStateFlow()

    private val _gridSize = MutableStateFlow(3)
    val gridSize: StateFlow<Int> = _gridSize.asStateFlow()

    private val _moves = MutableStateFlow(0)
    val moves: StateFlow<Int> = _moves.asStateFlow()

    private val _timeSeconds = MutableStateFlow(0L)
    val timeSeconds: StateFlow<Long> = _timeSeconds.asStateFlow()

    private val _isSolved = MutableStateFlow(false)
    val isSolved: StateFlow<Boolean> = _isSolved.asStateFlow()

    private val _imageBitmap = MutableStateFlow<Bitmap?>(null)
    val imageBitmap: StateFlow<Bitmap?> = _imageBitmap.asStateFlow()

    private var timerJob: Job? = null

    fun startGame(size: Int, bitmap: Bitmap? = null) {
        _gridSize.value = size
        _imageBitmap.value = bitmap
        engine = PuzzleEngine(size)
        engine?.shuffle()
        _tiles.value = engine?.getTiles() ?: emptyList()
        _moves.value = 0
        _timeSeconds.value = 0L
        _isSolved.value = false
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _timeSeconds.value += 1
            }
        }
    }

    fun onTileClicked(index: Int) {
        if (_isSolved.value) return

        if (engine?.moveTile(index) == true) {
            _tiles.value = engine?.getTiles() ?: emptyList()
            _moves.value += 1
            if (engine?.isSolved() == true) {
                _isSolved.value = true
                timerJob?.cancel()
            }
        }
    }

    fun resetGame() {
        engine?.shuffle()
        _tiles.value = engine?.getTiles() ?: emptyList()
        _moves.value = 0
        _timeSeconds.value = 0L
        _isSolved.value = false
        startTimer()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
