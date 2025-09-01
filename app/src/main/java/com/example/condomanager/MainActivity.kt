package com.example.condomanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.condomanager.adapters.CommunicationAdapter
import com.example.condomanager.data.CommunicationRepository
import com.example.condomanager.model.Communication
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var communicationAdapter: CommunicationAdapter
    private val communications = mutableListOf<Communication>()
    private lateinit var repository: CommunicationRepository

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted.
            } else {
                // Feature is unavailable.
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = CommunicationRepository.getInstance(applicationContext)

        askNotificationPermission()
        setupRecyclerView()
        setupFabs()
    }

    override fun onResume() {
        super.onResume()
        loadCommunications()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewCommunications)
        recyclerView.layoutManager = LinearLayoutManager(this)
        communicationAdapter = CommunicationAdapter(communications) { selectedCommunication ->
            val intent = Intent(this, CommunicationDetailActivity::class.java).apply {
                putExtra("communication_db_id", selectedCommunication.dbId)
            }
            startActivity(intent)
        }
        recyclerView.adapter = communicationAdapter
    }

    private fun setupFabs() {
        val fabAddCommunication = findViewById<FloatingActionButton>(R.id.fabAddCommunication)
        fabAddCommunication.setOnClickListener {
            val intent = Intent(this, CreateCommunicationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadCommunications() {
        lifecycleScope.launch {
            val dbCommunications = repository.getCommunications()
            communications.clear()
            communications.addAll(dbCommunications)
            communicationAdapter.notifyDataSetChanged()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
