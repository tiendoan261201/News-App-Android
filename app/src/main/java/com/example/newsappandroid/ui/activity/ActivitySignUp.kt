package com.example.newsappandroid.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.newsappandroid.R
import com.example.newsappandroid.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class ActivitySignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        iniView()
        initListener()


    }

    private fun initListener() {
        binding.signUpButton.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPass = binding.signupConfirm.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty()){
                if(password == confirmPass){
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(this,"Sign up succesfully",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ActivitySignUp,LoginActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"Password is not match",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Can not be empty",Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister2.setOnClickListener {
            val intent = Intent(this@ActivitySignUp,LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun iniView() {

    }
}