package com.martin.bibleapp.data.repository.usfm

import androidx.annotation.VisibleForTesting
import bibleapp.composeapp.generated.resources.Res
import com.martin.bibleapp.domain.bible.BibleReader
import com.martin.bibleapp.domain.reference.BibleBook
import org.jetbrains.compose.resources.ExperimentalResourceApi

class UsfmFileReader : BibleReader {
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun read(book: BibleBook): String {
        return Res.readBytes("files/bsb/usfm/${book.usfmCode}.usfm")
            .decodeToString()
            .split("\n")
            .map { toHtml(it) }
            .filter { it.isNotEmpty() }
            .joinToString("")
    }

    @VisibleForTesting
    fun toHtml(line: String): String {
        if (line.isEmpty()) return ""
        val cleanLine = line.replace("\\\\f.*\\\\f\\*".toRegex(), "")
        val tag = cleanLine.substringBefore(" ")
        return when (tag) {
            "\\v" -> cleanLine.drop(3)
            "\\b" -> "<br />"
            else -> ""
        }
    }
}