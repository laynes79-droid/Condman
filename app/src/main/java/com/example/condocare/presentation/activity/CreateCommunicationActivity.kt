package com.example.condocare.presentation.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.condocare.databinding.ActivityCreateCommunicationBinding
import com.example.condocare.presentation.viewmodel.CreateCommunicationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCommunicationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateCommunicationBinding
    private val viewModel: CreateCommunicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCommunicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.buttonCreate.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val message = binding.editTextMessage.text.toString()
            val isEmergency = binding.switchEmergency.isChecked

            if (title.isNotBlank() && message.isNotBlank()) {
                viewModel.createCommunication(title, message, isEmergency)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        viewModel.creationResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Communication created successfully", Toast.LENGTH_SHORT).show()
                finish() // Go back to MainActivity
            }.onFailure {
                Toast.makeText(this, "Failed to create communication: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
