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
}

