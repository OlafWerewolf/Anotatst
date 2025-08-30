package com.example.anonatsu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// ★ これらの import が無いと「Unresolved reference」になります
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.anonatsu.ui.ChatBubble
import com.example.anonatsu.ui.InputBar
import com.example.anonatsu.ui.theme.AppTheme
import com.example.anonatsu.viewmodel.ChatViewModel
import com.example.anonatsu.viewmodel.ChatViewModelFactory

import androidx.compose.material3.ExperimentalMaterial3Api

class MainActivity : ComponentActivity() {

    // Application を App 型にキャスト（App.kt 必須）
    private val vm: ChatViewModel by viewModels {
        val app = application as App
        ChatViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatRoute()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ChatRoute(
        vm: ChatViewModel = viewModel(
            factory = ChatViewModelFactory((application as App).repository)
        )
    ) {
        val messages by vm.messages.collectAsState()
        val input by vm.input.collectAsState()

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("LINE風（オフライン）") },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    actions = {
                        AssistChip(onClick = { vm.clearAll() }, label = { Text("履歴クリア") })
                    }
                )
            }
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                LazyColumn(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(messages) { msg ->
                        ChatBubble(msg)
                    }
                }
                Divider(color = MaterialTheme.colorScheme.outline)
                InputBar(
                    value = input,
                    onValueChange = vm::onInputChange,
                    onSend = { vm.onSend() },
                    enabled = true
                )
            }
        }
    }
}
