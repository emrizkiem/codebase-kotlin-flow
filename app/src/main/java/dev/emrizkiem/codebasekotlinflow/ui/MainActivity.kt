package dev.emrizkiem.codebasekotlinflow.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import dev.emrizkiem.codebasekotlinflow.R
import dev.emrizkiem.codebasekotlinflow.model.data.Post
import dev.emrizkiem.codebasekotlinflow.util.State
import dev.emrizkiem.codebasekotlinflow.util.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: MainViewModel

    // Coroutines scope
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, ViewModelFactory())
            .get(MainViewModel::class.java)

        button_load.setOnClickListener(this)
        button_add.setOnClickListener(this)
    }

    private suspend fun loadPost() {
        viewModel.getAllPost().collect { state ->
            when (state) {
                is State.Loading -> {
                    showToast("Loading")
                }

                is State.Success -> {
                    val postText = state.data.joinToString("\n") {
                        "${it.postContent} ~ ${it.postAuthor}"
                    }

                    text_post_content.text = postText
                }

                is State.Failed -> showToast("Failed! ${state.message}")
            }
        }
    }

    private suspend fun addPost(post: Post) {
        viewModel.addPost(post).collect { state ->
            when (state) {
                is State.Loading -> {
                    showToast("Loading")
                    button_add.isEnabled = false
                }

                is State.Success -> {
                    showToast("Posted")
                    field_post_content.setText("")
                    button_add.isEnabled = true
                }

                is State.Failed -> {
                    showToast("Failed! ${state.message}")
                    button_add.isEnabled = true
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            button_load.id -> {
                uiScope.launch { loadPost() }
            }
            button_add.id -> {
                uiScope.launch {
                    addPost(
                        Post(
                            postContent = field_post_content.text.toString(),
                            postAuthor = "Unknown"
                        )
                    )
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
