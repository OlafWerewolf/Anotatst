package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.anonatsu.data.ChatRepository
import com.example.anonatsu.engine.RuleEngine
import com.example.anonatsu.model.Message
import com.example.anonatsu.model.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val repo: ChatRepository) : ViewModel() {
    private val engine = RuleEngine()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _input = MutableStateFlow("")
    val input: StateFlow<String> = _input

    fun onInputChange(v: String) { _input.value = v }

    init {
        viewModelScope.launch { _messages.value = repo.history() }
    }

    fun clearAll() {
        viewModelScope.launch {
            repo.clear()
            _messages.value = emptyList()
            // 状態も初期化
            val field = engine::class.java.getDeclaredField("currentState")
            field.isAccessible = true
            field.set(engine, "INIT")
        }
    }

    fun onSend() {
        val text = input.value.trim()
        if (text.isBlank()) return
        _input.value = ""

        viewModelScope.launch {
            val stateAtSend = engine.currentState
            val userMsg = Message(role = Role.USER, text = text, stateAtSend = stateAtSend)
            repo.add(userMsg)
            _messages.value = _messages.value + userMsg

            when (val matched = engine.match(text)) {
                is RuleEngine.Matched.Ignore -> { /* STATE5: 無反応 */ }
                is RuleEngine.Matched.Fallback -> {
                    val bot = Message(role = Role.BOT, text = "失敗しました")
                    repo.add(bot)
                    _messages.value = _messages.value + bot
                }
                is RuleEngine.Matched.Hit -> {
                    engine.advance(matched) { reply ->
                        val bot = Message(role = Role.BOT, text = reply)
                        viewModelScope.launch {
                            repo.add(bot)
                            _messages.value = _messages.value + bot
                        }
                    }
                }
            }
        }
    }
}

class ChatViewModelFactory(private val repo: ChatRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(repo) as T
    }
}
