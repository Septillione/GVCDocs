@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gvcdocs

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentDetailScreen(
    viewModel: DocumentViewModel,
    documentId: Int,
    onBack: () -> Unit,
    onEditDocument: (Int) -> Unit,
    onExportDocument: (Int) -> Unit,
    onApproveDocument: (Int) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showApprovalDialog by remember { mutableStateOf(false) } // Добавляем эту переменную
    val document by viewModel.getDocumentById(documentId).collectAsStateWithLifecycle(initialValue = null)

    // Обработчик подтверждения согласования
    fun handleApprove() {
        document?.id?.let { docId ->
            onApproveDocument(docId)
        }
        showApprovalDialog = false
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить документ?") },
            text = { Text("Вы уверены, что хотите удалить \"${document?.title}\"?") },
            confirmButton = {
                Button(
                    onClick = {
                        document?.let { viewModel.delete(it) }
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(document?.title ?: "Документ") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { document?.id?.let(onEditDocument) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                    }
                    IconButton(onClick = { document?.id?.let(onExportDocument) }) {
                        Icon(Icons.Filled.AddCircle, contentDescription = "Экспорт")
                    }
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Удалить", tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
        },
        floatingActionButton = {
            if (document?.status == DocStatus.DRAFT) {
                ExtendedFloatingActionButton(
                    onClick = { showApprovalDialog = true }, // Показываем диалог вместо прямого вызова
                    icon = { Icon(Icons.Filled.Check, contentDescription = "На согласование") },
                    text = { Text("На согласование") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
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
                    text = "Обновлён: ${SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(document!!.updatedAt))}",
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

        if (showApprovalDialog) {
            AlertDialog(
                onDismissRequest = { showApprovalDialog = false },
                title = { Text("Отправить на согласование") },
                text = { Text("Вы уверены, что хотите отправить этот документ на согласование?") },
                confirmButton = {
                    TextButton(
                        onClick = ::handleApprove
                    ) {
                        Text("Да")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showApprovalDialog = false }) {
                        Text("Нет")
                    }
                }
            )
        }
    }
}

//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import java.text.SimpleDateFormat
//import java.util.*
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DocumentDetailScreen(
//    viewModel: DocumentViewModel,
//    documentId: Int,
//    onBack: () -> Unit,
//    onEditDocument: (Int) -> Unit,
//    onExportDocument: (Int) -> Unit,
//    onApproveDocument: (Int) -> Unit
//) {
//    val document by viewModel.getDocumentById(documentId).collectAsStateWithLifecycle(initialValue = null)
//    val context = LocalContext.current
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(document?.title ?: "Документ") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
//                    }
//                },
//                actions = {
//                    IconButton(onClick = { document?.id?.let(onEditDocument) }) {
//                        Icon(Icons.Default.Edit, contentDescription = "Редактировать")
//                    }
//                    IconButton(onClick = { document?.id?.let(onExportDocument) }) {
//                        Icon(Icons.Filled.AddCircle, contentDescription = "Экспорт в PDF") // Измененная иконка
//                    }
//                }
//            )
//        },
//        floatingActionButton = {
//            if (document?.status == DocStatus.DRAFT) {
//                ExtendedFloatingActionButton(
//                    onClick = {
//                        document?.id?.let { docId ->
//                            // Показываем диалог подтверждения
//                            AlertDialog(
//                                onDismissRequest = { /* */ },
//                                title = { Text("Отправить на согласование") },
//                                text = { Text("Вы уверены, что хотите отправить этот документ на согласование?") },
//                                confirmButton = {
//                                    TextButton(
//                                        onClick = {
//                                            onApproveDocument(docId)
//                                        }
//                                    ) {
//                                        Text("Да")
//                                    }
//                                },
//                                dismissButton = {
//                                    TextButton(onClick = { /* */ }) {
//                                        Text("Нет")
//                                    }
//                                }
//                            )
//                        }
//                    },
//                    icon = { Icon(Icons.Filled.Check, contentDescription = "На согласование") },
//                    text = { Text("На согласование") }
//                )
//            }
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .padding(16.dp)
//                .fillMaxSize()
//        ) {
//            if (document == null) {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//                return@Column
//            }
//
//            Text(
//                text = document!!.title,
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier.fillMaxWidth(),
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = "Тип: ${document!!.type.name}",
//                    style = MaterialTheme.typography.bodyMedium
//                )
//                Text(
//                    text = "Категория: ${document!!.category}",
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = "Создан: ${SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(document!!.createdAt))}",
//                style = MaterialTheme.typography.bodySmall
//            )
//
//            if (document!!.updatedAt != document!!.createdAt) {
//                Text(
//                    text = "Обновлен: ${SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(document!!.updatedAt))}",
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = document!!.description,
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Divider()
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = document!!.content,
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    }
//}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DocumentDetailScreen(
//    viewModel: DocumentViewModel,
//    documentId: Int,
//    navController: NavController
//) {
//    val document by remember(documentId) {
//        viewModel.getDocumentById(documentId)
//    }.collectAsState(initial = null)
//
//    LaunchedEffect(documentId) {
//        viewModel.getDocumentById(documentId).collect { doc ->
//            viewModel.setCurrentDocument(doc)
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(document?.title ?: "Документ") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
//                    }
//                },
//                actions = {
//                    IconButton(onClick = {
//                        document?.let {
//                            navController.navigate("editDocument/${it.id}")
//                        }
//                    }) {
//                        Icon(Icons.Default.Edit, contentDescription = "Редактировать")
//                    }
//                }
//            )
//        }
//    ) { padding ->
//        when {
//            document == null -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(padding),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                        CircularProgressIndicator()
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Text("Загрузка документа...")
//                    }
//                }
//            }
//            else -> {
//                DocumentDetailContent(
//                    document = document!!,
//                    modifier = Modifier.padding(padding)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun DocumentDetailContent(
//    document: Doc,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier
//            .padding(16.dp)
//            .verticalScroll(rememberScrollState())
//    ) {
//        // Заголовок и метаданные
//        Text(
//            text = document.title,
//            style = MaterialTheme.typography.headlineMedium,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(
//                text = "Тип: ${document.type.name}",
//                style = MaterialTheme.typography.bodyMedium
//            )
//            Text(
//                text = "Версия: ${document.version}",
//                style = MaterialTheme.typography.bodyMedium
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Дата создания/обновления
//        Text(
//            text = "Создан: ${SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(document.createdAt))}",
//            style = MaterialTheme.typography.bodySmall,
//            color = MaterialTheme.colorScheme.onSurfaceVariant
//        )
//
//        if (document.updatedAt != document.createdAt) {
//            Text(
//                text = "Обновлен: ${SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(document.updatedAt))}",
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Описание документа
//        if (document.description.isNotBlank()) {
//            Text(
//                text = document.description,
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//
//        Divider()
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Основное содержимое документа
//        Text(
//            text = document.content,
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        // Кнопки действий (если нужно)
//        if (document.status == DocStatus.IN_APPROVAL) {
//            Spacer(modifier = Modifier.height(24.dp))
//            Row(
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Button(onClick = { /* Обработка утверждения */ }) {
//                    Text("Утвердить")
//                }
//                Button(
//                    onClick = { /* Обработка отклонения */ },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.errorContainer
//                    )
//                ) {
//                    Text("Отклонить")
//                }
//            }
//        }
//    }
//}
