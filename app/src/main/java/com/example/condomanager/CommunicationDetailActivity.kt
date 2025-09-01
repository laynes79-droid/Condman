package com.example.condomanager

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.condomanager.adapters.ComplementAdapter
import com.example.condomanager.data.CommunicationRepository
import com.example.condomanager.model.Communication
import com.example.condomanager.model.CommunicationCategory
import com.example.condomanager.model.Complement
import com.example.condomanager.model.Role
import com.example.condomanager.model.User
import com.example.condomanager.util.NotificationHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CommunicationDetailActivity : AppCompatActivity() {

    private var communication: Communication? = null
    private lateinit var complementAdapter: ComplementAdapter
    private lateinit var complementsRecyclerView: RecyclerView
    private lateinit var addComplementEditText: EditText
    private lateinit var addComplementButton: Button
    private lateinit var statusTextView: TextView
    private lateinit var closeCommunicationButton: Button
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var repository: CommunicationRepository

    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_communication_detail)

        notificationHelper = NotificationHelper(this)
        repository = CommunicationRepository.getInstance(applicationContext)
        val sharedPreferences = getSharedPreferences("condomanager_prefs", Context.MODE_PRIVATE)
        val loggedInUserId = sharedPreferences.getLong("logged_in_user_id", -1)

        val communicationId = intent.getLongExtra("communication_db_id", -1)
        if (communicationId == -1L || loggedInUserId == -1L) {
            Toast.makeText(this, R.string.error_loading_communication, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        bindViews()

        lifecycleScope.launch {
            currentUser = repository.getUserById(loggedInUserId)
            communication = repository.getCommunications().find { it.dbId == communicationId }

            if (currentUser == null || communication == null) {
                Toast.makeText(this@CommunicationDetailActivity, R.string.error_loading_communication, Toast.LENGTH_SHORT).show()
                finish()
                return@launch
            }

            val comm = communication!!
            val user = currentUser!!

            setupViews(comm)
            setupComplementsRecyclerView(comm)
            setupAddComplementButton(comm, user)
            setupCloseButton(comm, user)
            updateUiForClosedStatus(comm)
        }
    }

    private fun bindViews() {
        statusTextView = findViewById(R.id.textViewStatus)
        closeCommunicationButton = findViewById(R.id.buttonCloseCommunication)
        addComplementEditText = findViewById(R.id.editTextAddComplement)
        addComplementButton = findViewById(R.id.buttonAddComplement)
        complementsRecyclerView = findViewById(R.id.recyclerViewComplements)
    }

    private fun setupViews(comm: Communication) {
        val titleTextView = findViewById<TextView>(R.id.textViewDetailTitle)
        val authorTextView = findViewById<TextView>(R.id.textViewDetailAuthor)
        val contentTextView = findViewById<TextView>(R.id.textViewDetailContent)

        titleTextView.text = comm.title
        contentTextView.text = comm.content

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString = sdf.format(comm.date)
        authorTextView.text = getString(
            R.string.communication_detail_author_date_format,
            comm.author.name,
            dateString
        )
    }

    private fun setupComplementsRecyclerView(comm: Communication) {
        complementsRecyclerView.layoutManager = LinearLayoutManager(this)
        complementAdapter = ComplementAdapter(comm.complements)
        complementsRecyclerView.adapter = complementAdapter
    }

    private fun setupAddComplementButton(comm: Communication, user: User) {
        addComplementButton.setOnClickListener {
            val complementText = addComplementEditText.text.toString()
            if (complementText.isNotEmpty()) {
                val newComplement = Complement(
                    id = UUID.randomUUID().toString(),
                    author = user,
                    content = complementText,
                    date = Date()
                )
                comm.complements.add(newComplement)

                lifecycleScope.launch {
                    repository.updateCommunication(comm)
                    complementAdapter.notifyItemInserted(comm.complements.size - 1)
                    addComplementEditText.text.clear()
                    complementsRecyclerView.scrollToPosition(comm.complements.size - 1)
                }

                if (comm.category == CommunicationCategory.EMERGENCY) {
                    notificationHelper.sendNotification(
                        getString(R.string.new_complement_notification_title, comm.title),
                        newComplement.content
                    )
                }
            }
        }
    }

    private fun setupCloseButton(comm: Communication, user: User) {
        if (user.role == Role.MANAGER && !comm.isClosed) {
            closeCommunicationButton.visibility = View.VISIBLE
            closeCommunicationButton.setOnClickListener {
                comm.isClosed = true
                lifecycleScope.launch {
                    repository.updateCommunication(comm)
                }
                Toast.makeText(this, R.string.communication_closed_toast, Toast.LENGTH_SHORT).show()
                updateUiForClosedStatus(comm)
            }
        } else {
            closeCommunicationButton.visibility = View.GONE
        }
    }

    private fun updateUiForClosedStatus(comm: Communication) {
        if (comm.isClosed) {
            statusTextView.text = getString(R.string.communication_status_closed)
            addComplementEditText.isEnabled = false
            addComplementButton.isEnabled = false
            closeCommunicationButton.visibility = View.GONE
        } else {
            statusTextView.text = getString(R.string.communication_status_open)
        }
    }
}
