package com.example.gvcdocs

import kotlinx.coroutines.flow.Flow

class DocumentRepository(private val documentDao: DocumentDao) {
    val allDocuments: Flow<List<Doc>> = documentDao.getAllDocuments()

//    suspend fun insert(document: Doc) {
//        documentDao.insert(document)
//    }
//
//    suspend fun update(document: Doc) {
//        documentDao.update(document)
//    }

    suspend fun insert(document: Doc) {
        documentDao.insert(document)
    }

    suspend fun update(document: Doc) {
        documentDao.update(document)
    }

    suspend fun delete(document: Doc) {
        documentDao.delete(document)
    }

    fun getDocumentById(id: Int): Flow<Doc> {
        return documentDao.getDocumentById(id)
    }

    fun searchDocuments(query: String): Flow<List<Doc>> {
        return documentDao.searchDocuments("%$query%")
    }

    fun getDocumentsByType(type: DocumentType): Flow<List<Doc>> {
        return documentDao.getDocumentsByType(type)
    }

    fun getAllCategories(): Flow<List<String>> {
        return documentDao.getAllCategories()
    }

    fun getDocumentsByCategory(category: String): Flow<List<Doc>> {
        return documentDao.getDocumentsByCategory(category)
    }
}