package it.ciano.blocconote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
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
	var notaDaModificare by remember {mutableStateOf<Note?>(null)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Il mio Blocco Note",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = testoInput,
            onValueChange = { nuovoTesto -> testoInput = nuovoTesto },
            label = { Text("Scrivi una nota...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
				if (notaDaModificare != null && testoInput.isNotBlank()) { // <--- Aggiunto controllo vuoto
                    scope.launch {
                    dao.insert(notaDaModificare!!.copy(testo = testoInput))
                    testoInput = ""
					notaDaModificare = null
                    }
                }else if (testoInput.isNotBlank()) {
                        scope.launch {
                            dao.insert(Note(testo = testoInput))
                            testoInput = ""
                        }
				}
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (notaDaModificare == null) "Aggiungi Nota" else "Aggiorna")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(listaNote) { nota -> NoteItem(
                               nota = nota,
                               onDelete = { notaDaEliminare ->
                                   scope.launch {
                                   dao.delete(notaDaEliminare)
                                   }
                               },
							   onEdit = { 
								   notaSelezionata -> notaDaModificare = notaSelezionata
								   testoInput = notaSelezionata.testo
							   }
							   )
            }
        }
    }
}

@Composable
fun NoteItem(nota: Note,
             onDelete: (Note) -> Unit,
			 onEdit: (Note) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
			.clickable {onEdit(nota)}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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