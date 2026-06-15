package it.ciano.blocconote

import android.content.Context
import androidx.room.*

// Definiamo quali tabelle (Entity) appartengono al database e la versione (1)
@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    
    // Questa funzione ci restituisce il DAO per poter fare le operazioni
    abstract fun noteDao(): NoteDao

    companion object {
        // Usiamo il pattern Singleton: un unico database per tutta l'app
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            // Se l'istanza esiste già, la restituisco. Altrimenti la creo.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database" // Nome del file salvato sul disco
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}