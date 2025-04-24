package com.example.gvcdocs

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "documents")
data class Doc(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val content: String,
    val type: DocumentType,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val status: DocStatus = DocStatus.DRAFT,
    val currentApprover: String? = null,
    val filePath: String? = null,
    val approvers: String? = null,
    val version: Int = 1,
    val isImportant: Boolean = false,
    val category: String = "Общие"
) {
    fun getFullFileName(): String {
        return "${title}_v$version.${getFileExtension()}"
    }

    private fun getFileExtension(): String {
        return when (type) {
            DocumentType.PDF -> "pdf"
            DocumentType.DOCX -> "docx"
            else -> "txt"
        }
    }

    fun getFormattedDate(): String {
        return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            .format(Date(updatedAt))
    }

    fun getStatusColor(): Color {
        return when(status) {
            DocStatus.APPROVED -> Color.Green
            DocStatus.REJECTED -> Color.Red
            DocStatus.IN_APPROVAL -> Color.Yellow
            else -> Color.Gray
        }
    }
}

enum class DocStatus {
    DRAFT, IN_APPROVAL, APPROVED, REJECTED, ARCHIVED
}

enum class DocumentType {
    REGULATION,
    POLICY,
    PROCEDURE,
    INSTRUCTION,
    OTHER,
    PDF,
    DOCX
}