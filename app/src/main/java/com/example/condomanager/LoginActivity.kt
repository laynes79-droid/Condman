package com.example.condomanager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.condomanager.data.CommunicationRepository
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("condomanager_prefs", Context.MODE_PRIVATE)

        if (sharedPreferences.getLong("logged_in_user_id", -1) != -1L) {
            goToMainActivity()
            return
        }

        setContentView(R.layout.activity_login)

        val contactEditText = findViewById<EditText>(R.id.editTextLoginContact)
        val passwordEditText = findViewById<EditText>(R.id.editTextLoginPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        val goToRegisterTextView = findViewById<TextView>(R.id.textViewGoToRegister)

        val repository = CommunicationRepository.getInstance(applicationContext)

        loginButton.setOnClickListener {
            val contact = contactEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (contact.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = repository.findUserByCredentials(contact, password)
                if (user != null) {
                    sharedPreferences.edit().putLong("logged_in_user_id", user.id).apply()
                    goToMainActivity()
                } else {
                    Toast.makeText(this@LoginActivity, R.string.login_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }

        goToRegisterTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
