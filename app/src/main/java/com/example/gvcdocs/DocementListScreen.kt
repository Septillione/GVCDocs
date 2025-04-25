package com.example.gvcdocs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DocumentListScreen(
//    viewModel: DocumentViewModel,
//    onDocumentClick: (Int) -> Unit,
//    onAddDocument: () -> Unit
//) {
//    val documents by viewModel.allDocuments.collectAsState(initial = emptyList())
//    var searchQuery by remember { mutableStateOf("") }
//    var showSearch by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { if (!showSearch) Text("Документы организации") else Text("Поиск документов") },
//                actions = {
//                    if (showSearch) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            OutlinedTextField(
//                                value = searchQuery,
//                                onValueChange = { searchQuery = it },
//                                modifier = Modifier
//                                    .weight(1f)
//                                    .padding(end = 8.dp),
//                                placeholder = { Text("Поиск...") },
//                                singleLine = true
//                            )
//                            IconButton(onClick = {
//                                viewModel.searchDocuments(searchQuery)
//                            }) {
//                                Icon(Icons.Default.Search, contentDescription = "Искать")
//                            }
//                            IconButton(onClick = {
//                                showSearch = false
//                                searchQuery = ""
//                                viewModel.searchDocuments("")
//                            }) {
//                                Icon(Icons.Default.Close, contentDescription = "Закрыть поиск")
//                            }
//                        }
//                    } else {
//                        IconButton(onClick = { showSearch = true }) {
//                            Icon(Icons.Default.Search, contentDescription = "Поиск")
//                        }
//                    }
//                }
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = onAddDocument) {
//                Icon(Icons.Default.Add, contentDescription = "Добавить")
//            }
//        }
//    ) { padding ->
//        LazyColumn(modifier = Modifier.padding(padding)) {
//            items(documents) { document ->
//                DocumentListItem(
//                    document = document,
//                    onClick = { onDocumentClick(document.id) }
//                )
//                Divider()
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentListScreen(
    viewModel: DocumentViewModel,
    onDocumentClick: (Int) -> Unit,
    onAddDocument: () -> Unit
) {
    val documents by viewModel.allDocuments.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            RzdTopAppBar(
                title = "Документы РЖД",
                actions = {
                    IconButton(onClick = { showSearch = !showSearch }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Поиск",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddDocument,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (showSearch) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.searchDocuments(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text("Поиск документов") },
                    colors =  OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(documents) { document ->
                    RzdDocumentListItem(
                        document = document,
                        onClick = { onDocumentClick(document.id) }
                    )
                    Divider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
fun DocumentListItem(
    document: Doc,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = document.title,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = document.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = document.type.name,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = SimpleDateFormat("dd.MM.yyyy").format(Date(document.createdAt)),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RzdTopAppBar(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                 Icon(
                     painter = painterResource(R.drawable.rzd_logo),
                     contentDescription = "Логотип РЖД",
                     modifier = Modifier.size(64.dp),
                     tint = Color.Unspecified
                 )
                Text(
                    text = title,
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun RzdButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text)
    }
}

@Composable
fun RzdOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text)
    }
}

@Composable
fun RzdDocumentListItem(
    document: Doc,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = document.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (document.isImportant) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Важный документ",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = document.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = document.type.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )

                Text(
                    text = SimpleDateFormat("dd.MM.yyyy").format(Date(document.createdAt)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DocumentListScreen(
//    viewModel: DocumentViewModel,
//    onDocumentClick: (Int) -> Unit,
//    onAddDocument: () -> Unit
//) {
//    val documents by viewModel.allDocuments.collectAsState(initial = emptyList())
//    var searchQuery by remember { mutableStateOf("") }
//    var showSearch by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Документы организации") },
//                actions = {
//                    IconButton(onClick = { showSearch = !showSearch }) {
//                        Icon(Icons.Default.Search, contentDescription = "Поиск")
//                    }
//                }
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = onAddDocument) {
//                Icon(Icons.Default.Add, contentDescription = "Добавить")
//            }
//        }
//    ) { padding ->
//        Column(modifier = Modifier.padding(padding)) {
//            if (showSearch) {
//                OutlinedTextField(
//                    value = searchQuery,
//                    onValueChange = {
//                        searchQuery = it
//                        viewModel.searchDocuments(it)
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                    placeholder = { Text("Поиск документов") }
//                )
//            }
//
//            LazyColumn(modifier = Modifier.fillMaxSize()) {
//                items(documents) { document ->
//                    DocumentListItem(
//                        document = document,
//                        onClick = { onDocumentClick(document.id) }
//                    )
//                    Divider()
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun DocumentListItem(document: Doc, onClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clickable(onClick = onClick),
//        colors = CardDefaults.cardColors(
//            containerColor = if (document.isImportant)
//                MaterialTheme.colorScheme.primaryContainer
//            else
//                MaterialTheme.colorScheme.surfaceVariant
//        )
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text(
//                    text = document.title,
//                    style = MaterialTheme.typography.titleMedium,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis,
//                    modifier = Modifier.weight(1f))
//                Box(
//                    modifier = Modifier
//                        .size(12.dp)
//                        .background(document.getStatusColor())
//                        .clip(CircleShape)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = document.description,
//                style = MaterialTheme.typography.bodyMedium,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = document.type.name,
//                    style = MaterialTheme.typography.labelSmall
//                )
//
//                Text(
//                    text = document.getFormattedDate(),
//                    style = MaterialTheme.typography.labelSmall
//                )
//            }
//        }
//    }
//}