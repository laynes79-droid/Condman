package com.example.condocare.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.condocare.R
import com.example.condocare.data.model.Complement
import java.text.SimpleDateFormat
import java.util.*

class ComplementAdapter(
    private var complements: List<Complement>
) : RecyclerView.Adapter<ComplementAdapter.ViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_complement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(complements[position], dateFormat)
    }

    override fun getItemCount(): Int = complements.size

    fun updateData(newComplements: List<Complement>) {
        complements = newComplements
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.textViewComplementMessage)
        private val authorTextView: TextView = itemView.findViewById(R.id.textViewComplementAuthor)

        fun bind(complement: Complement, dateFormat: SimpleDateFormat) {
            messageTextView.text = complement.message
            val formattedDate = dateFormat.format(complement.timestamp)
            authorTextView.text = "- ${complement.authorName} on $formattedDate"
        }
    }
}
