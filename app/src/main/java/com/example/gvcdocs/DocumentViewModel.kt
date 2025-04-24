package com.example.gvcdocs

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DocumentViewModel(private val repository: DocumentRepository) : ViewModel() {
    val allDocuments: Flow<List<Doc>> = repository.allDocuments

    private val _searchResults = MutableStateFlow<List<Doc>>(emptyList())
    val searchResults: StateFlow<List<Doc>> = _searchResults.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun update(document: Doc) = viewModelScope.launch {
        repository.update(document)
        _toastMessage.value = "Документ обновлен"
    }

    fun delete(document: Doc) = viewModelScope.launch {
        repository.delete(document)
        _toastMessage.value = "Документ удален"
    }

    fun searchDocuments(query: String) = viewModelScope.launch {
        repository.searchDocuments(query).collect {
            _searchResults.value = it
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

    fun getDocumentsByStatus(status: DocStatus): Flow<List<Doc>> {
        return repository.getDocumentsByStatus(status)
    }

    fun createNewVersion(docId: Int) {
        viewModelScope.launch {
            try {
                val newDoc = repository.createNewVersion(docId)
                _toastMessage.value = "Создана новая версия: ${newDoc.version}"
            } catch (e: Exception) {
                _toastMessage.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun approveDocument(docId: Int, approver: String) {
        viewModelScope.launch {
            try {
                repository.approveDocument(docId, approver)
                _toastMessage.value = "Документ отправлен на согласование"
            } catch (e: Exception) {
                _toastMessage.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun rejectDocument(docId: Int, comment: String) {
        viewModelScope.launch {
            try {
                repository.rejectDocument(docId, comment)
                _toastMessage.value = "Документ отклонён: $comment"
            } catch (e: Exception) {
                _toastMessage.value = "Ошибка: ${e.message}"
            }
        }
    }

    private val _currentDocument = MutableStateFlow<Doc?>(null)
    val currentDocument: StateFlow<Doc?> = _currentDocument.asStateFlow()

    // Новый метод для установки текущего документа
    fun setCurrentDocument(doc: Doc) {
        _currentDocument.value = doc
    }

    // Исправленный метод getDocumentById
    fun getDocumentById(id: Int): Flow<Doc> {
        return repository.getDocumentById(id)
            .catch { e ->
                _toastMessage.value = "Ошибка загрузки документа: ${e.message}"
            }
    }

    // Исправленный метод insert
    fun insert(document: Doc) = viewModelScope.launch {
        try {
            repository.insert(document)
            _toastMessage.value = "Документ создан"
            // После создания сразу устанавливаем его как текущий
            repository.getDocumentById(document.id).collect { doc ->
                _currentDocument.value = doc
            }
        } catch (e: Exception) {
            _toastMessage.value = "Ошибка создания документа: ${e.message}"
        }
    }

    fun exportToPdf(doc: Doc, context: Context) {
        viewModelScope.launch {
            try {
                val pdfFile = PdfExporter.exportToPdf(doc, context)
                repository.updateFilePath(doc.id, pdfFile.absolutePath) // Используем repository напрямую
                _toastMessage.value = "PDF создан: ${pdfFile.name}"
            } catch (e: Exception) {
                _toastMessage.value = "Ошибка экспорта: ${e.localizedMessage}"
            }
        }
    }

    fun startApprovalProcess(docId: Int, approvers: List<String>) {
        viewModelScope.launch {
            repository.changeStatus(docId, DocStatus.IN_APPROVAL)
            repository.setApprovers(docId, approvers)
            _toastMessage.value = "Документ отправлен на согласование"
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}