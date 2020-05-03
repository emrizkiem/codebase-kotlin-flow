package dev.emrizkiem.codebasekotlinflow.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.emrizkiem.codebasekotlinflow.model.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MainRepository::class.java)
            .newInstance(MainRepository())
    }
}