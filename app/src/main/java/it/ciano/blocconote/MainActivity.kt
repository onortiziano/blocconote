package it.ciano.blocconote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.foundation.BorderStroke
import it.ciano.blocconote.ui.theme.LocalAppColors
import it.ciano.blocconote.ui.theme.BlocconoteTheme
import androidx.activity.enableEdgeToEdge


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		enableEdgeToEdge()
        setContent {
            BlocconoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BloccoNoteApp()
                }
            }
        }
    }
}

@Composable
fun BloccoNoteApp() {
    val context = LocalContext.current
    val dao = NoteDatabase.getDatabase(context).noteDao()
    val scope = rememberCoroutineScope()
    var testoInput by remember { mutableStateOf("") }
    val listaNote by dao.getAllNotes().collectAsState(initial = emptyList())
    var notaDaModificare by remember { mutableStateOf<Note?>(null) }
    var ricerca by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
	val noteFiltrate = listaNote.filter {
		it.testo.contains (ricerca, ignoreCase = true)
	}
	
	val testoBottone = when {
		isSearching -> "Cerca"
		notaDaModificare == null -> "Aggiungi Nota"
		else -> "salva"
	}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Il mio blocco note",
                style = MaterialTheme.typography.headlineMedium
            )
            
            IconButton(onClick = { isSearching = !isSearching; if (! isSearching) ricerca = ""}) { 
                Icon(
                    imageVector = if (isSearching) Icons.Default.Close else Icons.Default.Search, 
                    contentDescription = "Cerca"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        if (isSearching) {
            // --- MODO RICERCA ---
            OutlinedTextField(
                value = ricerca,
                onValueChange = { ricerca = it },
                label = { Text("Cerca tra le note...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        } else {
            // --- MODO INSERIMENTO ---
            OutlinedTextField(
                value = testoInput,
                onValueChange = { nuovoTesto -> testoInput = nuovoTesto },
                label = { Text("Scrivi una nota...") },
                modifier = Modifier.fillMaxWidth()
            )
            
        }
		
		// --- SPAZIO ---
        Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    if (testoInput.isNotBlank()) {
                        scope.launch {
                            if (notaDaModificare != null) {
                                dao.insert(notaDaModificare!!.copy(testo = testoInput))
                                notaDaModificare = null
                            } else {
                                dao.insert(Note(testo = testoInput))
                            }
                            testoInput = ""
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = testoBottone)
            }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            itemsIndexed(noteFiltrate) { index, nota ->
                NoteItem(
                    index = index,
                    nota = nota,
					onDelete = { notaDaEliminare ->
                        scope.launch {
                            dao.delete(notaDaEliminare)
                        }
                    },
                    onEdit = { notaSelezionata ->
                        notaDaModificare = notaSelezionata
                        testoInput = notaSelezionata.testo
                    }
                )
            }
        }
    }
} 
  

@Composable
fun NoteItem(
    index: Int,
    nota: Note,
    onDelete: (Note) -> Unit,
	onEdit: (Note) -> Unit
) {
	val appColors = LocalAppColors.current
	
	
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onEdit(nota) },
        colors = CardDefaults.cardColors(
            containerColor = if (index % 2 == 0) appColors.zebra1 else appColors.zebra2
			
        ),
		border = BorderStroke(1.dp, appColors.border)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nota.testo,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onDelete(nota) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Elimina nota",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}