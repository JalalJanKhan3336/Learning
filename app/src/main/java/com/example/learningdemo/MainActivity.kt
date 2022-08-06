package com.example.learningdemo

import java.io.File
import android.os.Bundle
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.app.AppCompatActivity
import com.example.learningdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnLogin.setOnClickListener {
            var isFormFilled = true
            val email = mBinding.etEmail.text.toString().trim()

            if (email.isEmpty()) {
                mBinding.etEmail.error = "Email is missing!"
                isFormFilled = false
            } else
                mBinding.etEmail.error = null

            val password = mBinding.etPassword.text.toString().trim()

            if (password.isEmpty()) {
                mBinding.etPassword.error = "Password is missing!"
                isFormFilled = false
            } else
                mBinding.etPassword.error = null

            if (isFormFilled) {
                // Authenticate User
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        // Cache User's credentials
                        val pref = getSharedPreferences("learning_demo", Context.MODE_PRIVATE)
                        pref
                            .edit()
                            .putString("key_uid", authResult.user?.uid)
                            .putString("key_email", email)
                            .putString("key_password", password)
                            .apply()
                    }
                    .addOnFailureListener { exception ->
                        // Log exception into a file
                        val file = File("logs.txt")
                        file.appendText(
                            text = exception.message.toString()
                        )
                    }
            }
        }
    }

}