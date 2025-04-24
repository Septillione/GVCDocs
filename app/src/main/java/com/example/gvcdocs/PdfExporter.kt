package com.example.gvcdocs

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object PdfExporter {
    fun exportToPdf(doc: Doc, context: Context): File {
        val pdfDocument = PdfDocument()
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
            typeface = Typeface.DEFAULT
        }

        // Используем стандартный размер A4 (595 x 842 points)
        val pageWidth = 595
        val pageHeight = 842
        val margin = 50f
        var currentPage = 0

        // Создаем файл в каталоге Downloads
//        val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//            ?: context.filesDir
        val downloadsDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        }
        val fileName = "${doc.title}_v${doc.version}.pdf"
        val file = File(downloadsDir, fileName)

        try {
            var yPos = margin
            val lines = doc.content.split("\n")
            val lineHeight = paint.textSize * 1.5f

            // Создаем первую страницу
            var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, ++currentPage).create()
            var page = pdfDocument.startPage(pageInfo)
            var canvas = page.canvas

            // Рисуем заголовок на первой странице
            paint.textSize = 18f
            paint.typeface = Typeface.DEFAULT_BOLD
            canvas.drawText(doc.title, margin, yPos, paint)
            yPos += 30f

            paint.textSize = 12f
            paint.typeface = Typeface.DEFAULT
            canvas.drawText("Версия: ${doc.version}", margin, yPos, paint)
            yPos += 20f
            canvas.drawText("Дата: ${SimpleDateFormat("dd.MM.yyyy").format(Date(doc.createdAt))}", margin, yPos, paint)
            yPos += 40f

            // Рисуем содержимое
            for (line in lines) {
                if (yPos + lineHeight > pageHeight - margin) {
                    // Завершаем текущую страницу и начинаем новую
                    pdfDocument.finishPage(page)
                    pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, ++currentPage).create()
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    yPos = margin
                }
                canvas.drawText(line, margin, yPos, paint)
                yPos += lineHeight
            }

            pdfDocument.finishPage(page)

            // Сохраняем документ
            file.parentFile?.mkdirs()
            FileOutputStream(file).use { fos ->
                pdfDocument.writeTo(fos)
            }

            return file
        } catch (e: Exception) {
            throw IOException("Ошибка при создании PDF: ${e.message}", e)
        } finally {
            pdfDocument.close()
        }
    }
}

//object PdfExporter {
//    fun exportToPdf(doc: Doc, context: Context): File {
//        val pdfDocument = PdfDocument()
//        val paint = Paint().apply {
//            color = Color.BLACK
//            textSize = 12f
//            typeface = Typeface.DEFAULT
//        }
//
//        val pageWidth = 595
//        val pageHeight = 842
//        val margin = 50f
//        var currentPage = 0
//
//        // Создаем файл
//        val fileName = "${doc.title}_v${doc.version}.pdf"
//        val file = File(context.getExternalFilesDir(null), fileName)
//
//        try {
//            var yPos = margin
//            var lines = doc.content.split("\n")
//            val lineHeight = paint.textSize * 1.5f
//
//            // Создаем первую страницу
//            var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, ++currentPage).create()
//            var page = pdfDocument.startPage(pageInfo)
//            var canvas = page.canvas
//
//            // Рисуем заголовок на первой странице
//            paint.textSize = 18f
//            paint.typeface = Typeface.DEFAULT_BOLD
//            canvas.drawText(doc.title, margin, yPos, paint)
//            yPos += 30f
//
//            paint.textSize = 12f
//            paint.typeface = Typeface.DEFAULT
//            canvas.drawText("Версия: ${doc.version}", margin, yPos, paint)
//            yPos += 20f
//            canvas.drawText("Дата: ${SimpleDateFormat("dd.MM.yyyy").format(Date(doc.createdAt))}", margin, yPos, paint)
//            yPos += 40f
//
//            // Рисуем содержимое
//            for (line in lines) {
//                if (yPos + lineHeight > pageHeight - margin) {
//                    // Завершаем текущую страницу и начинаем новую
//                    pdfDocument.finishPage(page)
//                    pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, ++currentPage).create()
//                    page = pdfDocument.startPage(pageInfo)
//                    canvas = page.canvas
//                    yPos = margin
//                }
//                canvas.drawText(line, margin, yPos, paint)
//                yPos += lineHeight
//            }
//
//            pdfDocument.finishPage(page)
//
//            // Сохраняем документ
//            FileOutputStream(file).use { fos ->
//                pdfDocument.writeTo(fos)
//            }
//
//            return file
//        } catch (e: Exception) {
//            throw IOException("Ошибка при создании PDF: ${e.message}")
//        } finally {
//            pdfDocument.close()
//        }
//    }
//}

