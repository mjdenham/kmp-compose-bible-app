@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.martin.bibleapp.ui.selector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.martin.bibleapp.ui.util.OrientationProvider
import com.martin.bibleapp.ui.util.OrientationProviderImpl
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.versification.BibleBook
import org.crosswire.jsword.versification.system.Versifications
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BookSelectionScreen(
    modifier: Modifier = Modifier,
    viewModel: BookSelectorViewModel = koinViewModel(),
    orientation: OrientationProvider.Orientation = OrientationProviderImpl().getOrientation(),
    onSelected: (BibleBook) -> Unit
) {
    val documentState by viewModel.sectionBooksState.collectAsState()

    Column {
        documentState.forEach { section ->
            Text(
                text = section.sectionName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier.padding(8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(getColumnCount(orientation)),
            ) {
                items(section.books) { book ->
                    SelectionButton(book.osis, modifier) {
                        onSelected(book)
                    }
                }
            }
        }
    }
}

@Composable
fun ChapterSelectionScreen(
    book: BibleBook,
    modifier: Modifier = Modifier,
    viewModel: ChapterSelectorViewModel = koinViewModel{ parametersOf(book) },
    orientation: OrientationProvider.Orientation = OrientationProviderImpl().getOrientation(),
    onSelected: (Int) -> Unit
) {
    //TODO after lifecycle library upgrade add withLifecycle - currently tests fail withLifecycle
    val documentState: SelectionModel by viewModel.selectorState.collectAsState()

    documentState.numChapters?.let { chaps ->
        LazyVerticalGrid(columns = GridCells.Fixed(getColumnCount(orientation))) {
            items((1..chaps).toList()) {
                SelectionButton(it.toString(), modifier) {
                    viewModel.selectReference(Verse(DEFAULT_VERSIFICATION, book, it, 1))
                    onSelected(it)
                }
            }
        }
    }
}

@Composable
private fun SelectionButton(
    text: String,
    modifier: Modifier,
    onSelected: () -> Unit
) {
    OutlinedButton(
        onClick = { onSelected() },
        modifier = modifier.padding(4.dp, 0.dp),
        shape = RoundedCornerShape(20),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier = modifier.padding(0.dp)
        )
    }
}

@Composable
private fun getColumnCount(orientation: OrientationProvider.Orientation): Int =
    if (orientation == OrientationProvider.Orientation.Landscape) {
        ColumnsLandscape
    } else {
        ColumnsPortrait
    }

private const val ColumnsLandscape = 13
private const val ColumnsPortrait = 6
private val DEFAULT_VERSIFICATION = Versifications.instance().getVersification("KJV")
