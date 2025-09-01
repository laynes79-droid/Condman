package com.example.condomanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.condomanager.R
import com.example.condomanager.model.Complement
import java.text.SimpleDateFormat
import java.util.Locale

class ComplementAdapter(private val complements: List<Complement>) :
    RecyclerView.Adapter<ComplementAdapter.ComplementViewHolder>() {

    class ComplementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content: TextView = itemView.findViewById(R.id.textViewComplementContent)
        val authorAndDate: TextView = itemView.findViewById(R.id.textViewComplementAuthorAndDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_complement, parent, false)
        return ComplementViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComplementViewHolder, position: Int) {
        val complement = complements[position]
        holder.content.text = complement.content

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString = sdf.format(complement.date)
        holder.authorAndDate.text = holder.itemView.context.getString(
            R.string.item_complement_author_date_format,
            complement.author.name,
            dateString
        )
    }

    override fun getItemCount() = complements.size
}
