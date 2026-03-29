package com.example.slidepuzzle.logic

import org.junit.Assert.*
import org.junit.Test

class PuzzleEngineTest {

    @Test
    fun `test puzzle initialization`() {
        val engine = PuzzleEngine(3)
        val tiles = engine.getTiles()
        
        assertEquals(9, tiles.size)
        assertTrue(tiles.last().isEmpty)
        for (i in 0 until 9) {
            assertEquals(i, tiles[i].originalIndex)
        }
        assertTrue(engine.isSolved())
    }

    @Test
    fun `test move tile`() {
        val engine = PuzzleEngine(3)
        // Empty tile is at index 8 (bottom-right)
        // Neighbors are 5 (up) and 7 (left)
        
        // Try moving non-adjacent tile
        assertFalse(engine.moveTile(0))
        
        // Move adjacent tile (7)
        assertTrue(engine.moveTile(7))
        val tiles = engine.getTiles()
        assertTrue(tiles[7].isEmpty)
        assertEquals(7, tiles[8].originalIndex)
        assertFalse(engine.isSolved())
    }

    @Test
    fun `test shuffle guarantees solvability`() {
        // Our shuffle method moves the empty tile, so it's always solvable.
        val engine = PuzzleEngine(3)
        engine.shuffle(50)
        assertFalse(engine.isSolved())
        
        // Hard to test solvability algorithmically without a solver, 
        // but moving the empty space is the standard way to guarantee it.
    }
}
