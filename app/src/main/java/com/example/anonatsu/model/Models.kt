package com.example.anonatsu.model

enum class Role { USER, BOT }

data class Message(
    val id: Long = 0L,
    val role: Role,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val stateAtSend: String? = null // 送信時の状態（既読表示用）
)
