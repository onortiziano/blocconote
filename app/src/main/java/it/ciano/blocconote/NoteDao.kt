package it.ciano.blocconote

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    // Prendi tutte le note, ordinate dalla più recente alla più vecchia
    @Query("SELECT * FROM note_table ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Note>>

    // Inserisci una nuova nota. Se l'ID esiste già, sostituiscila.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    // Cancella una nota specifica
    @Delete
    suspend fun delete(note: Note)
}