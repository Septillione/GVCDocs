package com.example.gvcdocs

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    viewModel: DocumentViewModel,
    docId: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var exportProgress by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    val doc by viewModel.getDocumentById(docId).collectAsStateWithLifecycle(initialValue = null)

    if (exportProgress) {
        LaunchedEffect(Unit) {
            try {
                doc?.let { document ->
                    val pdfFile = PdfExporter.exportToPdf(document, context)
                    viewModel.exportToPdf(document, context)
                    successMessage = "PDF успешно создан: ${pdfFile.name}"
                } ?: run {
                    errorMessage = "Документ не найден"
                }
            } catch (e: Exception) {
                errorMessage = "Ошибка экспорта: ${e.localizedMessage ?: "Неизвестная ошибка"}"
            } finally {
                exportProgress = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Экспорт документа") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                doc == null -> CircularProgressIndicator()
                exportProgress -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Создание PDF...")
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { exportProgress = true }) {
                        Text("Повторить")
                    }
                }
                successMessage != null -> {
                    Text(
                        text = successMessage!!,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBack) {
                        Text("Вернуться")
                    }
                }
                else -> {
                    Text(
                        text = "Документ готов к экспорту:",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = doc!!.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { exportProgress = true },
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text("Экспортировать в PDF")
                    }
                }
            }
        }
    }
}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ExportScreen(
//    viewModel: DocumentViewModel,
//    docId: Int,
//    onBack: () -> Unit
//) {
//    val context = LocalContext.current
//    var exportProgress by remember { mutableStateOf(false) }
//    val doc by viewModel.getDocumentById(docId).collectAsStateWithLifecycle(
//        initialValue = null,
//        context = context as CoroutineContext
//    )
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Экспорт документа") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
//                    }
//                }
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .padding(16.dp)
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            when {
//                doc == null -> CircularProgressIndicator(
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
//                exportProgress -> Column(
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    CircularProgressIndicator()
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text("Создание PDF...")
//                }
//                else -> {
//                    Text(
//                        text = "Документ: ${doc!!.title}",
//                        style = MaterialTheme.typography.titleLarge
//                    )
//                    Spacer(modifier = Modifier.height(24.dp))
//                    Button(
//                        onClick = {
//                            exportProgress = true
//                            viewModel.exportToPdf(doc!!, context)
//                            exportProgress = false
//                        },
//                        modifier = Modifier.width(200.dp)
//                    ) {
//                        Text("Экспортировать в PDF")
//                    }
//                }
//            }
//        }
//    }
//}
