package com.example.gvcdocs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Insert
    suspend fun insert(document: Doc)

    @Update
    suspend fun update(document: Doc)

    @Delete
    suspend fun delete(document: Doc)

    @Query("SELECT * FROM documents ORDER BY createdAt DESC")
    fun getAllDocuments(): Flow<List<Doc>>

    @Query("SELECT * FROM documents WHERE id = :id")
    fun getDocumentById(id: Int): Flow<Doc>

    @Query("SELECT * FROM documents WHERE title LIKE :query OR description LIKE :query OR content LIKE :query")
    fun searchDocuments(query: String): Flow<List<Doc>>

    @Query("SELECT * FROM documents WHERE type = :type ORDER BY createdAt DESC")
    fun getDocumentsByType(type: DocumentType): Flow<List<Doc>>

    @Query("SELECT * FROM documents WHERE category = :category ORDER BY createdAt DESC")
    fun getDocumentsByCategory(category: String): Flow<List<Doc>>

    @Query("SELECT DISTINCT category FROM documents")
    fun getAllCategories(): Flow<List<String>>

    @Query("UPDATE documents SET status = :status WHERE id = :docId")
    suspend fun updateStatus(docId: Int, status: DocStatus)

    @Query("UPDATE documents SET status = :status, currentApprover = :approver WHERE id = :docId")
    suspend fun approve(docId: Int, approver: String, status: DocStatus)

    @Query("UPDATE documents SET content = :newContent, status = :status WHERE id = :docId")
    suspend fun updateContent(docId: Int, newContent: String, status: DocStatus)

    @Query("SELECT * FROM documents WHERE id = :docId")
    suspend fun getByIdSync(docId: Int): Doc

    @Query("UPDATE documents SET approvers = :approvers WHERE id = :docId")
    suspend fun setApprovers(docId: Int, approvers: String)

    @Query("SELECT * FROM documents WHERE status = :status ORDER BY createdAt DESC")
    fun getDocumentsByStatus(status: DocStatus): Flow<List<Doc>>

    @Query("SELECT * FROM documents ORDER BY id DESC LIMIT 1")
    suspend fun getLastInsertedDoc(): Doc?

    @Query("UPDATE documents SET filePath = :filePath WHERE id = :docId")
    suspend fun updateFilePath(docId: Int, filePath: String)
}

