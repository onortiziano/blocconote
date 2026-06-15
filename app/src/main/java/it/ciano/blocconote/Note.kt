package it.ciano.blocconote

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity dice a Room che questa classe rappresenta una tabella nel database
@Entity(tableName = "note_table")

data class Note(
	@PrimaryKey(autoGenerate = true) val id: Int = 0,
	val testo: String
) 