//object PdfExporter {
//    fun exportToPdf(doc: Doc, context: Context): File {
//        val pdfDocument = PdfDocument()
//
//        // Создаем имя файла с timestamp для уникальности
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//        val fileName = "${doc.title}_${timeStamp}.pdf"
//
//        // Получаем директорию для сохранения
//        val storageDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
//        } else {
//            File(Environment.getExternalStorageDirectory(), "Documents")
//        }.also { it?.mkdirs() }
//
//        val file = File(storageDir, fileName)
//
//        try {
//            // Создаем страницу A4 (595 x 842 points)
//            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
//            val page = pdfDocument.startPage(pageInfo)
//            var canvas = page.canvas
//
//            // Настройки рисования
//            val paint = Paint().apply {
//                color = Color.BLACK
//                textSize = 12f
//                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
//            }
//
//            // Отступы
//            val margin = 50f
//            var yPos = margin
//
//            // Рисуем заголовок
//            paint.textSize = 18f
//            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
//            canvas.drawText(doc.title, margin, yPos, paint)
//            yPos += 30f
//
//            // Рисуем метаданные
//            paint.textSize = 12f
//            paint.typeface = Typeface.DEFAULT
//            canvas.drawText("Версия: ${doc.version}", margin, yPos, paint)
//            yPos += 20f
//            canvas.drawText("Дата создания: ${SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(doc.createdAt))}",
//                margin, yPos, paint)
//            yPos += 30f
//
//            // Рисуем содержимое
//            val lines = doc.content.split("\n")
//            val lineHeight = paint.textSize * 1.5f
//
//            for (line in lines) {
//                if (yPos > pageInfo.pageHeight - margin) {
//                    // Если текст не помещается, создаем новую страницу
//                    pdfDocument.finishPage(page)
//                    val newPage = pdfDocument.startPage(pageInfo)
//                    canvas = newPage.canvas
//                    yPos = margin
//                }
//                canvas.drawText(line, margin, yPos, paint)
//                yPos += lineHeight
//            }
//
//            pdfDocument.finishPage(page)
//
//            // Сохраняем в файл
//            FileOutputStream(file).use { outputStream ->
//                pdfDocument.writeTo(outputStream)
//            }
//
//            return file
//        } catch (e: Exception) {
//            throw IOException("Ошибка при создании PDF: ${e.message}")
//        } finally {
//            pdfDocument.close()
//        }
//    }
//}

