package dev.emrizkiem.codebasekotlinflow.model.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import dev.emrizkiem.codebasekotlinflow.model.data.Post
import dev.emrizkiem.codebasekotlinflow.util.Constants
import dev.emrizkiem.codebasekotlinflow.util.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class MainRepository {

    private val mPostCollection = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_POST)

    /**
     * Returns Flow of [State] which retrieves all posts from cloud firestore collection.
     */
    fun getAllPosts() = flow<State<List<Post>>> {
        // Emit loading state.
        emit(State.loading())

        val snapshot = mPostCollection.get().await()
        val post = snapshot.toObjects(Post::class.java)

        // Emit success with data.
        emit(State.success(post))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    /**
     * Adds post [post] into the cloud firestore collection.
     * @return The Flow of [State] which will store state of current action.
     */
    fun addPost(post: Post) = flow<State<DocumentReference>> {
        // Emit loading state.
        emit(State.loading())

        val postReference = mPostCollection.add(post).await()

        // Emit success state with post reference.
        emit(State.success(postReference))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}