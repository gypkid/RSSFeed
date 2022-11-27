package com.example.rssfeed.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.rssfeed.databinding.ActivityMainBinding
import com.example.rssfeed.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this, RssFeedActivity::class.java))
        }
    }

    fun register(v: View){
        var email = binding.etEmail.text.toString()
        var password = binding.etPassword.text.toString()
        Log.d("emaile", email)
        Log.d("emaile", password)

        if(TextUtils.isEmpty(email)){
            binding.etEmail.setError("Email jest wymagany")
        }

        if(TextUtils.isEmpty(password)){
            binding.etPassword.setError("Hasło jest wymagane")
        }

        if(password.length < 6){
            binding.etPassword.setError("Hasło musi mieć przynajmniej 6 znaków")
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("createUser", "createUserWithEmail:success")
                    val user = auth.currentUser
                    redirect(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("createUser", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    redirect(null)
                }
            }
    }

    private fun redirect(user: FirebaseUser?) {
        if (user != null){
            startActivity(Intent(this, RssFeedActivity::class.java))
        }
    }
}

