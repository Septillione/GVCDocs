package com.example.gvcdocs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDocumentScreen(
    viewModel: DocumentViewModel,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(DocumentType.REGULATION) }
    var category by remember { mutableStateOf("Общие") }
    var isImportant by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Добавить документ") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.insert(
                            Doc(
                                title = title,
                                description = description,
                                content = content,
                                type = type,
                                category = category,
                                isImportant = isImportant
                            )
                        )
                        onSave()
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Сохранить")
                    }
                }
            )
        }
    ) { padding ->
        DocumentForm(
            title = title,
            onTitleChange = { title = it },
            description = description,
            onDescriptionChange = { description = it },
            content = content,
            onContentChange = { content = it },
            type = type,
            onTypeChange = { type = it },
            category = category,
            onCategoryChange = { category = it },
            isImportant = isImportant,
            onImportantChange = { isImportant = it },
            modifier = Modifier.padding(padding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDocumentScreen(
    viewModel: DocumentViewModel,
    documentId: Int,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    val document by viewModel.getDocumentById(documentId).collectAsStateWithLifecycle(initialValue = null)

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(DocumentType.REGULATION) }
    var category by remember { mutableStateOf("Общие") }
    var isImportant by remember { mutableStateOf(false) }

    LaunchedEffect(document) {
        if (document != null) {
            title = document!!.title
            description = document!!.description
            content = document!!.content
            type = document!!.type
            category = document!!.category
            isImportant = document!!.isImportant
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактировать документ") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        document?.let { doc ->
                            viewModel.update(
                                doc.copy(
                                    title = title,
                                    description = description,
                                    content = content,
                                    type = type,
                                    category = category,
                                    isImportant = isImportant,
                                    updatedAt = System.currentTimeMillis()
                                )
                            )
                            onSave()
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Сохранить")
                    }
                }
            )
        }
    ) { padding ->
        DocumentForm(
            title = title,
            onTitleChange = { title = it },
            description = description,
            onDescriptionChange = { description = it },
            content = content,
            onContentChange = { content = it },
            type = type,
            onTypeChange = { type = it },
            category = category,
            onCategoryChange = { category = it },
            isImportant = isImportant,
            onImportantChange = { isImportant = it },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun DocumentForm(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    content: String,
    onContentChange: (String) -> Unit,
    type: DocumentType,
    onTypeChange: (DocumentType) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    isImportant: Boolean,
    onImportantChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = content,
            onValueChange = onContentChange,
            label = { Text("Содержание") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            maxLines = Int.MAX_VALUE
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Тип документа:", modifier = Modifier.align(Alignment.CenterVertically))
            DropdownMenuButton(
                items = DocumentType.values().toList(),
                selectedItem = type,
                onItemSelected = onTypeChange,
                itemText = { it.name }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = category,
            onValueChange = onCategoryChange,
            label = { Text("Категория") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isImportant,
                onCheckedChange = onImportantChange
            )
            Text("Важный документ")
        }
    }
}

@Composable
fun <T> DropdownMenuButton(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    itemText: @Composable (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(itemText(selectedItem))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(itemText(item)) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
