package com.martin.bibleapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.bible.Bible
import com.martin.bibleapp.ui.util.ResultIs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.crosswire.jsword.passage.KeyText

class SearchViewModel(val bible: Bible): ViewModel() {
    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions = _suggestions.asStateFlow()

    private val _searchResultsState = MutableStateFlow<ResultIs<List<KeyText>>>(ResultIs.Loading)
    val searchResultsState = _searchResultsState.asStateFlow()

    fun search(searchText: String) {
        _searchResultsState.value = ResultIs.Loading
        viewModelScope.launch {
            val searchResults = bible.search(searchText)
            _searchResultsState.update {
                ResultIs.Success(searchResults)
            }

            _suggestions.update {
                _suggestions.value + searchText
            }
        }
    }
}