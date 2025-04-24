package com.example.gvcdocs

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApprovalScreen(
    viewModel: DocumentViewModel,
    docId: Int,
    onBack: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var rejectComment by remember { mutableStateOf("") }
    // Добавляем получение документа
    val doc by viewModel.getDocumentById(docId).collectAsStateWithLifecycle(initialValue = null)

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Комментарий отклонения") },
            text = {
                OutlinedTextField(
                    value = rejectComment,
                    onValueChange = { rejectComment = it },
                    label = { Text("Причина") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.rejectDocument(docId, rejectComment)
                        onBack()
                    }
                ) {
                    Text("Отправить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Согласование документа") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                ExtendedFloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Отклонить")
                }

                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.approveDocument(docId, "Система")
                        onBack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Утвердить")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            when {
                doc == null -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                else -> {
                    Text(
                        text = doc!!.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = doc!!.content,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
