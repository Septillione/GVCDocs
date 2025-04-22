@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gvcdocs

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun DocumentDetailScreen(
    viewModel: DocumentViewModel,
    documentId: Int?,
    navController: NavController
) {
    if (documentId == null) return

    LaunchedEffect(documentId) {
        viewModel.getDocumentById(documentId)
    }

    val docState: State<Doc?> = viewModel.currentDocument.collectAsState()
    val document = docState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(document?.title ?: "Загрузка...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        document?.let {
                            navController.navigate("editDocument/${it.id}")
                        }
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                    }
                    IconButton(onClick = {
                        document?.let {
                            viewModel.delete(it)
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Удалить")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .fillMaxSize()
        ) {
            if (document == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                return@Column
            }

            Text(
                text = document!!.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Тип: ${document!!.type.name}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Категория: ${document!!.category}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Создан: ${SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(document!!.createdAt))}",
                style = MaterialTheme.typography.bodySmall
            )

            if (document!!.updatedAt != document!!.createdAt) {
                Text(
                    text = "Обновлен: ${SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(document!!.updatedAt))}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = document!!.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = document!!.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}


