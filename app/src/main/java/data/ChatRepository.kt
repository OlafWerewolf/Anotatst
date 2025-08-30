package data

import com.example.anonatsu.model.Message
import java.util.concurrent.atomic.AtomicLong

class ChatRepository {
    private val autoId = AtomicLong(1)
    private val store = mutableListOf<Message>()

    suspend fun history(): List<Message> = store.toList()

    suspend fun add(msg: Message): Long {
        val id = autoId.getAndIncrement()
        store += msg.copy(id = id)
        return id
    }

    suspend fun clear() {
        store.clear()
        autoId.set(1)
    }
}
