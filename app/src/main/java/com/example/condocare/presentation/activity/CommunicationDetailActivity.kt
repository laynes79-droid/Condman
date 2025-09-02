package com.example.condocare.presentation.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.condocare.data.model.Communication
import com.example.condocare.databinding.ActivityCommunicationDetailBinding
import com.example.condocare.presentation.adapters.ComplementAdapter
import com.example.condocare.presentation.viewmodel.CommunicationDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunicationDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COMMUNICATION_ID = "extra_communication_id"
    }

    private lateinit var binding: ActivityCommunicationDetailBinding
    private val viewModel: CommunicationDetailViewModel by viewModels()
    private lateinit var complementAdapter: ComplementAdapter
    private var communicationId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunicationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        communicationId = intent.getIntExtra(EXTRA_COMMUNICATION_ID, -1)
        if (communicationId == -1) {
            Toast.makeText(this, "Invalid Communication ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupRecyclerView()
        setupListeners()
        setupObservers()

        viewModel.loadCommunicationDetail(communicationId)
    }

    private fun setupRecyclerView() {
        complementAdapter = ComplementAdapter(emptyList())
        binding.recyclerViewComplements.apply {
            adapter = complementAdapter
            layoutManager = LinearLayoutManager(this@CommunicationDetailActivity)
        }
    }

    private fun setupListeners() {
        binding.buttonAddComplement.setOnClickListener {
            val message = binding.editTextComplement.text.toString()
            if (message.isNotBlank()) {
                viewModel.addComplement(communicationId, message)
                binding.editTextComplement.text.clear()
            }
        }

        binding.buttonCloseCommunication.setOnClickListener {
            viewModel.closeCommunication(communicationId)
        }
    }

    private fun setupObservers() {
        viewModel.communication.observe(this) { communication ->
            updateUi(communication)
        }

        viewModel.updateResult.observe(this) { result ->
            result.onSuccess { updatedCommunication ->
                Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show()
                updateUi(updatedCommunication)
            }.onFailure {
                Toast.makeText(this, "Update failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }
    }

    private fun updateUi(communication: Communication) {
        binding.textViewTitle.text = communication.title
        binding.textViewMessage.text = communication.message
        binding.textViewStatus.text = "Status: ${communication.status}"
        complementAdapter.updateData(communication.complements)

        val userRole = viewModel.getUserRole()
        if (userRole == "Manager" && communication.status.equals("Open", ignoreCase = true)) {
            binding.buttonCloseCommunication.visibility = View.VISIBLE
        } else {
            binding.buttonCloseCommunication.visibility = View.GONE
        }
    }
}
