package com.example.gvcdocs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gvcdocs.ui.theme.GVCDocsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val repository = DocumentRepository(db.documentDao())
        val viewModelFactory = DocumentViewModelFactory(repository)

        setContent {
            GVCDocsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: DocumentViewModel = viewModel(
                        factory = viewModelFactory
                    )
                    DocumentApp(viewModel)
                }
            }
        }
    }
}

class DocumentViewModelFactory(
    private val repository: DocumentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DocumentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DocumentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
////        val db = AppDatabase.getDatabase(applicationContext)
////        val repository = DocumentRepository(db.documentDao())
//
//        enableEdgeToEdge()
//        setContent {
//            GVCDocsTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    val viewModel: DocumentViewModel = viewModel()
//                    DocumentApp(
//                        viewModel = viewModel,
//                        startDestination = "documentList"
//                    )
//                }
////                Surface(modifier = Modifier.fillMaxSize()) {
////                    val viewModel: DocumentViewModel = viewModel(
////                        factory = DocumentViewModelFactory(repository)
////                    )
////                    DocumentApp(viewModel)
////                }
//            }
//        }
//    }
//}



//class DocumentViewModelFactory(private val repository: DocumentRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(DocumentViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return DocumentViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Неизвестный класс ViewModel")
//    }
//}
