package com.example.gvcdocs

import kotlinx.coroutines.flow.Flow

class DocumentRepository(private val documentDao: DocumentDao) {
    val allDocuments: Flow<List<Doc>> = documentDao.getAllDocuments()

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

    suspend fun changeStatus(docId: Int, status: DocStatus) {
        documentDao.updateStatus(docId, status)
    }

    suspend fun approveDocument(docId: Int, approver: String) {
        documentDao.approve(docId, approver, DocStatus.APPROVED)
    }

    suspend fun rejectDocument(docId: Int, comment: String) {
        documentDao.updateContent(docId, "[REJECTED] $comment", DocStatus.REJECTED)
    }

    suspend fun setApprovers(docId: Int, approvers: List<String>) {
        documentDao.setApprovers(docId, approvers.joinToString(","))
    }

    fun getDocumentsByStatus(status: DocStatus): Flow<List<Doc>> {
        return documentDao.getDocumentsByStatus(status)
    }

    suspend fun getLastInsertedDoc(): Doc? {
        return documentDao.getLastInsertedDoc()
    }

    suspend fun updateFilePath(docId: Int, filePath: String) {
        documentDao.updateFilePath(docId, filePath)
    }

    suspend fun createNewVersion(docId: Int): Doc {
        val oldDoc = documentDao.getByIdSync(docId)
        return oldDoc.copy(
            id = 0,
            version = oldDoc.version + 1,
            status = DocStatus.DRAFT,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        ).also { newDoc ->
            documentDao.insert(newDoc)
        }
    }

//    suspend fun createNewVersion(docId: Int): Doc {
//        val oldDoc = documentDao.getByIdSync(docId)
//        return oldDoc.copy(
//            id = 0,
//            version = oldDoc.version + 1,
//            status = DocStatus.DRAFT
//        ).also { newDoc ->
//            documentDao.insert(newDoc)
//        }
//    }
}