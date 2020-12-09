package ipvc.estg.pm_trab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.pm_trab.R
import ipvc.estg.pm_trab.entity.Note

class NoteAdapter internal constructor(
    context: Context, val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Note>() // Cached copy of words

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloView: TextView = itemView.findViewById(R.id.titulo)
        val dateView: TextView = itemView.findViewById(R.id.date)
        val textoView: TextView = itemView.findViewById(R.id.texto)

        fun bind( note: Note, clickListener: OnItemClickListener ) {
            tituloView.text = note.titulo
            dateView.text = note.date
            textoView.text = note.texto

            itemView.setOnClickListener {
                clickListener.onItemClicked(note)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = notes[position]
        holder.tituloView.text = current.titulo
        holder.dateView.text = current.date
        holder.textoView.text = current.texto

        holder.bind( current, itemClickListener )
    }
    fun getNoteAt(position: Int): Note {
        return notes[position]
    }

    internal fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }
    interface OnItemClickListener {
        fun onItemClicked( note: Note )
    }
    override fun getItemCount() = notes.size
}