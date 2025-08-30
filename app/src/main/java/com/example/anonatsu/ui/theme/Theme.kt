package com.example.anonatsu.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// LINE風（ライト）
private val LineGreen = Color(0xFF06C755)      // LINEロゴグリーン
private val ChatBg    = Color(0xFFF5F6F7)      // トーク背景薄グレー
private val Outline   = Color(0xFFE5E5EA)      // 仕切り/枠の薄グレー

val LineColorScheme = lightColorScheme(
    primary = LineGreen,
    onPrimary = Color.White,
    primaryContainer = LineGreen,
    onPrimaryContainer = Color.White,

    background = ChatBg,
    surface = ChatBg,               // 画面背景をトーク背景に
    onSurface = Color(0xFF111111),

    surfaceVariant = Color.White,   // 相手バブルのベース（白）
    onSurfaceVariant = Color(0xFF111111),

    outline = Outline,
    outlineVariant = Outline
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LineColorScheme,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
