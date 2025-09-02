package com.example.condocare.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.condocare.databinding.ActivityMainBinding
import com.example.condocare.presentation.adapters.CommunicationAdapter
import com.example.condocare.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var communicationAdapter: CommunicationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        setupObservers()

        viewModel.loadCommunications()
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to the activity
        viewModel.loadCommunications()
    }

    private fun setupRecyclerView() {
        communicationAdapter = CommunicationAdapter(emptyList()) { communication ->
            val intent = Intent(this, CommunicationDetailActivity::class.java)
            intent.putExtra(CommunicationDetailActivity.EXTRA_COMMUNICATION_ID, communication.id)
            startActivity(intent)
        }
        binding.recyclerViewCommunications.apply {
            adapter = communicationAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setupListeners() {
        binding.buttonCreateCommunication.setOnClickListener {
            val intent = Intent(this, CreateCommunicationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupObservers() {
        viewModel.communications.observe(this) { communications ->
            communicationAdapter.updateData(communications)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            // You can add a ProgressBar to your layout and manage its visibility here
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        // Show/Hide FAB based on user role
        if (viewModel.getUserRole() == "Resident") {
            binding.buttonCreateCommunication.visibility = View.GONE
        } else {
            binding.buttonCreateCommunication.visibility = View.VISIBLE
        }
    }
}
