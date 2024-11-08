package com.martin.bibleapp.di

import com.martin.bibleapp.data.document.DocumentInstallation
import com.martin.bibleapp.data.reference.RoomCurrentReferenceRepository
import com.martin.bibleapp.data.repository.sword.SwordReader
import com.martin.bibleapp.domain.bible.Bible
import com.martin.bibleapp.domain.bible.BibleReader
import com.martin.bibleapp.domain.bible.ReferenceSelectionUseCase
import com.martin.bibleapp.domain.install.InstallBsbUseCase
import com.martin.bibleapp.domain.install.Installation
import com.martin.bibleapp.domain.reference.CurrentReferenceRepository
import com.martin.bibleapp.ui.appsetup.AppSetupViewModel
import com.martin.bibleapp.ui.document.DocumentViewModel
import com.martin.bibleapp.ui.search.SearchViewModel
import com.martin.bibleapp.ui.selector.BookSelectorViewModel
import com.martin.bibleapp.ui.selector.ChapterSelectorViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
//    single<BibleReader> { UsfmFileReader() }
    single<BibleReader> { SwordReader() }
    single<CurrentReferenceRepository> { RoomCurrentReferenceRepository(get()) }
    single<Bible> { Bible(get(), get(), get()) }
    single<ReferenceSelectionUseCase> { ReferenceSelectionUseCase(get(), get()) }
    single<Installation> { DocumentInstallation() }
    single<InstallBsbUseCase> { InstallBsbUseCase(get()) }
    viewModel { DocumentViewModel(get(), get()) }
    viewModel { BookSelectorViewModel() }
    viewModel { ChapterSelectorViewModel(get(), get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { AppSetupViewModel(get()) }
}

private var koinStarted = false

fun initializeKoin(appDeclaration: KoinAppDeclaration = {}) {
    // Compose 1.7.0 seems to have a bug which causes MainViewController to be created twice, consequently initializeKoin would be called twice
    if (!koinStarted) {
        koinStarted = true

        startKoin {
            appDeclaration()
            modules(appModule)
        }
    }
}