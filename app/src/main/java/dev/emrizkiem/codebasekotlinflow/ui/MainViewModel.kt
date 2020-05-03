package dev.emrizkiem.codebasekotlinflow.ui

import androidx.lifecycle.ViewModel
import dev.emrizkiem.codebasekotlinflow.model.data.Post
import dev.emrizkiem.codebasekotlinflow.model.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainViewModel(private val repository: MainRepository): ViewModel() {

    fun getAllPost() = repository.getAllPosts()

    fun addPost(post: Post) = repository.addPost(post)
}