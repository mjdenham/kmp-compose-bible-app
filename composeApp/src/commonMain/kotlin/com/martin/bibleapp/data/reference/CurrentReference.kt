package com.martin.bibleapp.data.reference

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.martin.bibleapp.domain.reference.BibleBook

@Entity
data class CurrentReference(
    @PrimaryKey
    val id: Int,
    val book: BibleBook,
    val chapter: Int,
    val verse: Int
)