package com.martin.kmpsword

import com.martin.kmpsword.passage.Verse
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ZVerseBackendTest {

    private var backend = ZVerseBackend()

    @BeforeTest
    fun setup() {
    }

    @Test
    fun readVerse() {
        val result = backend.getRawText(Verse(1, 1, 1))
        assertContains(result, "In the beginning")
    }

    @Test
    fun readChapter() {
        val result = backend.getRawText(Verse(1, 1, 31))
        listOf("And God looked upon all that he had made and indeed it was very good".split(" ").forEach { word: String ->
            assertContains(result, word)
        })
    }
}