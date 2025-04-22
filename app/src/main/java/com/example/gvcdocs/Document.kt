package com.example.gvcdocs

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class Doc(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val content: String,
    val type: DocumentType,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isImportant: Boolean = false,
    val category: String = "Общие"
)

enum class DocumentType {
    REGULATION,
    POLICY,
    PROCEDURE,
    INSTRUCTION,
    OTHER
}