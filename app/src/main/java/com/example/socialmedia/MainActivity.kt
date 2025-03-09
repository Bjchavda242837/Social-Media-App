package com.example.socialmedia

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedia.Model.Post
import com.example.socialmedia.daos.PostDao
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity(), IPostAdapter {

    private lateinit var createPost: FloatingActionButton
    lateinit var postDao: PostDao
    private lateinit var adapter: PostAdapter
    private lateinit var postRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createPost = findViewById(R.id.addPostBtn)
        postRecyclerView = findViewById(R.id.post_recycle)
        postDao = PostDao()

        createPost.setOnClickListener {
            val addPost = Intent(this,CreatePostActivity::class.java)
            startActivity(addPost)
        }

        setUpRV()

    }

    private fun setUpRV() {

        val postsCollections = postDao.postCollection
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions,this)


        postRecyclerView.adapter = adapter
        postRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.UpdateLikes(postId)
    }
}