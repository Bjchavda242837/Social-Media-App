package com.example.socialmedia

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmedia.daos.PostDao

class CreatePostActivity : AppCompatActivity() {

    private lateinit var postbtn: Button
    private lateinit var postinput: EditText
    private lateinit var postDao: PostDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        postbtn = findViewById(R.id.postBtn)
        postinput = findViewById(R.id.postinput)
        postDao = PostDao()

        postbtn.setOnClickListener{
            val input = postinput.text.toString().trim()
            if (input.isNotEmpty()){

                postDao.AddPost(input)
                finish()

            }
        }


    }
}