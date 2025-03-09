package com.example.socialmedia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Visibility
import com.example.socialmedia.Model.User
import com.example.socialmedia.daos.UserDao
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class SigninActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var progressBar: ProgressBar
    private lateinit var SignButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        auth = FirebaseAuth.getInstance()

        progressBar = findViewById(R.id.progressbar)
        SignButton = findViewById(R.id.btnGoogleSignIn)

        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        SignButton.setOnClickListener {
            signIn() } // ✅ Calling only ONE signIn() function


    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        UpdateUi(currentUser)
    }

    private fun signIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
                    signInLauncher.launch(intentSenderRequest)  // ✅ Fixed here
                } catch (e: Exception) {
                    Log.e("GoogleSignIn", "Error launching sign-in intent", e)
                }
            }
            .addOnFailureListener { e ->
                Log.e("GoogleSignIn", "Sign-in failed", e)
            }
    }

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    val credential: SignInCredential = oneTapClient.getSignInCredentialFromIntent(result.data)
                    val idToken = credential.googleIdToken
                    if (idToken != null) {
                        firebaseAuthWithGoogle(idToken)
                    }
                } catch (e: Exception) {
                    Log.e("GoogleSignIn", "Error retrieving credential", e)
                }
            }
        }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        SignButton.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val FirebaseUser = auth.currentUser

                    UpdateUi(FirebaseUser)


                    Log.d("GoogleSignIn", "Firebase Auth success: ${auth.currentUser?.displayName}")
                } else {
                    Log.e("GoogleSignIn", "Firebase Auth failed", task.exception)
                }
            }
    }

    private fun UpdateUi(firebaseUser: FirebaseUser?) {
        if (firebaseUser != null){

            val user = User(firebaseUser.uid, firebaseUser.displayName.toString(),firebaseUser.photoUrl.toString())
            val userdao = UserDao()
            userdao.addUser(user)

            val mainAc = Intent(this,MainActivity::class.java)
            startActivity(mainAc)
            finish()
        }else {
            SignButton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }


}