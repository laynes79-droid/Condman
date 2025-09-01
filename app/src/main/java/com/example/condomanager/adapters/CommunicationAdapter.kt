package com.example.condomanager.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.condomanager.R
import com.example.condomanager.model.Communication
import com.example.condomanager.model.CommunicationCategory
import java.text.SimpleDateFormat
import java.util.Locale

class CommunicationAdapter(
    private val communications: List<Communication>,
    private val onItemClick: (Communication) -> Unit
) : RecyclerView.Adapter<CommunicationAdapter.CommunicationViewHolder>() {

    class CommunicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val priorityIndicator: View = itemView.findViewById(R.id.priorityIndicator)
        val title: TextView = itemView.findViewById(R.id.textViewCommunicationTitle)
        val snippet: TextView = itemView.findViewById(R.id.textViewCommunicationSnippet)
        val author: TextView = itemView.findViewById(R.id.textViewCommunicationAuthor)
        val date: TextView = itemView.findViewById(R.id.textViewCommunicationDate)

        fun bind(communication: Communication, onItemClick: (Communication) -> Unit) {
            itemView.setOnClickListener { onItemClick(communication) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunicationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_communication, parent, false)
        return CommunicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunicationViewHolder, position: Int) {
        val communication = communications[position]
        holder.bind(communication, onItemClick)

        val context = holder.itemView.context
        holder.title.text = communication.title
        holder.snippet.text = communication.content
        holder.author.text = context.getString(R.string.communication_list_author_format, communication.author.name)

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.date.text = context.getString(R.string.communication_list_date_format, sdf.format(communication.date))

        if (communication.category == CommunicationCategory.EMERGENCY) {
            holder.priorityIndicator.setBackgroundColor(Color.RED)
        } else {
            holder.priorityIndicator.setBackgroundColor(Color.GREEN)
        }

        if (communication.isClosed) {
            holder.itemView.alpha = 0.5f
        } else {
            holder.itemView.alpha = 1.0f
        }
    }

    override fun getItemCount() = communications.size
}
