package com.example.condomanager

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.condomanager.data.CommunicationRepository
import com.example.condomanager.model.Communication
import com.example.condomanager.model.CommunicationCategory
import com.example.condomanager.util.NotificationHelper
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class CreateCommunicationActivity : AppCompatActivity() {

    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_communication)

        notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannel()

        val titleEditText = findViewById<EditText>(R.id.editTextCommunicationTitle)
        val contentEditText = findViewById<EditText>(R.id.editTextCommunicationContent)
        val priorityRadioGroup = findViewById<RadioGroup>(R.id.radioGroupPriority)
        val sendButton = findViewById<Button>(R.id.buttonSendCommunication)

        val repository = CommunicationRepository.getInstance(applicationContext)
        val sharedPreferences = getSharedPreferences("condomanager_prefs", Context.MODE_PRIVATE)
        val loggedInUserId = sharedPreferences.getLong("logged_in_user_id", -1)

        sendButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            val selectedPriorityId = priorityRadioGroup.checkedRadioButtonId
            val category = if (selectedPriorityId == R.id.radioButtonEmergency) {
                CommunicationCategory.EMERGENCY
            } else {
                CommunicationCategory.NORMAL
            }

            if (title.isBlank() || content.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (loggedInUserId == -1L) {
                Toast.makeText(this, "Error: Not logged in.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val author = repository.getUserById(loggedInUserId)
                if (author == null) {
                    Toast.makeText(this@CreateCommunicationActivity, "Error: Could not find user.", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val newCommunication = Communication(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    content = content,
                    category = category,
                    author = author,
                    date = Date()
                )

                repository.createCommunication(newCommunication)

                if (newCommunication.category == CommunicationCategory.EMERGENCY) {
                    notificationHelper.sendNotification(
                        getString(R.string.new_complement_notification_title, newCommunication.title),
                        newCommunication.content
                    )
                }

                Toast.makeText(this@CreateCommunicationActivity, "Communication sent: ${newCommunication.title}", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
