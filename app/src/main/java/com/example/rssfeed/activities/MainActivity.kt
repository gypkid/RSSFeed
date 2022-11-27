package com.example.rssfeed.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.rssfeed.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            redirect(currentUser)
        }
    }

    fun SignIn(v : View){
        var email = binding.editTextTextEmailAddress.text.toString()
        var password = binding.editTextTextPassword.text.toString()

        if(TextUtils.isEmpty(email)){
            binding.editTextTextEmailAddress.setError("Email jest wymagany")
        }

        if(TextUtils.isEmpty(password)){
            binding.editTextTextPassword.setError("Hasło jest wymagane")
        }

        if(password.length < 6){
            binding.editTextTextPassword.setError("Hasło musi mieć przynajmniej 6 znaków")
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("loginUser", "loginUserWithEmail:success")
                    val user = auth.currentUser
                    redirect(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("loginUser", "loginUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Login failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        Firebase.auth.signOut()
    }

    private fun redirect(user: FirebaseUser?) {
        startActivity(Intent(this, RssFeedActivity::class.java).putExtra("user", user))
    }

    fun register(v: View){
        startActivity(Intent(this, RegisterActivity::class.java))
    }

}