package com.example.gvcdocs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun DocumentApp(
    viewModel: DocumentViewModel,
    startDestination: String = "documentList"
) {
    val navController = rememberNavController()
    val actions = remember(navController) { DocumentActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("documentList") {
            DocumentListScreen(
                viewModel = viewModel,
                onDocumentClick = actions.openDocument,
                onAddDocument = actions.addDocument
            )
        }

        composable(
            "documentDetail/{documentId}",
            arguments = listOf(navArgument("documentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val documentId = backStackEntry.arguments?.getInt("documentId") ?: 0
            DocumentDetailScreen(
                viewModel = viewModel,
                documentId = documentId,
                onBack = actions.navigateBack,
                onEditDocument = actions.editDocument,
                onExportDocument = actions.exportDocument,
                onApproveDocument = actions.approveDocument
            )
        }

        composable("addDocument") {
            AddDocumentScreen(
                viewModel = viewModel,
                onBack = actions.navigateBack,
                onSave = actions.navigateBack
            )
        }

        composable(
            "editDocument/{documentId}",
            arguments = listOf(navArgument("documentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val documentId = backStackEntry.arguments?.getInt("documentId") ?: 0
            EditDocumentScreen(
                viewModel = viewModel,
                documentId = documentId,
                onBack = actions.navigateBack,
                onSave = actions.navigateBack
            )
        }

        composable(
            "approveDocument/{documentId}",
            arguments = listOf(navArgument("documentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val documentId = backStackEntry.arguments?.getInt("documentId") ?: 0
            ApprovalScreen(
                viewModel = viewModel,
                docId = documentId,
                onBack = actions.navigateBack
            )
        }

        composable(
            "exportDocument/{documentId}",
            arguments = listOf(navArgument("documentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val documentId = backStackEntry.arguments?.getInt("documentId") ?: 0
            ExportScreen(
                viewModel = viewModel,
                docId = documentId,
                onBack = actions.navigateBack
            )
        }
    }
}

class DocumentActions(private val navController: NavHostController) {
    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }

    val openDocument: (Int) -> Unit = { documentId ->
        navController.navigate("documentDetail/$documentId")
    }

    val addDocument: () -> Unit = {
        navController.navigate("addDocument")
    }

    val editDocument: (Int) -> Unit = { documentId ->
        navController.navigate("editDocument/$documentId")
    }

    val exportDocument: (Int) -> Unit = { documentId ->
        navController.navigate("exportDocument/$documentId")
    }

    val approveDocument: (Int) -> Unit = { documentId ->
        navController.navigate("approveDocument/$documentId")
    }
}

//@Composable
//fun DocumentApp(
//    viewModel: DocumentViewModel,
//    startDestination: String = "documentList"
//) {
//    val navController = rememberNavController()
//    val actions = remember(navController) { DocumentActions(navController) }
//
//    NavHost(
//        navController = navController,
//        startDestination = startDestination
//    ) {
//        composable("documentList") {
//            DocumentListScreen(
//                viewModel = viewModel,
//                onDocumentClick = actions.openDocument,
//                onAddDocument = actions.addDocument
//            )
//        }
//
//        composable(
//            "documentDetail/{documentId}",
//            arguments = listOf(navArgument("documentId") { type = NavType.IntType })
//        ) { backStackEntry ->
//            val documentId = backStackEntry.arguments?.getInt("documentId") ?: 0
//            DocumentDetailScreen(
//                viewModel = viewModel,
//                documentId = documentId,
//                onBack = actions.navigateBack,
//                onEditDocument = actions.editDocument,
//                onExportDocument = actions.exportDocument,
//                onApproveDocument = actions.approveDocument
//            )
//        }
//
//        composable("addDocument") {
//            AddDocumentScreen(
//                viewModel = viewModel,
//                onBack = actions.navigateBack,
//                onSave = { navController.popBackStack() }
//            )
//        }
//
//        composable(
//            "editDocument/{documentId}",
//            arguments = listOf(navArgument("documentId") { type = NavType.IntType })
//        ) { backStackEntry ->
//            val documentId = backStackEntry.arguments?.getInt("documentId") ?: 0
//            EditDocumentScreen(
//                viewModel = viewModel,
//                documentId = documentId,
//                onBack = actions.navigateBack,
//                onSave = { navController.popBackStack() }
//            )
//        }
//
//        composable(
//            "approveDocument/{documentId}",
//            arguments = listOf(navArgument("documentId") { type = NavType.IntType })
//        ) { backStackEntry ->
//            val documentId = backStackEntry.arguments?.getInt("documentId") ?: 0
//            ApprovalScreen(
//                viewModel = viewModel,
//                docId = documentId,
//                onBack = actions.navigateBack
//            )
//        }
//
//        composable(
//            "exportDocument/{documentId}",
//            arguments = listOf(navArgument("documentId") { type = NavType.IntType })
//        ) { backStackEntry ->
//            val documentId = backStackEntry.arguments?.getInt("documentId") ?: 0
//            ExportScreen(
//                viewModel = viewModel,
//                docId = documentId,
//                onBack = actions.navigateBack
//            )
//        }
//    }
//}
//
//class DocumentActions(private val navController: NavHostController) {
//    val navigateBack: () -> Unit = {
//        navController.popBackStack()
//    }
//
//    val openDocument: (Int) -> Unit = { documentId ->
//        navController.navigate("documentDetail/$documentId")
//    }
//
//    val addDocument: () -> Unit = {
//        navController.navigate("addDocument")
//    }
//
//    val editDocument: (Int) -> Unit = { documentId ->
//        navController.navigate("editDocument/$documentId")
//    }
//
//    val exportDocument: (Int) -> Unit = { documentId ->
//        navController.navigate("exportDocument/$documentId")
//    }
//
//    val approveDocument: (Int) -> Unit = { documentId ->
//        navController.navigate("approveDocument/$documentId")
//    }
//}

//import androidx.compose.runtime.Composable
//import androidx.navigation.NavHost
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import org.w3c.dom.Document
//
//@Composable
//fun DocumentApp(viewModel: DocumentViewModel) {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "documentList") {
//        composable("documentList") {
//            DocumentListScreen(viewModel, navController)
//        }
//        composable("documentDetail/{documentId}") { backStackEntry ->
//            val documentId = backStackEntry.arguments?.getString("documentId")?.toIntOrNull()
//            DocumentDetailScreen(viewModel, documentId, navController)
//        }
//        composable("addDocument") {
//            AddDocumentScreen(viewModel, navController)
//        }
//        composable("editDocument/{documentId}") { backStackEntry ->
//            val documentId = backStackEntry.arguments?.getString("documentId")?.toIntOrNull()
//            EditDocumentScreen(viewModel, documentId, navController)
//        }
//    }
//}
