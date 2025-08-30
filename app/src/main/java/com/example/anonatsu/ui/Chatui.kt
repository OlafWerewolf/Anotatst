package com.example.anonatsu.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.anonatsu.model.Message
import com.example.anonatsu.model.Role

@Composable
fun ChatBubble(msg: Message) {
    val isMe = msg.role == Role.USER
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 10.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Column(horizontalAlignment = if (isMe) Alignment.End else Alignment.Start) {
            val shape = RoundedCornerShape(20.dp)
            if (isMe) {
                Surface(shape = shape, color = MaterialTheme.colorScheme.primary, tonalElevation = 0.dp) {
                    Text(
                        text = msg.text,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .widthIn(max = 320.dp)
                    )
                }
            } else {
                Surface(
                    shape = shape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 0.dp,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Text(
                        text = msg.text,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .widthIn(max = 320.dp)
                    )
                }
            }
            if (isMe && msg.stateAtSend != "STATE5") {
                Text(
                    text = "既読",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 3.dp, end = 6.dp)
                )
            }
        }
    }
}

@Composable
fun InputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    enabled: Boolean
) {
    Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            placeholder = { Text("メッセージを入力") }
        )
        Spacer(Modifier.width(8.dp))
        Button(onClick = onSend, enabled = enabled && value.isNotBlank()) { Text("送信") }
    }
}
