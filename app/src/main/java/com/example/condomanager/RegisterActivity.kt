package com.example.condomanager

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.condomanager.data.CommunicationRepository
import com.example.condomanager.model.Role
import com.example.condomanager.model.User
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nameEditText = findViewById<EditText>(R.id.editTextName)
        val contactEditText = findViewById<EditText>(R.id.editTextContact)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val confirmPasswordEditText = findViewById<EditText>(R.id.editTextConfirmPassword)
        val roleRadioGroup = findViewById<RadioGroup>(R.id.radioGroupRole)
        val registerButton = findViewById<Button>(R.id.buttonRegister)

        val repository = CommunicationRepository.getInstance(applicationContext)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val contact = contactEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val selectedRoleId = roleRadioGroup.checkedRadioButtonId
            val role = if (selectedRoleId == R.id.radioButtonManager) Role.MANAGER else Role.RESIDENT

            if (name.isBlank() || contact.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, R.string.passwords_do_not_match, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newUser = User(
                name = name,
                contact = contact,
                role = role,
                password = password
            )

            lifecycleScope.launch {
                repository.addUser(newUser)
                Toast.makeText(this@RegisterActivity, "User created: ${newUser.name}", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
