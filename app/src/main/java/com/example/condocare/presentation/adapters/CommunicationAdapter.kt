package com.example.condocare.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.condocare.R
import com.example.condocare.data.model.Communication

class CommunicationAdapter(
    private var communications: List<Communication>,
    private val onItemClicked: (Communication) -> Unit
) : RecyclerView.Adapter<CommunicationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_communication, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val communication = communications[position]
        holder.bind(communication)
        holder.itemView.setOnClickListener {
            onItemClicked(communication)
        }
    }

    override fun getItemCount(): Int = communications.size

    fun updateData(newCommunications: List<Communication>) {
        communications = newCommunications
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val statusTextView: TextView = itemView.findViewById(R.id.textViewStatus)

        fun bind(communication: Communication) {
            titleTextView.text = communication.title
            statusTextView.text = communication.status
        }
    }
}
