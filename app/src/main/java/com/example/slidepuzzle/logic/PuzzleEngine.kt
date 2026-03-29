package com.example.slidepuzzle.logic

import kotlin.random.Random

class PuzzleEngine(val gridSize: Int) {
    private val totalTiles = gridSize * gridSize
    private var tiles = mutableListOf<PuzzleTile>()

    init {
        reset()
    }

    fun reset() {
        tiles.clear()
        for (i in 0 until totalTiles - 1) {
            tiles.add(PuzzleTile(originalIndex = i))
        }
        // Last tile is empty
        tiles.add(PuzzleTile(originalIndex = totalTiles - 1, isEmpty = true))
    }

    fun shuffle(steps: Int = 1000) {
        repeat(steps) {
            val emptyIndex = tiles.indexOfFirst { it.isEmpty }
            val neighbors = getNeighbors(emptyIndex)
            val randomNeighbor = neighbors.random()
            swap(emptyIndex, randomNeighbor)
        }
    }

    private fun getNeighbors(index: Int): List<Int> {
        val neighbors = mutableListOf<Int>()
        val row = index / gridSize
        val col = index % gridSize

        if (row > 0) neighbors.add(index - gridSize) // Up
        if (row < gridSize - 1) neighbors.add(index + gridSize) // Down
        if (col > 0) neighbors.add(index - 1) // Left
        if (col < gridSize - 1) neighbors.add(index + 1) // Right

        return neighbors
    }

    private fun swap(i: Int, j: Int) {
        val temp = tiles[i]
        tiles[i] = tiles[j]
        tiles[j] = temp
    }

    fun moveTile(index: Int): Boolean {
        val emptyIndex = tiles.indexOfFirst { it.isEmpty }
        if (isAdjacent(index, emptyIndex)) {
            swap(index, emptyIndex)
            return true
        }
        return false
    }

    private fun isAdjacent(i: Int, j: Int): Boolean {
        val rowI = i / gridSize
        val colI = i % gridSize
        val rowJ = j / gridSize
        val colJ = j % gridSize

        return (Math.abs(rowI - rowJ) == 1 && colI == colJ) ||
                (Math.abs(colI - colJ) == 1 && rowI == rowJ)
    }

    fun isSolved(): Boolean {
        for (i in 0 until totalTiles) {
            if (tiles[i].originalIndex != i) return false
        }
        return true
    }

    fun getTiles(): List<PuzzleTile> = tiles.toList()
}

data class PuzzleTile(
    val originalIndex: Int,
    val isEmpty: Boolean = false
)