//import android.content.Context
//import android.graphics.*
//import android.graphics.pdf.PdfDocument
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//
//object PdfExporter {
//    fun exportToPdf(doc: Doc, context: Context): File {
//        val pdfDocument = PdfDocument()
//        val file = File(context.getExternalFilesDir(null), "${doc.title}_v${doc.version}.pdf")
//
//        try {
//            // 1. Настройка страницы (A4 в пунктах)
//            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
//            val page = pdfDocument.startPage(pageInfo)
//            var canvas = page.canvas
//
//            try {
//                // 2. Настройки стилей
//                val titlePaint = Paint().apply {
//                    color = Color.BLACK
//                    textSize = 18f
//                    isFakeBoldText = true
//                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
//                }
//
//                val headerPaint = Paint().apply {
//                    color = Color.DKGRAY
//                    textSize = 12f
//                }
//
//                val contentPaint = Paint().apply {
//                    color = Color.BLACK
//                    textSize = 12f
//                }
//
//                val metaPaint = Paint().apply {
//                    color = Color.GRAY
//                    textSize = 10f
//                }
//
//                // 3. Заголовок документа
//                canvas.drawText(doc.title, 50f, 50f, titlePaint)
//
//                // 4. Метаданные
//                canvas.drawText("Версия: ${doc.version}", 50f, 80f, headerPaint)
//                canvas.drawText("Статус: ${doc.status.name}", 50f, 100f, headerPaint)
//
//                // 5. Разделительная линия
//                val linePaint = Paint().apply {
//                    color = Color.GRAY
//                    strokeWidth = 1f
//                }
//                canvas.drawLine(50f, 110f, 545f, 110f, linePaint)
//
//                // 6. Содержимое документа с переносами
//                val lines = doc.content.split("\n")
//                var yPos = 130f
//                val lineHeight = contentPaint.textSize * 1.5f
//                val margin = 50f
//                val maxWidth = pageInfo.pageWidth - 2 * margin
//
//                for (line in lines) {
//                    // Перенос текста если строка не помещается
//                    var remainingText = line
//                    while (remainingText.isNotEmpty()) {
//                        // Проверяем, нужно ли создать новую страницу
//                        if (yPos > pageInfo.pageHeight - 50) {
//                            pdfDocument.finishPage(page)
//                            val newPage = pdfDocument.startPage(pageInfo)
//                            canvas = newPage.canvas
//                            yPos = 50f
//                        }
//
//                        // Определяем сколько символов помещается
//                        val charCount = contentPaint.breakText(
//                            remainingText,
//                            true,
//                            maxWidth,
//                            null
//                        )
//
//                        // Рисуем текст
//                        canvas.drawText(
//                            remainingText.substring(0, charCount),
//                            margin,
//                            yPos,
//                            contentPaint
//                        )
//
//                        yPos += lineHeight
//                        remainingText = remainingText.substring(charCount)
//                    }
//                }
//
//                pdfDocument.finishPage(page)
//
//                // 7. Сохранение файла
//                FileOutputStream(file).use { fos ->
//                    pdfDocument.writeTo(fos)
//                }
//
//                return file
//            } catch (e: Exception) {
//                throw IOException("Ошибка при создании PDF: ${e.message}")
//            }
//        } finally {
//            pdfDocument.close()
//        }
//    }
//}


//object PdfExporter {
//    fun exportToPdf(doc: Doc, context: Context): File {
//        val pdfDocument = PdfDocument()
//        val file = File(context.getExternalFilesDir(null), "${doc.title}_v${doc.version}.pdf")
//
//        try {
//            // 1. Создаем страницу
//            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
//            val page = pdfDocument.startPage(pageInfo)
//
//            try {
//                val canvas = page.canvas
//                val paint = Paint().apply {
//                    color = Color.BLACK
//                    textSize = 12f
//                    typeface = Typeface.DEFAULT
//                }
//
//                // 2. Рисуем заголовок
//                canvas.drawText(doc.title, 50f, 50f, paint)
//
//                // 3. Рисуем содержимое
//                val lines = doc.content.split("\n")
//                var yPos = 80f
//                val lineHeight = paint.textSize * 1.5f
//
//                for (line in lines) {
//                    if (yPos > pageInfo.pageHeight - 50) break
//                    canvas.drawText(line, 50f, yPos, paint)
//                    yPos += lineHeight
//                }
//
//                pdfDocument.finishPage(page)
//
//                // 4. Сохраняем в файл
//                FileOutputStream(file).use { fos ->
//                    pdfDocument.writeTo(fos)
//                }
//
//                return file
//            } catch (e: Exception) {
//                throw IOException("Ошибка при создании страницы: ${e.message}")
//            }
//        } finally {
//            pdfDocument.close()
//        }
//    }
//}