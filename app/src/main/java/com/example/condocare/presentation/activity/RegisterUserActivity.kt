package com.example.condocare.presentation.activity

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.condocare.R
import com.example.condocare.databinding.ActivityRegisterUserBinding
import com.example.condocare.presentation.viewmodel.RegisterUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding
    private val viewModel: RegisterUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        setupListeners()
        setupObservers()
    }

    private fun setupSpinner() {
        val roles = arrayOf("Resident", "Manager")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRole.adapter = adapter
    }

    private fun setupListeners() {
        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val apartment = binding.editTextApartment.text.toString()
            val role = binding.spinnerRole.selectedItem.toString()

            if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank() && apartment.isNotBlank()) {
                viewModel.registerUser(name, email, password, apartment, role)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        viewModel.registrationResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_LONG).show()
                finish() // Go back to LoginActivity
            }.onFailure {
                Toast.makeText(this, "Registration failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
