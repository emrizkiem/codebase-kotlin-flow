package dev.emrizkiem.codebasekotlinflow

import android.app.Application
import dev.emrizkiem.codebasekotlinflow.model.repository.MainRepository
import dev.emrizkiem.codebasekotlinflow.ui.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

@ExperimentalCoroutinesApi
class AppController: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            modules(kotlinFlow)
        }
    }
}

@ExperimentalCoroutinesApi
val kotlinFlow = module {
    single { MainRepository() }

    viewModel { MainViewModel(get()) }
}