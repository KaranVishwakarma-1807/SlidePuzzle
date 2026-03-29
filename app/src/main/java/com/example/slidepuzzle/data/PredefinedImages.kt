package com.example.slidepuzzle.data

data class LibraryImage(
    val id: Int,
    val url: String,
    val name: String
)

val PREDEFINED_IMAGES = listOf(
    LibraryImage(1, "https://picsum.photos/id/10/800/800", "Mountains"),
    LibraryImage(2, "https://picsum.photos/id/11/800/800", "Nature"),
    LibraryImage(3, "https://picsum.photos/id/12/800/800", "Beach"),
    LibraryImage(4, "https://picsum.photos/id/13/800/800", "Forest"),
    LibraryImage(5, "https://picsum.photos/id/14/800/800", "Sea"),
    LibraryImage(6, "https://picsum.photos/id/15/800/800", "River")
)
