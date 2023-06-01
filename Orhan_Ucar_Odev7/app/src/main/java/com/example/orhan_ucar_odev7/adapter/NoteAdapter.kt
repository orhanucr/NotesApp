package com.example.orhan_ucar_odev7.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orhan_ucar_odev7.R
import com.example.orhan_ucar_odev7.models.Note

class NoteAdapter(private var notes: List<Note>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val colors = listOf("#FFCDD2", "#F8BBD0", "#E1BEE7", "#D1C4E9", "#C5CAE9", "#FDBE3B", "#FF4842", "#3A52FC", "#FFBB86FC", "#C5CAE9")
    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_container_note,
            parent,
            false
        )
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notes[notes.size - position - 1]
        val randomColor = colors.random()
        holder.bind(currentNote, randomColor)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(notes.size - position - 1)
        }
    }

    override fun getItemCount(): Int = notes.size

    fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textTitle)
        private val detailTextView: TextView = itemView.findViewById(R.id.textNote)
        private val dateTextView: TextView = itemView.findViewById(R.id.textDateTime)

        fun bind(note: Note, color: String) {
            titleTextView.text = note.title
            detailTextView.text = note.detail
            dateTextView.text = note.date

            itemView.setBackgroundResource(R.drawable.background_note)
            itemView.background.setTint(Color.parseColor(color))
        }
    }
}
