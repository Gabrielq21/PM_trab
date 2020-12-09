package ipvc.estg.pm_trab.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.pm_trab.entity.Note

@Dao
interface NoteDao{
    @Query("SELECT * FROM note_table ORDER BY titulo ASC")
    fun getNotes(): LiveData<List<Note>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)
    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote( note: Note )

}