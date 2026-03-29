package com.example.slidepuzzle.logic

import android.graphics.Bitmap

object ImageSplitter {
    /**
     * Splits a bitmap into a grid of smaller bitmaps.
     * @param bitmap The source bitmap to split.
     * @param gridSize The number of rows and columns (e.g., 3 for 3x3).
     * @return A list of bitmaps, in order from top-left to bottom-right.
     */
    fun splitImage(bitmap: Bitmap, gridSize: Int): List<Bitmap> {
        val pieces = mutableListOf<Bitmap>()
        val width = bitmap.width
        val height = bitmap.height
        val pieceWidth = width / gridSize
        val pieceHeight = height / gridSize

        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val x = col * pieceWidth
                val y = row * pieceHeight
                
                // Ensure we don't exceed bitmap bounds due to rounding
                val w = if (col == gridSize - 1) width - x else pieceWidth
                val h = if (row == gridSize - 1) height - y else pieceHeight
                
                val piece = Bitmap.createBitmap(bitmap, x, y, w, h)
                pieces.add(piece)
            }
        }
        return pieces
    }
}
