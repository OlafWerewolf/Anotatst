package com.example.anonatsu

import android.app.Application
import com.example.anonatsu.data.ChatRepository

class App : Application() {
    // まずはメモリ上の簡易リポジトリでOK（あとでRoomに差し替え可）
    val repository: ChatRepository by lazy { ChatRepository() }
}
