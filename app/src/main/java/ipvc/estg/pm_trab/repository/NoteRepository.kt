package ipvc.estg.pm_trab.repository

import androidx.lifecycle.LiveData
import ipvc.estg.pm_trab.dao.NoteDao
import ipvc.estg.pm_trab.entity.Note

class NoteRepository(private val noteDao: NoteDao){
    val allNotes: LiveData<List<Note>> = noteDao.getNotes()

    suspend fun deleteAll(){
        noteDao.deleteAll()
    }
    suspend fun insert(note: Note){
        noteDao.insert(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }
    suspend fun deleteNote( note: Note ) {
        noteDao.deleteNote( note )
    }

}