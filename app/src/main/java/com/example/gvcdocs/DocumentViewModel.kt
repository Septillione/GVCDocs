package com.example.gvcdocs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DocumentViewModel(private val repository: DocumentRepository) : ViewModel() {
    val allDocuments: Flow<List<Doc>> = repository.allDocuments

    private val _searchResults = MutableStateFlow<List<Doc>>(emptyList())
    val searchResults: StateFlow<List<Doc>> = _searchResults.asStateFlow()

    private val _currentDocument = MutableStateFlow<Doc?>(null)
    val currentDocument: StateFlow<Doc?> = _currentDocument.asStateFlow()

    fun insert(document: Doc) = viewModelScope.launch {
        repository.insert(document)
    }

    fun update(document: Doc) = viewModelScope.launch {
        repository.update(document)
    }

    fun delete(document: Doc) = viewModelScope.launch {
        repository.delete(document)
    }

    fun searchDocuments(query: String) = viewModelScope.launch {
        repository.searchDocuments(query).collect {
            _searchResults.value = it
        }
    }

    fun getDocumentById(id: Int) = viewModelScope.launch {
        repository.getDocumentById(id).collect {
            _currentDocument.value = it
        }
    }

    fun clearCurrentDocument() {
        _currentDocument.value = null
    }

    fun getDocumentsByType(type: DocumentType): Flow<List<Doc>> {
        return repository.getDocumentsByType(type)
    }

    fun getAllCategories(): Flow<List<String>> {
        return repository.getAllCategories()
    }

    fun getDocumentsByCategory(category: String): Flow<List<Doc>> {
        return repository.getDocumentsByCategory(category)
    }
}