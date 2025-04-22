package com.example.gvcdocs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.w3c.dom.Document

@Composable
fun DocumentApp(viewModel: DocumentViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "documentList") {
        composable("documentList") {
            DocumentListScreen(viewModel, navController)
        }
        composable("documentDetail/{documentId}") { backStackEntry ->
            val documentId = backStackEntry.arguments?.getString("documentId")?.toIntOrNull()
            DocumentDetailScreen(viewModel, documentId, navController)
        }
        composable("addDocument") {
            AddDocumentScreen(viewModel, navController)
        }
        composable("editDocument/{documentId}") { backStackEntry ->
            val documentId = backStackEntry.arguments?.getString("documentId")?.toIntOrNull()
            EditDocumentScreen(viewModel, documentId, navController)
        }
    }
}
