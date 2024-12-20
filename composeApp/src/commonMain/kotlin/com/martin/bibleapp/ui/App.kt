package com.martin.bibleapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.martin.bibleapp.ui.appsetup.AppSetup
import com.martin.bibleapp.ui.bottomnavbar.BottomNavBar
import com.martin.bibleapp.ui.document.Document
import com.martin.bibleapp.ui.search.SearchScreen
import com.martin.bibleapp.ui.selector.BookSelectionScreen
import com.martin.bibleapp.ui.selector.ChapterSelectionScreen
import com.martin.bibleapp.ui.theme.BibleTheme
import com.martin.bibleapp.ui.topnavbar.DocumentTopNavBar
import com.martin.bibleapp.ui.topnavbar.SimpleTopNavBar
import kotlinx.serialization.Serializable
import org.crosswire.jsword.versification.BibleBook
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

/**
 * Values that represent the screens in the app
 */
sealed class BibleScreen {
    @Serializable
    data object Setup: BibleScreen()
    @Serializable
    data class BibleView(val page: Int): BibleScreen()
    @Serializable
    data object BibleBookPicker: BibleScreen()
    @Serializable
    data class BibleChapterPicker(val bookName: String): BibleScreen()
    @Serializable
    data object Search: BibleScreen()
    @Serializable
    data object Test: BibleScreen()
}

@Composable
@Preview
fun App(
    navController: NavHostController = rememberNavController()
) {
    BibleTheme {
        KoinContext {
            var documentTopNavBar by rememberSaveable { mutableStateOf(false) }
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                documentTopNavBar = destination.route?.contains("BibleView") ?: false
            }

            Scaffold(
                topBar = {
                    if (documentTopNavBar) {
                        DocumentTopNavBar(navController)
                    } else {
                        SimpleTopNavBar()
                    }
                },
                bottomBar = {
                    BottomNavBar(navController)
                },
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = BibleScreen.Setup,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    composable<BibleScreen.Setup> {
                        AppSetup {
                            navController.popBackStack()
                            navController.navigate(BibleScreen.BibleView(0))
                        }
                    }
                    composable<BibleScreen.BibleView> {
                        val page = it.toRoute<BibleScreen.BibleView>().page
                        Document(page)
                    }
                    composable<BibleScreen.BibleBookPicker> {
                        BookSelectionScreen(onSelected = { book ->
                            navController.navigate(BibleScreen.BibleChapterPicker(book.name))
                        })
                    }
                    composable<BibleScreen.BibleChapterPicker> {
                        val bookName = it.toRoute<BibleScreen.BibleChapterPicker>().bookName
                        val book = BibleBook.valueOf(bookName)
                        ChapterSelectionScreen(book) { selectedChapter ->
                            navController.popBackStack(BibleScreen.BibleBookPicker, true)
                        }
                    }
                    composable<BibleScreen.Search> {
                        SearchScreen()
                    }
                }
            }
        }
    }
}

